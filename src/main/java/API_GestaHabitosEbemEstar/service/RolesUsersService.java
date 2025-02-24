package API_GestaHabitosEbemEstar.service;

import javax.management.relation.RoleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
import API_GestaHabitosEbemEstar.config.security.JwtService;
import API_GestaHabitosEbemEstar.models.Roles;
import API_GestaHabitosEbemEstar.models.RolesUser;
import API_GestaHabitosEbemEstar.models.UsersModel;
import API_GestaHabitosEbemEstar.repository.RolesRepository;
import API_GestaHabitosEbemEstar.repository.RolesUserRepository;
import API_GestaHabitosEbemEstar.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class RolesUsersService {

    @Autowired
    private RolesRepository roleRepository;
    @Autowired
    private RolesUserRepository roleUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UsersService usersService;

    // Logger personalizado para mensagens de INFO manuais
    private static final Logger logger = LoggerFactory.getLogger("logs");

    // Função de logger
    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = null;
        Throwable result = throwable;
        while ((cause = result.getCause()) != null && result != cause) {
            result = cause;
        }
        return result;
    }

    //Relação de usuário com a role de permissão 
    @Transactional 
    public RolesUser relateUserToRole(RolesUser roleUser, Integer userId, String token) {
        try {
            String userRole = jwtService.getRole(token);

            usersService.checkAdminPermission(userRole);

            // Verifica se o usuário existe
            UsersModel user = userRepository.findById(userId).orElseThrow(() -> {
                logger.error("User with id{} not found", userId);
                return new ExceptionHandler.NotFoundException("User not found!");
            });

            // Verifica se a Role existe
            Roles role = roleRepository.findByidRoles(roleUser.getRoleId()).orElseThrow(() -> {
                logger.error("Role with id{} not found!", roleUser.getRoleId());
                return new RoleNotFoundException("Role not found!");
            });

            // Relaciona usuário e role
            roleUser.setUserId(user.getIdUser());
            roleUser.setRoleId(role.getIdRoles());
            roleUserRepository.save(roleUser);

            return roleUser;

        } catch (ExceptionHandler.BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error when associating user {} with role {}.", userId, roleUser.getRoleId(), e);
            throw new ExceptionHandler.BadRequestException(
                    "Error when associating user to role. Details: " + e.getMessage());
        }
    }

}

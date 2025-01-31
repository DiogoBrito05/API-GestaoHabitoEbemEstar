package API_GestaHabitosEbemEstar.service;

import javax.management.relation.RoleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
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

    @Transactional// ainda precisa ser testado 
    public RolesUser relateUserToRole(RolesUser roleUser, Integer userId) {
        try {
            // Verifica se o usuário existe
            UsersModel user = userRepository.findById(userId).orElseThrow(() -> {
                logger.error("Usuário com ID {} não encontrado!", userId);
                return new ExceptionHandler.NotFoundException("Usuário não encontrado!");
            });

            // Verifica se a Role existe
            Roles role = roleRepository.findByidRoles(roleUser.getRoleId()).orElseThrow(() -> {
                logger.error("Role com ID {} não encontrada!", roleUser.getRoleId());
                return new RoleNotFoundException("Role não encontrada!");
            });

            // Relaciona usuário e role
            roleUser.setUserId(user.getIdUser());
            roleUser.setRoleId(role.getIdRoles());
            roleUserRepository.save(roleUser);

            return roleUser;

        } catch (Exception e) {
            logger.error("Erro ao associar usuário {} à role {}.", userId, roleUser.getRoleId(), e);
            throw new ExceptionHandler.BadRequestException("Erro ao associar usuário à role. Detalhes: " + e.getMessage());
        }
    }

}

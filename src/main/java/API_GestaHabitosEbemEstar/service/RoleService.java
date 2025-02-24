package API_GestaHabitosEbemEstar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
import API_GestaHabitosEbemEstar.config.security.JwtService;
import API_GestaHabitosEbemEstar.models.Roles;
import API_GestaHabitosEbemEstar.repository.RolesRepository;

@Service
public class RoleService {

    @Autowired
    private RolesRepository roleRepository;

    @Autowired
    private JwtService jwtService;

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

    public Roles registrationRole(Roles role, String token) {
        try {
            logger.info("Starting role registration.");

            String userRole = jwtService.getRole(token);

            if (userRole == null || (!userRole.equals("ADMIN") && !userRole.contains("ADMIN"))) {
                logger.warn("User without permission");
                throw new ExceptionHandler.BadRequestException("You do not have permission to access this menu!");
            }

            return roleRepository.save(role);
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("error when reregistering role.", rootCause);
            throw new ExceptionHandler.BadRequestException("error when reregistering role. details: " + e.getMessage());
        }

    }

}

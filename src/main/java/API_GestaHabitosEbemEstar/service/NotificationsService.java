package API_GestaHabitosEbemEstar.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
import API_GestaHabitosEbemEstar.config.security.JwtService;
import API_GestaHabitosEbemEstar.models.Notifications;
import API_GestaHabitosEbemEstar.repository.NotificationsRepository;

@Service
public class NotificationsService {

    @Autowired
    private NotificationsRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsersService usersService;

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

    public Notifications newEmail(Notifications gain, String token) {
        logger.info("Starting to record notication");
        try {
            Integer userIdFromToken = Integer.valueOf(jwtService.getUserId(token));
            String userRole = jwtService.getRole(token);

            usersService.checkAdminPermission(userRole);

            gain.setIdUser(userIdFromToken);

            return repository.save(gain);
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error registering Notications.", rootCause);
            throw new ExceptionHandler.BadRequestException(
                    "Error registering Notifications. details: " + e.getMessage());
        }

    }

    public List<Notifications> noticationsList(String token) {
        try {
            logger.info("starting notifications listing");

            String userRole = jwtService.getRole(token);

            usersService.checkAdminPermission(userRole);

            List<Notifications> rewards = repository.findAll();

            return rewards;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error listing notifications.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error listing notifications. details: " + e.getMessage());
        }
    }

    public Notifications getNotifications(Integer id, String token) {
        try {
            logger.info("Starting notification retrieval");

            String userRole = jwtService.getRole(token);
            usersService.checkAdminPermission(userRole);

            return repository.findById(id)
                    .orElseThrow(() -> new ExceptionHandler.NotFoundException("Notifications not found with ID " + id));
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error retrieving category.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error retrieving category. Details: " + e.getMessage());
        }
    }

    public Notifications updateNotification(Integer id, Notifications mesage, String token) {
        logger.info("starting update on notification, categoryId{}", mesage.getIdNotificarions());
        try {

            String role = jwtService.getRole(token);

            Notifications existingNotifications = repository.findById(id)
                    .orElseThrow(() -> new ExceptionHandler.NotFoundException("Notificatios not found!"));

            usersService.checkAdminPermission(role);

            if (mesage.getMesage() != null) {
                existingNotifications.setMesage(mesage.getMesage());
            }

            Notifications updatedNotifications = repository.save(existingNotifications);

            return updatedNotifications;
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error updating notification.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error updating notification. details: " + e.getMessage());
        }

    }

    public void endsNotifications(Integer id, String token) {
        logger.info("Initiating notification deletion, UserID={}", id);
        try {
            String role = jwtService.getRole(token);

            usersService.checkAdminPermission(role);

            repository.deleteById(id);
            logger.info("Notifications with ID {} deleted successfully.", id);

        } catch (ExceptionHandler.NotFoundException e) {
            throw e;
        } catch (ExceptionHandler.BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting Notification.", e);
            throw new ExceptionHandler.BadRequestException("Error deleting Notification. Details: " + e.getMessage());
        }
    }
}

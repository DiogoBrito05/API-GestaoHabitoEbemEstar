package API_GestaHabitosEbemEstar.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
import API_GestaHabitosEbemEstar.config.security.JwtService;
import API_GestaHabitosEbemEstar.models.MonitoringHabits;
import API_GestaHabitosEbemEstar.repository.MonitoringHabitsRepository;

@Service
public class MonitoringHabitsService {

    @Autowired
    private MonitoringHabitsRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HabitsService habitsService;

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

    public MonitoringHabits creationMonitoring( String token,Integer idHabits) {
        logger.info(
                "Starting to record monitoring with data: ");
        try {

            Integer userIdFromToken = Integer.valueOf(jwtService.getUserId(token));
            MonitoringHabits request = new MonitoringHabits();

            LocalDate currentDate = LocalDate.now();
            request.setIdUser(userIdFromToken);
            request.setStatus("Pendente");
            request.setDate(currentDate);
            request.setIdHabits(idHabits);

            return repository.save(request);
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error registering monitoring.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error registering monitoring. details: " + e.getMessage());
        }

    }

    public Optional<MonitoringHabits> searchMonitoring(Integer id, String token) {
        try {
            logger.info("Monitoring recovery started");

            Optional<MonitoringHabits> optionalMonitoring = repository.findById(id);

            if (!optionalMonitoring.isPresent()) {
                throw new ExceptionHandler.NotFoundException("Monitoring not found with ID " + id);
            }

            if (optionalMonitoring.get().getIdHabits() != null) {
                habitsService.searchHabits(optionalMonitoring.get().getIdHabits(), token);
            }

            return optionalMonitoring;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error retrieving habits.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error retrieving habits. Details: " + e.getMessage());
        }
    }

    public List<MonitoringHabits> allMonitoring(Integer idUser, String token) {
        try {
            logger.info("starting monitoring listing");

            Integer userIdFromToken = Integer.valueOf(jwtService.getUserId(token));
            String userRole = jwtService.getRole(token);

            List<MonitoringHabits> optionalMonitoring = repository.findByIdUser(idUser);

            usersService.checkUserPermission(userRole, userIdFromToken, idUser);

            if (optionalMonitoring.isEmpty()) {
                return Collections.emptyList();
            }
            return optionalMonitoring;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error listing monitoring.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error listing monitoring. details: " + e.getMessage());
        }
    }

    public void dellMonitoring(Integer id, String token) {
        logger.info("Initiating monitoring deletion, UserID={}", id);
        try {
            Integer userIdFromToken = Integer.valueOf(jwtService.getUserId(token));
            String role = jwtService.getRole(token);

            MonitoringHabits existingMonitoring = repository.findById(id)
                    .orElseThrow(() -> new ExceptionHandler.NotFoundException("Monitoring not found!"));

            usersService.checkUserPermission(role, existingMonitoring.getIdUser(), userIdFromToken);

            repository.deleteById(id);
            logger.info("Monitoring with ID {} deleted successfully.", id);

        } catch (ExceptionHandler.NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting monitoring.", e);
            throw new ExceptionHandler.BadRequestException("Error deleting monitoring. Details: " + e.getMessage());
        }
    }
}

package API_GestaHabitosEbemEstar.service;

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

    public MonitoringHabits creationMonitoring(MonitoringHabits request, String token) {
        logger.info(
                "Starting to record monitoring with data: ");
        try {

            if (request.getIdHabits() != null) {
                habitsService.searchHabits(request.getIdHabits(), token);
            }

            return repository.save(request);
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error registering monitoring.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error registering monitoring. details: " + e.getMessage());
        }

    }
}

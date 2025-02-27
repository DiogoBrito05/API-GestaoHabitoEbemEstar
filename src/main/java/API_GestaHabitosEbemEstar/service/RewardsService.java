package API_GestaHabitosEbemEstar.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
import API_GestaHabitosEbemEstar.config.security.JwtService;
import API_GestaHabitosEbemEstar.models.Rewards;
import API_GestaHabitosEbemEstar.repository.RewardsRepository;

@Service
public class RewardsService {

    @Autowired
    private RewardsRepository repository;

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

    public Rewards newRewards(Rewards gain, String token) {
        logger.info(
                "Starting to record rewards");
        try {

            Integer userIdFromToken = Integer.valueOf(jwtService.getUserId(token));

            gain.setIdUser(userIdFromToken);

            return repository.save(gain);
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error registering Rewards.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error registering rewards. details: " + e.getMessage());
        }

    }

    public void endsRewards(Integer id, String token) {
        logger.info("Initiating rewards deletion, UserID={}", id);
        try {
            String userIdString = jwtService.getUserId(token);
            Integer userIdInteger = Integer.parseInt(userIdString);
            String role = jwtService.getRole(token);

            Rewards existingRewards = repository.findById(id)
                    .orElseThrow(() -> new ExceptionHandler.NotFoundException("Rewards not found!"));

            usersService.checkUserPermission(role, existingRewards.getIdUser(), userIdInteger);

            repository.deleteById(id);
            logger.info("Rewards with ID {} deleted successfully.", id);

        } catch (ExceptionHandler.NotFoundException e) {
            throw e;
        } catch (ExceptionHandler.BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting rewards.", e);
            throw new ExceptionHandler.BadRequestException("Error deleting rewards. Details: " + e.getMessage());
        }
    }

    public List<Rewards> rewardsList(Integer userID, String token) {
        try {
            logger.info("starting rewards listing");

            String userIdString = jwtService.getUserId(token);
            Integer userIdInteger = Integer.parseInt(userIdString);
            String userRole = jwtService.getRole(token);

            usersService.checkUserPermission(userRole, userIdInteger, userID);

            List<Rewards> rewards = repository.findAllByIdUser(userID);

            if (rewards.isEmpty()) {
                return Collections.emptyList();
            }
            return rewards;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error listing rewards.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error listing rewards. details: " + e.getMessage());
        }
    }

    public Rewards getRewards(Integer id, String token) {
        try {
            logger.info("Starting rewards retrieval");

            Integer userIdFromToken = Integer.valueOf(jwtService.getUserId(token));
            String userRole = jwtService.getRole(token);

            Optional<Rewards> rewardsOptional = repository.findById(id);

            if (!rewardsOptional.isPresent()) {
                throw new ExceptionHandler.NotFoundException("Rewards not found with ID " + id);
            }

            Rewards rewards = rewardsOptional.get();

            usersService.checkUserPermission(userRole, rewards.getIdUser(), userIdFromToken);

            return rewards;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error retrieving rewards.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error retrieving rewards. Details: " + e.getMessage());
        }
    }

}

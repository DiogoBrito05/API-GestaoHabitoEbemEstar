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
import API_GestaHabitosEbemEstar.models.Habits;
import API_GestaHabitosEbemEstar.repository.HabitsRepository;

@Service
public class HabitsService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HabitsRepository repository;

    @Autowired
    private CategoryService categoryService;

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

    public Habits createHabits(Habits requeHabits, String token) {
        logger.info(
                "Starting to record habits with data: UserId={}, Name={}, Description={}, CategoriaId={}, frequency={}, goal={}, startDate={}, Active={} ",
                requeHabits.getName(), requeHabits.getDescription(), requeHabits.getCategoryId(),
                requeHabits.getFrequency(), requeHabits.getGoal(), requeHabits.getStartDate(), requeHabits.getActive());
        try {

            Integer userIdFromToken = Integer.valueOf(jwtService.getUserId(token));

            if (requeHabits.getCategoryId() != null) {
                // Chamo essa função, pois nela já é feito as regras para chamar a categoria
                // E verificar se o usuario tem acesso ou não na categoria específica
                categoryService.getCategory(requeHabits.getCategoryId(), token);
            }

            requeHabits.setIdUser(userIdFromToken);

            return repository.save(requeHabits);
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error registering habits.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error registering habits. details: " + e.getMessage());
        }

    }

    public Habits updateHabits(Integer habitsId, Habits request, String token) {
        logger.info("starting update on habits, habitsId{}", request.getIdHabits());
        try {

            String userIdString = jwtService.getUserId(token);
            Integer userIdInteger = Integer.parseInt(userIdString);
            String role = jwtService.getRole(token);

            Habits existingHabits = repository.findById(habitsId)
                    .orElseThrow(() -> new ExceptionHandler.NotFoundException("Habits not found!"));

            // Verifica se o usuário tem permissão para alterar a categoria
            usersService.checkUserPermission(role, existingHabits.getIdUser(), userIdInteger);

            if (request.getName() != null) {
                existingHabits.setName(request.getName());
            }
            if (request.getDescription() != null) {
                existingHabits.setDescription(request.getDescription());
            }

            if (request.getCategoryId() != null) {
                categoryService.getCategory(request.getCategoryId(), token);
                existingHabits.setCategoryId(request.getCategoryId());

            }

            if (request.getFrequency() != null) {
                existingHabits.setFrequency(request.getFrequency());
            }

            if (request.getGoal() != null) {
                existingHabits.setGoal(request.getGoal());
            }
            if (request.getActive() != null) {
                existingHabits.setActive(request.getActive());
            }

            Habits updatedHabits = repository.save(existingHabits);

            return updatedHabits;
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error updating Category.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error updating category. details: " + e.getMessage());
        }

    }

    public Habits searchHabits(Integer habitsId, String token) {
        try {
            logger.info("Habit recovery started");

            Integer userIdFromToken = Integer.valueOf(jwtService.getUserId(token));
            String userRole = jwtService.getRole(token);

            Optional<Habits> optionalHabits = repository.findById(habitsId);

            if (!optionalHabits.isPresent()) {
                throw new ExceptionHandler.NotFoundException("Habits not found with ID " + habitsId);
            }

            Habits requestHabits = optionalHabits.get();

            usersService.checkUserPermission(userRole, requestHabits.getIdUser(), userIdFromToken);

            return requestHabits;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error retrieving habits.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error retrieving habits. Details: " + e.getMessage());
        }
    }

    public List<Habits> habitsList(Integer userId, String token) {
        try {
            logger.info("starting habits listing");

            Integer userIdFromToken = Integer.valueOf(jwtService.getUserId(token));
            String userRole = jwtService.getRole(token);

            usersService.checkUserPermission(userRole, userIdFromToken, userId);

            List<Habits> searchHabits = repository.findAllByIdUser(userId);

            if (searchHabits.isEmpty()) {
                return Collections.emptyList();
            }
            return searchHabits;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error listing habits.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error listing habits. details: " + e.getMessage());
        }
    }

    public void habitDeleter(Integer habitsId, String token) {
        logger.info("Initiating habits deletion, UserID={}", habitsId);
        try {
            Integer userIdFromToken = Integer.valueOf(jwtService.getUserId(token));
            String role = jwtService.getRole(token);

            Habits existingHabits = repository.findById(habitsId)
                    .orElseThrow(() -> new ExceptionHandler.NotFoundException("Habits not found!"));

            usersService.checkUserPermission(role, existingHabits.getIdUser(), userIdFromToken);

            repository.deleteById(habitsId);
            logger.info("Habits with ID {} deleted successfully.", habitsId);

        } catch (ExceptionHandler.NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting Habits.", e);
            throw new ExceptionHandler.BadRequestException("Error deleting Habits. Details: " + e.getMessage());
        }
    }

}

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
import API_GestaHabitosEbemEstar.models.Category;
import API_GestaHabitosEbemEstar.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

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

    public Category insertCategory(Category insert, String token) {
        logger.info("Starting category registration with data:  CategoryId={}, NameCategory={}",
                insert.getIdCategory(), insert.getNameCategory());
        try {
            String userIdString = jwtService.getUserId(token);
            Integer userIdInteger = Integer.parseInt(userIdString);

            insert.setUserId(userIdInteger);

            return repository.save(insert);
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error registering category.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error registering category. details: " + e.getMessage());
        }

    }

    public Category updateCategory(Integer categoryId, Category category, String token) {
        logger.info("starting update on category, categoryId{}", category.getIdCategory());
        try {

            String userIdString = jwtService.getUserId(token);
            Integer userIdInteger = Integer.parseInt(userIdString);
            String role = jwtService.getRole(token);

            Category existingCategory = repository.findByidCategory(categoryId)
                    .orElseThrow(() -> new ExceptionHandler.NotFoundException("Category not found!"));

            // Verifica se o usuário tem permissão para alterar a categoria
            usersService.checkUserPermission(role, existingCategory.getUserId(), userIdInteger);

            if (category.getNameCategory() != null) {
                existingCategory.setNameCategory(category.getNameCategory());
            }

            Category updatedCategory = repository.save(existingCategory);

            return updatedCategory;
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error updating Category.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error updating category. details: " + e.getMessage());
        }

    }

    public void deleteCategory(Integer categoryId, String token) {
        logger.info("Initiating category deletion, UserID={}", categoryId);
        try {
            String userIdString = jwtService.getUserId(token);
            Integer userIdInteger = Integer.parseInt(userIdString);
            String role = jwtService.getRole(token);

            Category existingCategory = repository.findById(categoryId)
                    .orElseThrow(() -> new ExceptionHandler.NotFoundException("Catefory not found!"));

            usersService.checkUserPermission(role, existingCategory.getUserId(), userIdInteger);

            repository.deleteById(categoryId);
            logger.info("Category with ID {} deleted successfully.", categoryId);

        } catch (ExceptionHandler.NotFoundException e) {
            throw e;
        } catch (ExceptionHandler.BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting category.", e);
            throw new ExceptionHandler.BadRequestException("Error deleting category. Details: " + e.getMessage());
        }
    }

    public List<Category> categoryList(Integer userID, String token) {
        try {
            logger.info("starting category listing");

            String userIdString = jwtService.getUserId(token);
            Integer userIdInteger = Integer.parseInt(userIdString);
            String userRole = jwtService.getRole(token);

            usersService.checkUserPermission(userRole, userIdInteger, userID);

            List<Category> categories = repository.findAllByUserId(userID);

            if (categories.isEmpty()) {
                return Collections.emptyList(); // Retornar uma lista vazia se o usuário não tiver categorias
            }
            return categories;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error listing categories.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error listing categories. details: " + e.getMessage());
        }
    }

//=========================ainda falta acabar
    public Category getCategory(Integer categoryId, String token) {
        try {
            logger.info("starting category retrieval");
    
            String userIdString = jwtService.getUserId(token);
            Integer userIdInteger = Integer.parseInt(userIdString);
            String userRole = jwtService.getRole(token);
            
            usersService.checkUserPermission(userRole, userIdInteger, userIdInteger); 
    
            // Buscar a categoria específica associada ao userID
            Optional<Category> category = repository.findByidCategory(categoryId);
    
            if (!category.isPresent()) {
                throw new ExceptionHandler.NotFoundException("Category not found" );
            }
    
            return category.get();  
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error retrieving category.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error retrieving category. details: " + e.getMessage());
        }
    }
    
}

package API_GestaHabitosEbemEstar.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
import API_GestaHabitosEbemEstar.models.Category;
import API_GestaHabitosEbemEstar.repository.CategoryRepository;

@Service
public class CategoryService {


    @Autowired
    private CategoryRepository repository;

    
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


    public Category insertCategory(Category insert) {
        logger.info("Starting category registration with data:  CategoryId={}, NameCategory={}",
                insert.getIdCategory(), insert.getNameCategory());
        try {

            if (repository.findByidCategory(insert.getIdCategory()).isPresent()) {
                logger.error("Category already registered");
                throw new ExceptionHandler.Conflict("Category already registered!");   
            }
                    

            return repository.save(insert);
        }  catch (ExceptionHandler.Conflict e) {
            throw  e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error registering category.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error registering category. details: " + e.getMessage());
        }

    }

    public Category updateCategory(Integer categoryId, Category category) {
       // logger.info("Start user update:  UserID={}, Name={}, Email={}, Creation_Date={}, Senha={********}",
         //       user.getIdUser(), user.getUsername(), user.getEmail(), user.getDateCreation());
        try {
            Category existingCategory = repository.findByidCategory(categoryId)
            .orElseThrow(() -> new ExceptionHandler.NotFoundException("Category not found!"));

            if(category.getNameCategory() != null){
                existingCategory.setNameCategory(category.getNameCategory());
            }

            
            Category updatedCategory = repository.save(existingCategory);

            return updatedCategory;
        }  catch (ExceptionHandler.Conflict e) {
            throw  e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error updating Category.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error updating category. details: " + e.getMessage());
        }

    }
    
}

package API_GestaHabitosEbemEstar.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import API_GestaHabitosEbemEstar.models.Category;
import API_GestaHabitosEbemEstar.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/register")
    public ResponseEntity<?> registrationCategory(@RequestBody Category category,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        Category newCategory = categoryService.insertCategory(category, token);
        return ResponseEntity.ok(newCategory);
    }

    @PutMapping("/categoryUpdate/{categoryId}")
    public ResponseEntity<?> categoryUpdate(@PathVariable Integer categoryId, @RequestBody Category category,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        Category newCategory = categoryService.updateCategory(categoryId, category, token);
        return ResponseEntity.ok(newCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> categoryDelete(@PathVariable Integer categoryId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        categoryService.deleteCategory(categoryId, token);
        return ResponseEntity.ok("Category deleted successfully.");
    }

    @GetMapping("/listCategorys/{userId}")
    public ResponseEntity<?> listCategory(@PathVariable Integer userId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        List<Category> allCategory = categoryService.categoryList(userId, token);
        return ResponseEntity.ok(allCategory);
    }

    @GetMapping("/search/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Integer categoryId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        Category category = categoryService.getCategory(categoryId, token);
        return ResponseEntity.ok(category);
    }

}

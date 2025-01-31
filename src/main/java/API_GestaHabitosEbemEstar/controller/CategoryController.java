package API_GestaHabitosEbemEstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import API_GestaHabitosEbemEstar.models.Category;
import API_GestaHabitosEbemEstar.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/register")
    public ResponseEntity<?> registrationCategory(@RequestBody Category category){
        Category newCategory = categoryService.insertCategory(category);
        return ResponseEntity.ok(newCategory);
    }

    @PutMapping("/categoryUpdate/{categoryId}")
    public ResponseEntity<?> categoryUpdate(@RequestParam Integer categoryId, @RequestBody Category category ) {
        Category newCategory = categoryService.updateCategory(categoryId, category);
        return ResponseEntity.ok(newCategory);
    }
}
     
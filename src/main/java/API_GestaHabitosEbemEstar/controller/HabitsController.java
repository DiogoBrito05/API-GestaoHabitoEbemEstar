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
import API_GestaHabitosEbemEstar.config.security.ExtractBearer;
import API_GestaHabitosEbemEstar.models.Habits;
import API_GestaHabitosEbemEstar.service.HabitsService;

@RestController
@RequestMapping("/habits")
public class HabitsController {

    @Autowired
    private HabitsService service;


    @Autowired
    private ExtractBearer extratorBearer;

    @PostMapping("/newHabits")
    public ResponseEntity<?> registerHabits(@RequestBody Habits request,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        Habits habitsNEW = service.createHabits(request, token);
        return ResponseEntity.ok(habitsNEW);
    }

    @PutMapping("/Update/{habitsId}")
    public ResponseEntity<?> categoryUpdate(@PathVariable Integer habitsId, @RequestBody Habits updHabits,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        Habits newHabits = service.updateHabits(habitsId, updHabits, token);
        return ResponseEntity.ok(newHabits);
    }


    @GetMapping("/search/{habitsId}")
    public ResponseEntity<?> getHabits(@PathVariable Integer habitsId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        Habits habits = service.searchHabits(habitsId, token);
        return ResponseEntity.ok(habits);
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<?> listCategory(@PathVariable Integer userId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        List<Habits> allhabits = service.habitsList(userId, token);
        return ResponseEntity.ok(allhabits);
    }

    @DeleteMapping("/{habitsId}")
    public ResponseEntity<?> habitsDelete(@PathVariable Integer habitsId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        service.habitDeleter(habitsId, token);
        return ResponseEntity.ok("Habits deleted successfully.");
    }

}

package API_GestaHabitosEbemEstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import API_GestaHabitosEbemEstar.models.Habits;
import API_GestaHabitosEbemEstar.service.HabitsService;

@RestController
@RequestMapping("/habits")
public class HabitsController {

    @Autowired
    private HabitsService service;

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

}

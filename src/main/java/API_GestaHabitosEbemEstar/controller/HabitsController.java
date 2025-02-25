// package API_GestaHabitosEbemEstar.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import API_GestaHabitosEbemEstar.models.Habits;
// import API_GestaHabitosEbemEstar.service.HabitsService;

// @RestController
// @RequestMapping("/habits")
// public class HabitsController {


//     @Autowired
//     private HabitsService service;

//     @PostMapping("/newHabits")
//     public ResponseEntity<?> registerHabits(@RequestBody Habits request) {
//         Habits habitsNEW = service.createHabits(request);
//         return ResponseEntity.ok(habitsNEW);
//     }



// }

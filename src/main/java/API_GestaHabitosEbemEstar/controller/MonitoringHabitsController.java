package API_GestaHabitosEbemEstar.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import API_GestaHabitosEbemEstar.config.security.ExtractBearer;
import API_GestaHabitosEbemEstar.models.MonitoringHabits;
import API_GestaHabitosEbemEstar.service.MonitoringHabitsService;

@RestController
@RequestMapping("/monitoring")
public class MonitoringHabitsController {

    @Autowired
    private MonitoringHabitsService service;
    @Autowired
    private ExtractBearer extratorBearer;

    // @PostMapping("/newMonitoring")
    // public ResponseEntity<?> userRegistration(@RequestBody MonitoringHabits request,
    //         @RequestHeader("Authorization") String authorizationHeader) {
    //     String token = extratorBearer.extractToken(authorizationHeader);
    //     MonitoringHabits newMoni = service.creationMonitoring(request, token);
    //     return ResponseEntity.ok(newMoni);
    // }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> getMonitoring(@PathVariable Integer id,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        Optional<MonitoringHabits> moni = service.searchMonitoring(id, token);
        return ResponseEntity.ok(moni);
    }

    @GetMapping("/list/{idUser}")
    public ResponseEntity<?> listMonitoring(@PathVariable Integer idUser,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        List<MonitoringHabits> all = service.allMonitoring(idUser,token);
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> monitoringDelete(@PathVariable Integer id,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        service.dellMonitoring(id, token);
        return ResponseEntity.ok("Monitoring deleted successfully.");
    }
}

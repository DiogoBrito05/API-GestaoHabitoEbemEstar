package API_GestaHabitosEbemEstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/newMonitoring")
    public ResponseEntity<?> userRegistration(@RequestBody MonitoringHabits request,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        MonitoringHabits newMoni = service.creationMonitoring(request, token);
        return ResponseEntity.ok(newMoni);
    }

}

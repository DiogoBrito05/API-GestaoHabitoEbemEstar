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
import API_GestaHabitosEbemEstar.models.Notifications;
import API_GestaHabitosEbemEstar.service.NotificationsService;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {

    @Autowired
    private ExtractBearer extratorBearer;

    @Autowired
    private NotificationsService service;

    @PostMapping("/registerEmails")
    public ResponseEntity<?> createEmails(@RequestBody Notifications requestEmail,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        Notifications gainEmail = service.newEmail(requestEmail, token);
        return ResponseEntity.ok(gainEmail);
    }

    @GetMapping("/list")
    public ResponseEntity<?> listRewards(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        List<Notifications> allEmails = service.noticationsList(token);
        return ResponseEntity.ok(allEmails);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> serachMensages(@PathVariable Integer id,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        Notifications serachNotifications = service.getNotifications(id, token);
        return ResponseEntity.ok(serachNotifications);
    }

    @PutMapping("/noticatiosUpdate/{id}")
    public ResponseEntity<?> noticatiosUpdate(@PathVariable Integer id, @RequestBody Notifications notication,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        Notifications newMensages = service.updateNotification(id, notication, token);
        return ResponseEntity.ok(newMensages);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> dellMensages(@PathVariable Integer id,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        service.endsNotifications(id, token);
        return ResponseEntity.ok("Notification deleted successfully.");
    }

}



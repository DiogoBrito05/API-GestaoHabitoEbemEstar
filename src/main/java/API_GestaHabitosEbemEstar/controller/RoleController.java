package API_GestaHabitosEbemEstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import API_GestaHabitosEbemEstar.service.RoleService;
import API_GestaHabitosEbemEstar.models.Roles;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

   @PostMapping("/roleRegistration")
    public ResponseEntity<?> createRole(@RequestBody Roles role, @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        Roles newRole = roleService.registrationRole(role, token);
        return ResponseEntity.ok(newRole);
    }
}

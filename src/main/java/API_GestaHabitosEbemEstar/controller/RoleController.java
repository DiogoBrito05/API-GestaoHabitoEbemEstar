package API_GestaHabitosEbemEstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<?> createRole(@RequestBody Roles role) {
        Roles newRole = roleService.registrationRole(role);
        return ResponseEntity.ok(newRole);
    }
}

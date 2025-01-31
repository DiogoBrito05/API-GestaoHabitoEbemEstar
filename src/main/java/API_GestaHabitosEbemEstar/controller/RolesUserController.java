package API_GestaHabitosEbemEstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import API_GestaHabitosEbemEstar.service.RolesUsersService;
import API_GestaHabitosEbemEstar.models.RolesUser;

@RestController
@RequestMapping("/rolesUsers")
public class RolesUserController {

    @Autowired
    private RolesUsersService roleUserService;

    @PostMapping("/assign")
    public ResponseEntity<?> relateUserAndRole(@RequestBody RolesUser roleUser, @RequestParam Integer userId) {
        RolesUser newRelation = roleUserService.relateUserToRole(roleUser, userId);
        return ResponseEntity.ok(newRelation);
    }
}

package API_GestaHabitosEbemEstar.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import API_GestaHabitosEbemEstar.models.UsersModel;
import API_GestaHabitosEbemEstar.service.UsersService;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/user")
public class UsersCrontoller {

    @Autowired
    private UsersService userService;

    @PostMapping("/userRegistration")
    public ResponseEntity<?> userRegistration(@RequestBody UsersModel user){
        UsersModel newUser = userService.insertUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/userUpdate/{userId}")
    public ResponseEntity<?> userUpdate(@PathVariable Integer userId, @RequestBody UsersModel user,  @RequestHeader("Authorization") String authorizationHeader ) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
        }
        UsersModel newUser = userService.updateUsuario(userId, user, token);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/listUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listUser(){
        List<UsersModel> allUser = userService.userList();
        return ResponseEntity.ok(allUser);
    }


}

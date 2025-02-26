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
import API_GestaHabitosEbemEstar.models.UsersModel;
import API_GestaHabitosEbemEstar.service.UsersService;

@RestController
@RequestMapping("/user")
public class UsersCrontoller {

    @Autowired
    private UsersService userService;
    
    @Autowired
    private ExtractBearer extratorBearer;

    @PostMapping("/userRegistration")
    public ResponseEntity<?> userRegistration(@RequestBody UsersModel user) {
        UsersModel newUser = userService.insertUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/userUpdate/{userId}")
    public ResponseEntity<?> userUpdate(@PathVariable Integer userId, @RequestBody UsersModel user,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        UsersModel newUser = userService.updateUsuario(userId, user, token);
        return ResponseEntity.ok(newUser);
    }

    @DeleteMapping("/userDelete/{userId}")
    public ResponseEntity<?> userDelete(@PathVariable Integer userId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        userService.cancelUser(userId, token);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @GetMapping("/listUsers")
    public ResponseEntity<?> listUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        List<UsersModel> allUser = userService.userList(token);
        return ResponseEntity.ok(allUser);
    }

}

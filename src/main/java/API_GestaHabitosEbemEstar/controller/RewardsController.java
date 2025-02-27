package API_GestaHabitosEbemEstar.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import API_GestaHabitosEbemEstar.config.security.ExtractBearer;
import API_GestaHabitosEbemEstar.models.Rewards;
import API_GestaHabitosEbemEstar.service.RewardsService;

@RestController
@RequestMapping("/rewards")
public class RewardsController {

    @Autowired
    private RewardsService service;

    @Autowired
    private ExtractBearer extratorBearer;

    @PostMapping("/register")
    public ResponseEntity<?> createRewards(@RequestBody Rewards request,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        Rewards newGain = service.newRewards(request, token);
        return ResponseEntity.ok(newGain);
    }

    @DeleteMapping("/{rewardsId}")
    public ResponseEntity<?> rewardsDelete(@PathVariable Integer rewardsId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        service.endsRewards(rewardsId, token);
        return ResponseEntity.ok("Rewards deleted successfully.");
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<?> listRewards(@PathVariable Integer userId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        List<Rewards> allRewards = service.rewardsList(userId, token);
        return ResponseEntity.ok(allRewards);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> serachRewards(@PathVariable Integer id,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extratorBearer.extractToken(authorizationHeader);
        Rewards rewards = service.getRewards(id, token);
        return ResponseEntity.ok(rewards);
    }

}

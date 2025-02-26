package API_GestaHabitosEbemEstar.config.security;

import org.springframework.stereotype.Component;

@Component
public class ExtractBearer {
    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}

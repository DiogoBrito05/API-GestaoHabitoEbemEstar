package API_GestaHabitosEbemEstar.config.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
import API_GestaHabitosEbemEstar.models.UsersModel;

@Service
public class JwtService {


    // Logger personalizado para mensagens de INFO manuais
    private static final Logger logger = LoggerFactory.getLogger("logs");

    // Função de logger
    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = null;
        Throwable result = throwable;
        while ((cause = result.getCause()) != null && result != cause) {
            result = cause;
        }
        return result;
    }
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private Long jwtExpiration;

    public String generateToken(UsersModel user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            Map<String, Object> claims = new HashMap<>();
            claims.put("ID", user.getIdUser());
            claims.put("Name", user.getName());
            claims.put("Email", user.getEmail());
            claims.put("Roles", user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));

            Date expirationDate = new Date(System.currentTimeMillis() + jwtExpiration);
            String token = JWT.create()
                    .withIssuer("API-GestaoHabitos")
                    .withSubject(user.getUsername())
                    .withExpiresAt(expirationDate)
                    .withClaim("user", claims)
                    .sign(algorithm);
            return token;
        } catch (ExceptionHandler.NotFoundException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("API-GestaoHabitos")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }
//==========================================================================================
                            //Decodificador de token 

    public Map<String, Object> decodeToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("Issuer", decodedJWT.getIssuer());
            tokenData.put("Subject", decodedJWT.getSubject());
            tokenData.put("ExpiresAt", decodedJWT.getExpiresAt());
            tokenData.put("Claims", decodedJWT.getClaim("user").asMap());
            return tokenData;
        } catch (Exception e) {
            logger.error("Error decoding token: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    public String getUserId(String token) {
        Map<String, Object> claims = decodeToken(token);
        if (claims.containsKey("Claims")) {
            Map<String, Object> userClaims = (Map<String, Object>) claims.get("Claims");
            return userClaims.get("ID").toString();
        }
        return null;
    }

    public String getRole(String token) {
        Map<String, Object> claims = decodeToken(token);
        if (claims.containsKey("Claims")) {
            Map<String, Object> userClaims = (Map<String, Object>) claims.get("Claims");
            return userClaims.get("Roles").toString();
        }
        return null;
    }

}

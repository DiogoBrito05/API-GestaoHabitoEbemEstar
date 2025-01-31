package API_GestaHabitosEbemEstar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
import API_GestaHabitosEbemEstar.config.security.JwtService;
import API_GestaHabitosEbemEstar.dtos.JwtDTO;
import API_GestaHabitosEbemEstar.dtos.LoginUserDto;
import API_GestaHabitosEbemEstar.models.UsersModel;
import API_GestaHabitosEbemEstar.repository.UserRepository;
import com.google.gson.Gson;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

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

    public String authentication(LoginUserDto loginUserDto) {
        logger.info("starting Login");
        try {

            if (loginUserDto == null || loginUserDto.getEmail() == null || loginUserDto.getPassword() == null) {
                throw new ExceptionHandler.NotAuthorizedException("Invalid credentials");
            }
            // Autenticar o usuário com o AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));

            UsersModel user = userRepository.findBySearchEmail(loginUserDto.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Gerar o token
            String jwt = jwtService.generateToken(user);
            

            var token = new JwtDTO(jwt);

            Gson gson = new Gson();

            String json = gson.toJson(token);
           

            return json;
        } catch (ExceptionHandler.NotAuthorizedException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (ExceptionHandler.BadRequestException e) {
            logger.error(e.getMessage());
            throw new ExceptionHandler.BadRequestException("internal error: " + e.getMessage());
        }
    }

}

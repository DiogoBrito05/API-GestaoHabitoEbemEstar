package API_GestaHabitosEbemEstar.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
import API_GestaHabitosEbemEstar.config.security.JwtService;
import API_GestaHabitosEbemEstar.models.UsersModel;
import API_GestaHabitosEbemEstar.repository.UserRepository;
import java.util.List;

import org.slf4j.Logger;

@Service
public class UsersService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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

    public UsersModel insertUser(UsersModel user) {
        logger.info(
                "Starting User registration with data:  UserID={}, Name={}, Email={}, Creation_Date={}, Senha={********}",
                user.getIdUser(), user.getUsername(), user.getEmail(), user.getDateCreation());
        try {

            if (repository.findBySearchEmail(user.getEmail()).isPresent()) {
                logger.error("User already registered");
                throw new ExceptionHandler.Conflict("User already registered!");
            }

            // Criptografa a senha antes de salvar
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            return repository.save(user);
        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error registering user.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error registering user. details: " + e.getMessage());
        }

    }

    public UsersModel updateUsuario(Integer userId, UsersModel user, String token) {
        logger.info("Start user update:  UserID={}, Name={}, Email={}, Creation_Date={}, Senha={********}",
                user.getIdUser(), user.getUsername(), user.getEmail(), user.getDateCreation());
        try {
            String userIdString = jwtService.getUserId(token);
            Integer userIdInteger = Integer.parseInt(userIdString);
            String role = jwtService.getRole(token);

            UsersModel existingUser = repository.findByidUser(userId)
                    .orElseThrow(() -> new ExceptionHandler.NotFoundException("User not found!"));

            // boolean isAdmin = "ADMIN".equals(role.trim().toUpperCase());

            // Se for ADMIN, ele pode alterar qualquer usuário
            if (role.contains("ADMIN")) {
                logger.info("User is ADMIN. Skipping ownership check.");
            }
            // Se não for ADMIN, só pode modificar a própria conta
            else if (!existingUser.getIdUser().equals(userIdInteger)) {
                throw new ExceptionHandler.BadRequestException("You do not have permission to modify this user!");
            }

            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getName() != null) {
                existingUser.setName(user.getName());
            }
            if (user.getPassword() != null) {
                existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }

            return repository.save(existingUser);

        } catch (ExceptionHandler.Conflict e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating user.", e);
            throw new ExceptionHandler.BadRequestException("Error updating user. Details: " + e.getMessage());
        }
    }

    public List<UsersModel> userList() {
        try {
            logger.info("starting user listing");
            var teste = repository.findAll();

            return teste;
        } catch (Exception e) {
            Throwable rootCause = getRootCause(e);
            logger.error("Error listing users.", rootCause);
            throw new ExceptionHandler.BadRequestException("Error listing users. details: " + e.getMessage());
        }
    }

}

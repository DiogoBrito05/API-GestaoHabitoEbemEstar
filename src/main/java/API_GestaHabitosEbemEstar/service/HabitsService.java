// package API_GestaHabitosEbemEstar.service;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import API_GestaHabitosEbemEstar.config.exception.ExceptionHandler;
// import API_GestaHabitosEbemEstar.config.security.JwtService;
// import API_GestaHabitosEbemEstar.models.Habits;
// import API_GestaHabitosEbemEstar.models.Roles;
// import API_GestaHabitosEbemEstar.repository.HabitsRepository;

// @Service
// public class HabitsService {

//     @Autowired
//     private JwtService jwtService;

//     @Autowired
//     private HabitsRepository repository;

//     // Logger personalizado para mensagens de INFO manuais
//     private static final Logger logger = LoggerFactory.getLogger("logs");

//     // Função de logger
//     private Throwable getRootCause(Throwable throwable) {
//         Throwable cause = null;
//         Throwable result = throwable;
//         while ((cause = result.getCause()) != null && result != cause) {
//             result = cause;
//         }
//         return result;
//     }

//     public Habits createHabits(Habits requeHabits) {
//         logger.info(
//                 "Starting to record habits with data: UserId={}, Name={}, Description={}, CategoriaId={}, frequency={}, goal={}, startDate={}, Active={} ",
//                 requeHabits.getName(), requeHabits.getDescription(), requeHabits.getCategoryId(), requeHabits.getFrequency(), requeHabits.getGoal(),requeHabits.getStartDate(), requeHabits.getActive());
//         try {

//             // if (repository.findBySearchEmail(user.getEmail()).isPresent()) {
//             //     logger.error("User already registered");
//             //     throw new ExceptionHandler.Conflict("User already registered!");
//             // }

//             // // Criptografa a senha antes de salvar
//             // user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

//             // Roles userRole = roleRepository.findById(2)
//             //         .orElseThrow(() -> new ExceptionHandler.NotFoundException("Role USER not found!"));

//             // user.getRoles().add(userRole);

//             return repository.save(requeHabits);
//         } catch (ExceptionHandler.Conflict e) {
//             throw e;
//         } catch (Exception e) {
//             Throwable rootCause = getRootCause(e);
//             logger.error("Error registering habits.", rootCause);
//             throw new ExceptionHandler.BadRequestException("Error registering habits. details: " + e.getMessage());
//         }

//     }

// }

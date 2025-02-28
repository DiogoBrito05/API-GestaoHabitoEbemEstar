package API_GestaHabitosEbemEstar.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import API_GestaHabitosEbemEstar.models.MonitoringHabits;
import API_GestaHabitosEbemEstar.models.Notifications;
import API_GestaHabitosEbemEstar.models.UsersModel;
import API_GestaHabitosEbemEstar.repository.MonitoringHabitsRepository;
import API_GestaHabitosEbemEstar.repository.NotificationsRepository;
import API_GestaHabitosEbemEstar.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class EmailTriggerService {

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private MonitoringHabitsRepository repositoryMonitoring;

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Autowired
    private EmailService emailService;

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

    // Essa anotção é usada para enviar todos os dias às 08:00 AM (horário de
    // Brasília)
    @Scheduled(cron = "0 0 8 * * *", zone = "America/Sao_Paulo")
    public void sendEmailToUsers() {
        try {
            List<UsersModel> users = usersRepository.findAll();

            List<MonitoringHabits> monitoramentosPendentes = repositoryMonitoring.findByStatus("PENDENTE");
            List<MonitoringHabits> monitoramentosEmAndamento = repositoryMonitoring.findByStatus("EM_ANDAMENTO");
            List<MonitoringHabits> monitoramentosConcluido = repositoryMonitoring.findByStatus( "CONCLUIDO");

            // Se houver hábitos pendentes, envia e-mails correspondentes
            if (!monitoramentosPendentes.isEmpty()) {
                sendEmail(users, "PENDENTE");
            }

            // Se houver hábitos em andamento, envia e-mails correspondentes
            if (!monitoramentosEmAndamento.isEmpty()) {
                sendEmail(users, "EM_ANDAMENTO");
            }

            // Se houver hábitos concluídos, envia e-mails correspondentes
            if (!monitoramentosConcluido.isEmpty()) {
                sendEmail(users, "CONCLUIDO");
            }

            logger.info("Emails successfully sent to all users.");
        } catch (Exception e) {
            logger.error("Error sending emails: " + e.getMessage(), e);
        }
    }

    // Método responsável por enviar e-mails para todos os usuários com base no
    // status do hábito
    private void sendEmail(List<UsersModel> users, String status) {
        for (UsersModel user : users) {
            Optional<Notifications> templateOpt = notificationsRepository.findByStandard(status);
            if (templateOpt.isPresent()) {
                Notifications template = templateOpt.get();// Se a notificação existir
                // Envia um e-mail para o usuário com a mensagem correspondente
                emailService.sendEmail(user.getEmail(), "Notificação Hábitos", template.getMesage());
            }
        }
    }

}

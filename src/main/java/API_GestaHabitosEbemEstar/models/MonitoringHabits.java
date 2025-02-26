package API_GestaHabitosEbemEstar.models;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "monitoramento_habitos")
public class MonitoringHabits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer idMonitoring;

    @Column(name = "USUARIO_ID")
    private Integer idUser;

    @Column(name = "HABITO_ID")
    private Integer idHabits;

    @Column(name = "DATA")
    private LocalDate date;

    @Column(name = " Status")
    private String status;

}

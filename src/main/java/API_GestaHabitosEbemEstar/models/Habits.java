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
@Table(name = "habitos")
public class Habits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer idHabits;

    @Column(name = "USUARIO_ID")
    private Integer idUser;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "FREQUENCIA")
    private Enum frequency;

    @Column(name = "META")
    private String goal;

    @Column(name = "DATA_INICIO")
    private LocalDate  startDate;

    @Column(name = "ATIVO")
    private Boolean active;

}

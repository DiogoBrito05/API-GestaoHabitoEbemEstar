package API_GestaHabitosEbemEstar.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "recompensas")
public class Rewards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer idRewards;

    @Column(name = "USUARIO_ID")
    private Integer idUser;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "DATA_CONQUISTA")
    private String dateConquest;

}

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
@Table(name = "notificacoes")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer idNotificarions;

    @Column(name = "USUARIO_ID")
    private Integer idUser;

    @Column(name = "MENSAGEM")
    private String mesage;

    @Column(name = "PADRAO_STATUS")
    private String standard;


}

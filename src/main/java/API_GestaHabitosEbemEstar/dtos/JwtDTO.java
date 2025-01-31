package API_GestaHabitosEbemEstar.dtos;

import lombok.Data;

@Data
public class JwtDTO {

    private String token;
    
     public JwtDTO(String token) {
        this.token = token;
       
    }
}

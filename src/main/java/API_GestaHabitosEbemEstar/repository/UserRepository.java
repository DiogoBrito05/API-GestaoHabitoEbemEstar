package API_GestaHabitosEbemEstar.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import API_GestaHabitosEbemEstar.models.UsersModel;

@Repository
public interface UserRepository extends JpaRepository<UsersModel, Integer> {

    @Query(value = """
            SELECT * FROM usuarios
            WHERE EMAIL = :email
            """, nativeQuery = true)
    Optional<UsersModel> findBySearchEmail(@Param("email")String email);

    Optional<UsersModel>findByidUser(Integer userId);

     /* UsersModel findByEmail(String username); */

    UsersModel findByEmail(String email);

}

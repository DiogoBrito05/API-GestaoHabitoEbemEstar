package API_GestaHabitosEbemEstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import API_GestaHabitosEbemEstar.models.RolesUser;

@Repository
public interface RolesUserRepository extends JpaRepository<RolesUser, Integer>{

}

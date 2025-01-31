package API_GestaHabitosEbemEstar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import API_GestaHabitosEbemEstar.models.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer>{

    Optional<Roles> findByidRoles(Integer idRole);

}

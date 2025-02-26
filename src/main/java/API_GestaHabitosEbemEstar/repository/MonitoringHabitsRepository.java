package API_GestaHabitosEbemEstar.repository;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import API_GestaHabitosEbemEstar.models.MonitoringHabits;

@Repository
public interface MonitoringHabitsRepository extends JpaRepository<MonitoringHabits, Integer> {

    Optional<MonitoringHabits> findById(Integer id);

    List<MonitoringHabits> findByIdUser(Integer idUser);

}

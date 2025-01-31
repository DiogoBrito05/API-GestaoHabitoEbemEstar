package API_GestaHabitosEbemEstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import API_GestaHabitosEbemEstar.models.MonitoringHabits;

@Repository
public interface MonitoringHabitsRepository extends JpaRepository<MonitoringHabits, Integer> {

}

package API_GestaHabitosEbemEstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import API_GestaHabitosEbemEstar.models.Habits;

@Repository
public interface HabitsRepository extends JpaRepository<Habits, Integer> {

}

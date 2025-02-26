package API_GestaHabitosEbemEstar.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import API_GestaHabitosEbemEstar.models.Habits;

@Repository
public interface HabitsRepository extends JpaRepository<Habits, Integer> {

    List<Habits> findAllByIdUser(Integer userId);

    boolean existsByCategoryId(Integer categoryId);


}

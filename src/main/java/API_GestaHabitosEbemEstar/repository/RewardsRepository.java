package API_GestaHabitosEbemEstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import API_GestaHabitosEbemEstar.models.Rewards;

@Repository
public interface RewardsRepository extends JpaRepository<Rewards, Integer>{

    List<Rewards> findAllByIdUser(Integer userID);

}

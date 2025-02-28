package API_GestaHabitosEbemEstar.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import API_GestaHabitosEbemEstar.models.Notifications;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {

    Optional<Notifications> findByStandard(String status);

}

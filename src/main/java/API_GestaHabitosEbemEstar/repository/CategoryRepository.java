package API_GestaHabitosEbemEstar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import API_GestaHabitosEbemEstar.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByidCategory(Integer IdCategory);

    List<Category> findAllByUserId(Integer userID);

}

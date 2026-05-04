package mara.spichiger.todoprojekt.repository;

import mara.spichiger.todoprojekt.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
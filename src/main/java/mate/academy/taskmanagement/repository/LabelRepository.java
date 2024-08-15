package mate.academy.taskmanagement.repository;

import mate.academy.taskmanagement.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
}

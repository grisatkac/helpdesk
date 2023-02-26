package by.tms.tkach.helpdesk.repositories;

import by.tms.tkach.helpdesk.entities.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractRepository <T extends AbstractEntity> extends JpaRepository<T, Long> {
}

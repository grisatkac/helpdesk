package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.entities.AbstractEntity;

import java.util.List;
import java.util.Optional;

public interface CrudService<T extends AbstractEntity> {

    T create(T entity);

    Optional<T> findById(Long id);

    List<T> getAll();

    T update(T entity);

    T delete(Long id);
}

package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.entities.AbstractEntity;

import java.util.List;

public interface CrudService<T extends AbstractEntity> {

    T create(T entity);

    T findById(Long id);

    List<T> getAll();

    T update(T entity);

    T delete(Long id);
}

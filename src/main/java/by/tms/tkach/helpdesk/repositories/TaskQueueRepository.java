package by.tms.tkach.helpdesk.repositories;


import by.tms.tkach.helpdesk.entities.TaskQueue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskQueueRepository extends AbstractRepository<TaskQueue> {

    TaskQueue findByName(String name);

    @Query("SELECT taskQueue FROM TaskQueue taskQueue WHERE taskQueue.id = :id")
    TaskQueue findTaskQueueDetails(@Param("id") Long id);

    @Query("SELECT taskQueue.id, taskQueue.name FROM TaskQueue taskQueue WHERE taskQueue.id = :id")
    TaskQueue findShortTaskQueue(@Param("id") Long id);

    @Query("SELECT name FROM TaskQueue ")
    List<String> findAllNames();
}

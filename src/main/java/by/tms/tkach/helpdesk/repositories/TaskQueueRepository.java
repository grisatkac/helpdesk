package by.tms.tkach.helpdesk.repositories;

import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskQueueRepository extends AbstractRepository<TaskQueue> {

    Optional<TaskQueue> findByName(String name);

    @Query("SELECT taskQueue FROM TaskQueue taskQueue LEFT JOIN FETCH taskQueue.department WHERE taskQueue.id = :id")
    TaskQueue findTaskQueueDetails(@Param("id") Long id);

    @Query("SELECT taskQueue.id, taskQueue.name FROM TaskQueue taskQueue WHERE taskQueue.id = :id")
    TaskQueue findShortTaskQueue(@Param("id") Long id);

    @Query("SELECT name FROM TaskQueue ")
    List<String> findAllNames();

    //@Query("SELECT taskQueue FROM TaskQueue taskQueue LEFT JOIN User user WHERE user.id = :userId")
    //Page<TaskQueue> findAllUserQueues(@Param("userId") Long id, Pageable pageable);

    Page<TaskQueue> findTaskQueuesByUsersIn(List<User> users, Pageable pageable);
    //List<TaskQueue> findAllByUsersIn(List<User> users); /// MAY BE SET IN USER REPOSITORY?
}

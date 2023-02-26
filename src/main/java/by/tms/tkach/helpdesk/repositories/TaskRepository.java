package by.tms.tkach.helpdesk.repositories;

import by.tms.tkach.helpdesk.dto.task.response.TaskDetailsResponseDTO;
import by.tms.tkach.helpdesk.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends AbstractRepository<Task> {

    //@Query("SELECT task FROM Task task LEFT JOIN TaskQueue taskqueue ON task.taskQueue.id = :id JOIN User user ON task.ownerUser.id = user.id")
    /*@Query(value = "SELECT * FROM tasks LEFT JOIN system_users ON tasks.owner_id = system_users.id LEFT JOIN task_queues ON tasks.queue_id = task_queues.id WHERE tasks.id = ?1"
    ,nativeQuery = true)*/
    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.ownerUser u LEFT JOIN FETCH t.executorUser e WHERE t.id = :id")
    Task findTaskDetails(@Param("id") Long id);

    @Query("SELECT t, q FROM Task t LEFT JOIN FETCH t.taskQueue q  WHERE t.id = :id")
    Task findTaskWithQueue(@Param("id") Long id);

    @Query(value = "SELECT t FROM Task t LEFT JOIN t.ownerUser o LEFT JOIN t.executorUser e WHERE t.ownerUser.id = :id",
            countQuery = "SELECT COUNT (t) FROM Task t")
    Page<Task> findAllAssignableTasks(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT t FROM Task t LEFT JOIN t.ownerUser u LEFT JOIN t.executorUser e WHERE t.executorUser.id = :id",
            countQuery = "SELECT COUNT (t) FROM Task t")
    Page<Task> findAllExecutableTasks(@Param("id") Long id, Pageable pageable);


}

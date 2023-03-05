package by.tms.tkach.helpdesk.repositories;

import by.tms.tkach.helpdesk.entities.Task;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.entities.enums.task.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends AbstractRepository<Task> {

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.ownerUser u LEFT JOIN FETCH t.executorUser e WHERE t.id = :id")
    Task findTaskDetails(@Param("id") Long id);

    @Query("SELECT t, q FROM Task t LEFT JOIN FETCH t.taskQueue q  WHERE t.id = :id")
    Optional<Task> findTaskWithQueue(@Param("id") Long id);

    @Query(value = "SELECT t FROM Task t LEFT JOIN t.ownerUser o LEFT JOIN t.executorUser e WHERE t.ownerUser.id = :id",
            countQuery = "SELECT COUNT (t) FROM Task t")
    Page<Task> findAllAssignableTasks(@Param("id") Long id, Pageable pageable);

    @Query("SELECT COUNT(task) FROM Task task WHERE task.executorUser.id = :userId AND task.status = :status")
    Long findCountOfExecutableTasksByStatus(@Param("userId") Long userId, @Param("status") Status status);


    Page<Task> findAllByOwnerUserId(Long id, Pageable pageable);
    Page<Task> findAllByExecutorUserId(Long id, Pageable pageable);
    //@Query("SELECT task FROM Task task LEFT JOIN User user LEFT JOIN TaskQueue queue WHERE task.status = 'PROCESSING' AND queue.users  ")
    //Page<Task> findAllAvailableToComplete();

    Page<Task> findByStatusAndAndExecutorUserAndTaskQueueIn(Status status, User user, List<TaskQueue> queues, Pageable pageable);

    /*@Query(value = "SELECT t FROM Task t LEFT JOIN t.ownerUser u LEFT JOIN t.executorUser e WHERE t.executorUser IS EMPTY ",
            countQuery = "SELECT COUNT (t) FROM Task t")
    Page<Task> findAllExecutableTasks(@Param("id") Long id, Pageable pageable);

    @Query("SELECT task FROM Task task WHERE task.taskQueue.name = :id")
    Task getTaskByDepartmentName(@Param("id") Long id);*/
}

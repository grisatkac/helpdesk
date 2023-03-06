package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.Task;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.entities.enums.task.Status;
import by.tms.tkach.helpdesk.entities.enums.task.Urgency;
import by.tms.tkach.helpdesk.mappers.TaskMapper;
import by.tms.tkach.helpdesk.repositories.TaskRepository;
import by.tms.tkach.helpdesk.repositories.UserRepository;
import by.tms.tkach.helpdesk.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskQueueService taskQueueService;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;

    @BeforeEach
    public void init() {
        task = Task.builder()
                .id(1L)
                .name("test")
                .description("test")
                .createdDate(LocalDate.now())
                .status(Status.PROCESSING)
                .urgency(Urgency.LOW)
                .ownerUser(null)
                .executorUser(null)
                .taskQueue(TaskQueue.builder()
                        .id(1L)
                        .name("test")
                        .description("test")
                        .department(Department.builder()
                                .id(1L)
                                .name("test")
                                .description("test")
                                .build())
                        .build())
                .build();
    }

    @Test
    public void findById() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Optional<Task> actual = taskService.findById(task.getId());

        assertEquals(task, actual.get());
    }
}

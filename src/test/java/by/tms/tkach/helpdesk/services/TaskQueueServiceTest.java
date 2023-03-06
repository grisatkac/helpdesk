package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.dto.department.response.DepartmentShortResponseDTO;
import by.tms.tkach.helpdesk.dto.taskQueue.response.TaskQueueDetailsResponse;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.TaskQueue;
import by.tms.tkach.helpdesk.exception.CreateError;
import by.tms.tkach.helpdesk.mappers.TaskQueueMapper;
import by.tms.tkach.helpdesk.repositories.DepartmentRepository;
import by.tms.tkach.helpdesk.repositories.TaskQueueRepository;
import by.tms.tkach.helpdesk.services.impl.TaskQueueServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({MockitoExtension.class})
public class TaskQueueServiceTest {

    @Mock
    private TaskQueueRepository taskQueueRepository;

    @Mock
    private TaskQueueMapper taskQueueMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private TaskQueueServiceImpl taskQueueService;

    private TaskQueue taskQueue;
    private TaskQueueDetailsResponse taskQueueDetails;


    @BeforeEach
    void init() {
        taskQueue = TaskQueue.builder()
                .id(1L)
                .name("Test queue")
                .description("Test description")
                .department(Department.builder()
                        .id(1L)
                        .name("test")
                        .description("test")
                        .build())
                .build();
        taskQueueDetails = TaskQueueDetailsResponse.builder()
                .id(1L)
                .name("Test queue")
                .description("Test description")
                .department(DepartmentShortResponseDTO.builder()
                        .id(1L)
                        .name("Test queue")
                        .build())
                .build();
    }

    @Test
    void createTest() {
        when(taskQueueRepository.save(taskQueue)).thenReturn(taskQueue);

        TaskQueue actual = taskQueueService.create(taskQueue);

        assertEquals(taskQueue, actual);
    }

    @Test
    void findById() {
        when(taskQueueRepository.findById(taskQueue.getId())).thenReturn(Optional.of(taskQueue));

        Optional<TaskQueue> actual = taskQueueService.findById(taskQueue.getId());

        assertEquals(taskQueue, actual.get());
    }

    @Test
    void getAllTest() {
        List<TaskQueue> list = List.of(taskQueue);
        when(taskQueueRepository.findAll()).thenReturn(list);

        List<TaskQueue> actual = taskQueueService.getAll();

        assertEquals(list, actual);
    }

    @Test
    void getByNameTest() {
        when(taskQueueRepository.findByName(taskQueue.getName())).thenReturn(Optional.of(taskQueue));

        Optional<TaskQueue> actual = taskQueueService.getByName(taskQueue.getName());

        assertEquals(taskQueue, actual.get());

    }

    @Test
    void getTaskQueueDetails() {
        when(taskQueueRepository.findTaskQueueDetails(taskQueue.getId())).thenReturn(taskQueue);
        when(taskQueueMapper.toTaskDetails(taskQueue)).thenReturn(taskQueueDetails);

        TaskQueueDetailsResponse actual = taskQueueService.getTaskQueueDetails(taskQueue.getId());

        assertEquals(taskQueueDetails, actual);
    }

    @Test
    void getAllNames() {
        List<String> namesList = List.of(taskQueue.getName());
        when(taskQueueRepository.findAllNames()).thenReturn(namesList);

        List<String> actual = taskQueueService.findAllNames();

        assertEquals(namesList, actual);
    }











}

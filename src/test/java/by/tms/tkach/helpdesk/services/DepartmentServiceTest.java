package by.tms.tkach.helpdesk.services;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentDetailsResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.mappers.DepartmentMapper;
import by.tms.tkach.helpdesk.repositories.DepartmentRepository;
import by.tms.tkach.helpdesk.services.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;


    private Department department;
    private DepartmentUpdateDTO departmentUpdateDTO;
    private DepartmentDetailsResponseDTO departmentDetailsResponseDTO;


    @BeforeEach void init() {
        department = Department.builder()
                .id(1L)
                .name("Name test")
                .description("Description test")
                .head(null)
                .build();

        departmentUpdateDTO = DepartmentUpdateDTO.builder()
                .id(1L)
                .name("Name test")
                .description("Description test")
                .headLogin(null)
                .build();

        departmentDetailsResponseDTO = new DepartmentDetailsResponseDTO();
        departmentDetailsResponseDTO.setId(1L);
        departmentDetailsResponseDTO.setName("Name test");
        departmentDetailsResponseDTO.setDescription("Description test");
        departmentDetailsResponseDTO.setHead(null);
    }


    @Test
    void createTest() {
        when(departmentRepository.save(department)).thenReturn(department);

        Department actual = departmentService.create(department);

        assertEquals(department, actual);
    }

    @Test
    void getByNameTest() {
        when(departmentRepository.findByName(department.getName())).thenReturn(Optional.of(department));

        Optional<Department> byName = departmentService.getByName(department.getName());

        assertEquals(department, byName.get());
    }

    @Test
    void getForUpdateTest() {
        when(departmentRepository.findDepartmentToUpdate(department.getId())).thenReturn(Optional.of(department));
        when(departmentMapper.toDepartmentUpdate(department)).thenReturn(departmentUpdateDTO);

        DepartmentUpdateDTO actual = departmentService.getForUpdate(department.getId());

        assertEquals(departmentUpdateDTO, actual);
    }

    @Test
    void getDepartmentDetails() {
        when(departmentRepository.findDepartmentDetails(department.getId())).thenReturn(Optional.of(department));
        when(departmentMapper.toDepartmentDetails(department)).thenReturn(departmentDetailsResponseDTO);

        DepartmentDetailsResponseDTO actual = departmentService.getDepartmentDetails(department.getId());

        assertEquals(departmentDetailsResponseDTO, actual);
    }
}

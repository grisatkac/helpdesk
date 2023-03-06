package by.tms.tkach.helpdesk.controllers.rest;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.request.DepartmentCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentDetailsResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.mappers.DepartmentMapper;
import by.tms.tkach.helpdesk.services.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/departments")
public class DepartmentRestController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @PostMapping("/create")
    public ResponseEntity<Department> create(@Valid @RequestBody DepartmentCreateRequestDTO departmentCreateRequestDTO) {
        Department createDepartment = departmentService.create(departmentMapper.toDepartment(departmentCreateRequestDTO));
        return new ResponseEntity<>(createDepartment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDetailsResponseDTO> getById(@PathVariable Long id) {
        DepartmentDetailsResponseDTO departmentDetails = departmentService.getDepartmentDetails(id);
        return new ResponseEntity<>(departmentDetails, HttpStatus.FOUND);
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<DepartmentUpdateDTO> getToUpdate(@PathVariable Long id) {
        DepartmentUpdateDTO forUpdate = departmentService.getForUpdate(id);
        return new ResponseEntity<>(forUpdate, HttpStatus.FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Department> update(@PathVariable Long id, @RequestBody DepartmentUpdateDTO departmentUpdate) {
        Department updatedDepartment = departmentService.update(departmentMapper.toDepartment(departmentUpdate));
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Department> delete(@PathVariable Long id) {
        Department deletedDepartment = departmentService.delete(id);
        return new ResponseEntity<>(deletedDepartment, HttpStatus.OK);
    }
}

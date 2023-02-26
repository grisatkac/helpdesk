package by.tms.tkach.helpdesk.services.impl;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentPagingResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentShortResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.exception.EntityNotFound;
import by.tms.tkach.helpdesk.mappers.DepartmentMapper;
import by.tms.tkach.helpdesk.repositories.DepartmentRepository;
import by.tms.tkach.helpdesk.services.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public Department create(Department entity) {
        return departmentRepository.save(entity);
    }

    @Override
    public Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new EntityNotFound(String.format("Can't find user with id %s", id)));
    }

    @Override
    public Department getByHeadId(Long id) {
        return departmentRepository.findByHeadId(id);
    }

    @Override
    public Department getByName(String name) {
        return departmentRepository.findByName(name);
    }

    @Transactional
    @Override
    public DepartmentUpdateDTO getForUpdate(Long id) {
        Department department = findById(id); // throw not found exception

        DepartmentUpdateDTO departmentToUpdate = departmentMapper.toDepartmentUpdate(department);

        User headOfDepartment = department.getHead();

        if (headOfDepartment != null) {
            departmentToUpdate.setHeadLogin(department.getHead().getLogin());
        }

        return departmentToUpdate;
    }

    @Override
    public DepartmentDetailsResponseDTO getDepartmentDetails(Long id) {
        Department foundDepartment = departmentRepository.findDepartmentDetails(id);
        return departmentMapper.toDepartmentDetails(foundDepartment);
    }

    @Override
    public DepartmentShortResponseDTO getShortDepartment(Long id) {
        return departmentMapper.toShortDepartment(departmentRepository.findShortDepartment(id));
    }

    @Override
    public DepartmentPagingResponseDTO getAllPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Page<Department> page = getPage(pageNumber, pageSize, sortBy, sortDir);
        return DepartmentPagingResponseDTO.builder()
                .currentPage(pageNumber)
                .totalPages(page.getTotalPages())
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .reverseSortDir(sortDir.equals("asc")? "desc" : "asc")
                .items(departmentMapper.toDepartmentDetailsList(page.getContent()))
                .build();
    }

    @Override
    public DepartmentUpdateDTO updateDepartment(Long id, DepartmentUpdateDTO department) {
        departmentRepository.save(departmentMapper.toDepartment(department));
        return department;
    }

    private Page<Department> getPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        return departmentRepository.findAll(paging);
    }

    @Override
    public boolean isExist(String name) {
        return departmentRepository.findByName(name) != null;
    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department update(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department delete(Long id) {
        return null;
    }


}

package by.tms.tkach.helpdesk.services.impl;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentDetailsResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentPagingResponseDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentShortResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.User;
import by.tms.tkach.helpdesk.exception.CreateError;
import by.tms.tkach.helpdesk.exception.NotFoundException;
import by.tms.tkach.helpdesk.exception.UpdateError;
import by.tms.tkach.helpdesk.mappers.DepartmentMapper;
import by.tms.tkach.helpdesk.repositories.DepartmentRepository;
import by.tms.tkach.helpdesk.repositories.UserRepository;
import by.tms.tkach.helpdesk.services.DepartmentService;
import by.tms.tkach.helpdesk.services.validators.DepartmentValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final UserRepository userRepository;

    @Override
    public Department create(Department department) {
        if (!DepartmentValidator.isValidToCreate) {
            String errorMessage = validateDepartmentNameToCreate(department.getName());
            if (!errorMessage.isEmpty()) {
                throw new CreateError(errorMessage);
            }
        }

        return departmentRepository.save(department);
    }

    @Override
    public String validateDepartmentNameToCreate(String name) {
        String errorMessage = "";

        if (getByName(name).isPresent()) {
            errorMessage = "Department name must be unique";
        }

        return errorMessage;
    }

    @Override
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Transactional
    @Override
    public DepartmentUpdateDTO getForUpdate(Long id) {
        Department foundDepartment = departmentRepository.findDepartmentToUpdate(id)
                .orElseThrow(() -> new NotFoundException("Can't find department"));

        return departmentMapper.toDepartmentUpdate(foundDepartment);
    }

    @Transactional
    @Override
    public Department update(Department department) {

        if (department == null) {
            throw new UpdateError("Department can't be null");
        }

        if (!DepartmentValidator.isIsValidToUpdate) {
            String departmentNameValidationErrorMessage = validateNameToUpdate(department);

            if (!departmentNameValidationErrorMessage.isEmpty()) {
                throw new UpdateError(departmentNameValidationErrorMessage);
            }

            if (department.getHead() == null || department.getHead().getLogin().isEmpty()) {
                department.setHead(null);
            } else {
                String headLoginValidationErrorMessage = validateHeadLoginToUpdate(department);

                if (!headLoginValidationErrorMessage.isEmpty()) {
                    throw new UpdateError(headLoginValidationErrorMessage);
                }
            }
        }

        Optional<User> userByLogin = userRepository.getByLogin(department.getHead().getLogin());
        Department departmentToUpdate = departmentRepository.findById(department.getId()).get();

        if (userByLogin.isPresent()) {
            departmentToUpdate.setHead(userByLogin.get());
        } else {
            departmentToUpdate.setHead(null);
        }

        departmentToUpdate.setName(department.getName());
        departmentToUpdate.setDescription(department.getDescription());

        return departmentRepository.save(departmentToUpdate);
    }

    @Override
    public String validateNameToUpdate(Department department) {

        String errorMessage = "";

        Optional<Department> departmentByName = getByName(department.getName());

        if (departmentByName.isPresent() && !Objects.equals(departmentByName.get().getId(), department.getId())) {
            return "You can't update department name because department name must be unique and already exist";
        }

        return errorMessage;
    }

    @Transactional
    public String validateHeadLoginToUpdate(Department department) {
        String errorMessage = "";
        if (!department.getHead().getLogin().isEmpty()) {

            Optional<User> userByLogin = userRepository.getByLogin(department.getHead().getLogin());

            if (userByLogin.isEmpty() && department.getHead().getLogin() != null) {
                return "Incorrect user login: user with login not exist";
            }

            Department departmentById = findById(department.getId()).orElseThrow(() -> new UpdateError("Department id is incorrect"));

            userByLogin.get().getDepartment();

            if (!userByLogin.get().getDepartment().getId().equals(departmentById.getId())) {
                return "This user is head of another department: not empty queue";
            }

            Department departmentByHeadLogin = getByHeadLogin(department.getHead().getLogin());

            if (departmentByHeadLogin != null && !departmentById.equals(departmentByHeadLogin)) {
                return "Incorrect user login: this user are head of another department";
            }
        }

        return errorMessage;
    }

    @Override
    public Department getByHeadLogin(String login) {
        return departmentRepository.findByHeadLogin(login);
    }


    @Override
    public Department getByHeadId(Long id) {
        return departmentRepository.findByHeadId(id);
    }

    @Override
    public Optional<Department> getByName(String name) {
        return departmentRepository.findByName(name);
    }

    @Override
    public DepartmentDetailsResponseDTO getDepartmentDetails(Long id) {
        Department foundDepartment = departmentRepository.findDepartmentDetails(id).orElseThrow(() -> new NotFoundException("Department not exist"));
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

    private Page<Department> getPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        return departmentRepository.findAll(paging);
    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Transactional
    @Override
    public Department delete(Long id) {
        Department department = findById(id).orElseThrow(() -> new NotFoundException("Can't delete: invalid id"));
        if (department.getQueues().size() != 0 || department.getUsers().size() != 0) {
            throw new UpdateError("Can't delete: department not empty");
        }
        departmentRepository.delete(department);
        return department;
    }
}

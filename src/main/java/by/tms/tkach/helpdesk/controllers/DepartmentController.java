package by.tms.tkach.helpdesk.controllers;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.request.DepartmentCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentPagingResponseDTO;
import by.tms.tkach.helpdesk.mappers.DepartmentMapper;
import by.tms.tkach.helpdesk.services.DepartmentService;
import by.tms.tkach.helpdesk.services.validators.DepartmentValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    private final DepartmentValidator departmentValidator;

    @ModelAttribute("departmentToUpdate")
    public DepartmentUpdateDTO getUserCreate() {
        return DepartmentUpdateDTO.builder().build();
    }

    @ModelAttribute("department")
    public DepartmentCreateRequestDTO getDepartmentCreateRequest() {
        return DepartmentCreateRequestDTO.builder().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String getCreateDepartmentPage(@ModelAttribute("department") DepartmentCreateRequestDTO department) {
        return "department/departmentCreate";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("department") DepartmentCreateRequestDTO department, BindingResult bindingResult, Model model) {

        departmentValidator.validateDepartmentNameToCreate(department.getName(), bindingResult);

        if(bindingResult.hasErrors()) {
            model.addAttribute("department", department);
            return "department/departmentCreate";
        }

        departmentService.create(departmentMapper.toDepartment(department));

        return "redirect:/departments/all";
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department details data")
    public String getDepartmentDetailsPageById(@PathVariable Long id, Model model) {
        model.addAttribute("department", departmentService.getDepartmentDetails(id));
        return "department/departmentInfo";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable Long id, @ModelAttribute("departmentToUpdate") DepartmentUpdateDTO departmentUpdateDTO, Model model) {
        model.addAttribute("departmentToUpdate", departmentService.getForUpdate(id));
        return "department/departmentUpdate";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("departmentToUpdate") DepartmentUpdateDTO departmentToUpdate, BindingResult bindingResult, Model model) {
        departmentValidator.validateDepartmentToUpdate(departmentMapper.toDepartment(departmentToUpdate), bindingResult);

        if(bindingResult.hasErrors()) {
            model.addAttribute("departmentToUpdate", departmentToUpdate);
            return "department/departmentUpdate";
        }

        departmentService.update(departmentMapper.toDepartment(departmentToUpdate));
        return "redirect:/departments/all";
    }

    @GetMapping("/all")
    public String findAll(Model model,
                          @RequestParam(defaultValue = "0") Integer pageNumber,
                          @RequestParam(defaultValue = "5") Integer pageSize,
                          @RequestParam(defaultValue = "name") String sortBy,
                          @RequestParam(defaultValue = "asc") String sortDir)
    {
        DepartmentPagingResponseDTO response = departmentService.getAllPageable(pageNumber, pageSize, sortBy, sortDir);
        model.addAttribute("page", response);
        return "department/departments";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        departmentService.delete(id);
        return "redirect:/departments/all";
    }
}

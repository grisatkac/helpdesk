package by.tms.tkach.helpdesk.controllers;

import by.tms.tkach.helpdesk.dto.department.DepartmentUpdateDTO;
import by.tms.tkach.helpdesk.dto.department.request.DepartmentCreateRequestDTO;
import by.tms.tkach.helpdesk.dto.department.response.DepartmentPagingResponseDTO;
import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.services.DepartmentService;
import by.tms.tkach.helpdesk.services.validators.DepartmentValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/departments")
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentValidator departmentValidator;

    @ModelAttribute("departmentToUpdate")
    public DepartmentUpdateDTO getUserCreate() {
        return new DepartmentUpdateDTO();
    }

    @ModelAttribute("department")
    public DepartmentCreateRequestDTO getDepartmentCreateRequest() {
        return new DepartmentCreateRequestDTO();
    }

    @GetMapping("/create")
    public String getCreateDepartmentPage() {
        return "department/departmentCreate";
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department details data")
    public String getDepartmentDetailsPageById(@PathVariable Long id, Model model) {
        model.addAttribute("department", departmentService.getDepartmentDetails(id));
        return "department/departmentInfo";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable Long id, Model model) {
        model.addAttribute("departmentToUpdate", departmentService.getForUpdate(id));
        return "department/departmentUpdate";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute DepartmentCreateRequestDTO department, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("department", department);
            return "department/departmentCreate";
        }

        Department departmentToCreate = departmentValidator.validateAndPrepareDepartmentForCreate(department, bindingResult);

        if(bindingResult.hasErrors()) {
            model.addAttribute("department", department);
            return "department/departmentCreate";
        } else {
            departmentService.create(departmentToCreate);
            return "redirect:/departments/all";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute DepartmentUpdateDTO departmentToUpdate, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("departmentToUpdate", departmentService.getForUpdate(id));
            return "department/departmentCreate";
        }

        Department department = departmentValidator.validateAndPrepareDepartmentForUpdate(departmentToUpdate, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("departmentToUpdate", departmentService.getForUpdate(id));
            return "department/departmentUpdate";
        }

        departmentService.update(department);
        model.addAttribute("departmentToUpdate", departmentToUpdate);
        return "department/departmentUpdate";
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

}

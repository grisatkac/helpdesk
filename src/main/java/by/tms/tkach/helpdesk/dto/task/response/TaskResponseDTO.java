package by.tms.tkach.helpdesk.dto.task.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate createdDate;
    private String urgency;
    private String status;
}

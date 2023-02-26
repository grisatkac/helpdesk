package by.tms.tkach.helpdesk.entities;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Department} entity
 */
@Data
public class DepartmentDto implements Serializable {
    private final Long id;
    private final String description;
}
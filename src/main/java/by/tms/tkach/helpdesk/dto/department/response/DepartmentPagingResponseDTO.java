package by.tms.tkach.helpdesk.dto.department.response;

import by.tms.tkach.helpdesk.dto.department.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DepartmentPagingResponseDTO extends PagingDTO<DepartmentDetailsResponseDTO> {
}

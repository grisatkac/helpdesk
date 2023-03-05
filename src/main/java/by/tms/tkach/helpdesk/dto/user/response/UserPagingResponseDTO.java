package by.tms.tkach.helpdesk.dto.user.response;

import by.tms.tkach.helpdesk.dto.department.PagingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserPagingResponseDTO extends PagingDTO<UserDetailsResponse> {
}

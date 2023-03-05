package by.tms.tkach.helpdesk.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserQueuesDTO {
    private Long id;
    private String queueName;
}

package by.tms.tkach.helpdesk.dto.form;

import by.tms.tkach.helpdesk.entities.enums.task.Urgency;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DefaultDataTaskInputFields {

    List<String> queueNamesList;
    List<Urgency> urgencyList;
}

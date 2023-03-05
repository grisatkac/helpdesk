package by.tms.tkach.helpdesk.entities;

import by.tms.tkach.helpdesk.entities.enums.task.Status;
import by.tms.tkach.helpdesk.entities.enums.task.Urgency;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task extends AbstractEntity {

    private String name;
    private String description;
    @CreationTimestamp
    private LocalDate createdDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Urgency urgency;

    @ManyToOne(cascade = /*CascadeType.MERGE*/ CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    @ToString.Exclude
    private User ownerUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "executor_id")
    @ToString.Exclude
    private User executorUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "queue_id")
    @ToString.Exclude
    private TaskQueue taskQueue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Task task = (Task) o;
        return id != null && Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package by.tms.tkach.helpdesk.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_queues")
public class TaskQueue extends AbstractEntity {

    String name;
    String description;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    Department department;*/
    @ManyToOne
    @JoinColumn(name = "department_id")
    Department department;

    @ManyToMany(mappedBy = "queues")
    @ToString.Exclude
    List<User> users;

    @OneToMany(mappedBy = "taskQueue")
    @ToString.Exclude
    List<Task> tasks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TaskQueue taskQueue = (TaskQueue) o;
        return id != null && Objects.equals(id, taskQueue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

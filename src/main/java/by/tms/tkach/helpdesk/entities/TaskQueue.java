package by.tms.tkach.helpdesk.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;

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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    Department department;

    /*@ManyToMany(mappedBy = "queues", cascade = {CascadeType.MERGE, CascadeType.MERGE, CascadeType.PERSIST})
    @ToString.Exclude
            @Cascade(org.hibernate.annotations.CascadeType.REMOVE)*/
    @ManyToMany(mappedBy = "queues",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    List<User> users;

    @OneToMany(mappedBy = "taskQueue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

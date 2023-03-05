package by.tms.tkach.helpdesk.entities;

import by.tms.tkach.helpdesk.entities.enums.task.Roles;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

//@Getter
//@Setter
//@ToString
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "system_users")
public class User extends AbstractEntity{

    String firstName;
    String lastName;
    String email;
    String login;
    String password;

    /*@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    Role role;*/
    @Enumerated(EnumType.STRING)
    Roles role;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    Department department;

    @OneToMany(mappedBy = "ownerUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Task> assignedTasks;

    @OneToMany(mappedBy = "executorUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Task> executableTasks;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_queues",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "queue_id")
    )
    @ToString.Exclude
    List<TaskQueue> queues;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public User addQueue(TaskQueue queue) {
        this.queues.add(queue);
        return this;
    }
}

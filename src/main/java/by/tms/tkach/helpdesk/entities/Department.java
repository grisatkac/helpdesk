package by.tms.tkach.helpdesk.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Entity
@Table(name = "departments")
public class Department extends AbstractEntity{

    String name;
    String description;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    //@MapsId
    @JoinColumn(name = "head_id")
    User head;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<TaskQueue> queues;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Department that = (Department) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

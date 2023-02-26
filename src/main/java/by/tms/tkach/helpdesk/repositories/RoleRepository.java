package by.tms.tkach.helpdesk.repositories;

import by.tms.tkach.helpdesk.entities.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends AbstractRepository<Role> {
    Role findByName(String name);
}

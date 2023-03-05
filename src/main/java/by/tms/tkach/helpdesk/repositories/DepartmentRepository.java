package by.tms.tkach.helpdesk.repositories;

import by.tms.tkach.helpdesk.entities.Department;
import by.tms.tkach.helpdesk.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends AbstractRepository<Department> {

    @Query("SELECT department FROM Department department WHERE department.head.id = :headId")
    Department findByHeadId(@Param("headId") Long id);

    @Query("SELECT department FROM Department department LEFT JOIN department.head WHERE department.head.login = :headLogin")
    Department findByHeadLogin(@Param("headLogin") String headLogin);


    //List<Department> findByUsersIs(List<User> users);

    Optional<Department> findByName(String name);

    @Query("SELECT department.id, department.name FROM Department department WHERE department.id = :id")
    Department findShortDepartment(@Param("id") Long id);

    @Query("SELECT department FROM Department department LEFT JOIN department.head h WHERE department.id = :id")
    Optional<Department> findDepartmentDetails(@Param("id") Long id);

    @Query("SELECT department FROM Department department LEFT JOIN FETCH department.head h WHERE department.id = :id")
    Optional<Department> findDepartmentToUpdate(@Param("id") Long id);
}

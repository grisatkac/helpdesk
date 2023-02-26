package by.tms.tkach.helpdesk.repositories;

import by.tms.tkach.helpdesk.entities.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends AbstractRepository<Department> {

    @Query("SELECT department FROM Department department WHERE department.head.id = :headId")
    Department findByHeadId(@Param("headId") Long id);

    Department findByName(String name);

    @Query("SELECT department.id, department.name FROM Department department WHERE department.id = :id")
    Department findShortDepartment(@Param("id") Long id);

    @Query("SELECT department FROM Department department LEFT JOIN department.head h WHERE department.id = :id")
    Department findDepartmentDetails(@Param("id") Long id);

    //"SELECT department.id, department.name, department.description, (department.head.id, department.head.firstName, department.head.lastName) AS department FROM Department department LEFT JOIN department.head h WHERE department.id = :id"
}

package by.tms.tkach.helpdesk.repositories;

import by.tms.tkach.helpdesk.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AbstractRepository<User> {

    User getByLogin(String login);

    User getByEmail(String email);

    User findByLoginAndPassword(String login, String password);

    @Query("SELECT user.id, user.firstName, user.lastName FROM User user WHERE user.id = :id")
    User getShortUser(@Param("id") Long id);

    @Query("SELECT user FROM User user LEFT JOIN user.department d WHERE user.id = :id")
    User getUserDetails(@Param("id") Long id);
}

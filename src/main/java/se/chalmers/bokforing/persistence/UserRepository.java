package se.chalmers.bokforing.persistence;

import se.chalmers.bokforing.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /** Finds all user with this name
     * @param name
     * @return  */
    List<User> findByName(String name);
    /** Finds the user with a certain name
     * @param email
     * @return  */
    User findByEmail(String email);
    //TODO Redundant to have a name & pass fetch.
    /** Finds a user with name and password
     * @param email
     * @param pass
     * @return  */
    User findByEmailAndPass(String email, String pass);
}
  


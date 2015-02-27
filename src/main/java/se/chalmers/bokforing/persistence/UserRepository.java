package se.chalmers.bokforing.persistence;

import se.chalmers.bokforing.model.UserAccount;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAccount,Long> {

    /** Finds all user with this name
     * @param name
     * @return  */
    List<UserAccount> findByName(String name);
    /** Finds the user with a certain name
     * @param email
     * @return  */
    UserAccount findByEmail(String email);
    //TODO Redundant to have a name & pass fetch.
    /** Finds a user with name and password
     * @param email
     * @param pass
     * @return  */
    UserAccount findByEmailAndPass(String email, String pass);
}
  


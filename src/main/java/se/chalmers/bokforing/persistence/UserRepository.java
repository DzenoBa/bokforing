package se.chalmers.bokforing.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends JpaRepository<UserEnt,Long> {

    /** Finds all user with this name
     * @param name
     * @return  */
    List<UserEnt> findByName(String name);
    /** Finds the user with a certain name
     * @param email
     * @return  */
    UserEnt findByEmail(String email);
    //TODO Redundant to have a name & pass fetch.
    /** Finds a user with name and password
     * @param email
     * @param pass
     * @return  */
    UserEnt findByEmailAndPass(String email, String pass);
}
  


package se.chalmers.bokforing.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends JpaRepository<UserEnt,Long> {

    /** Finds all user with this name */
    List<UserEnt> findByName(String name);
    /** Finds the user with a certain name */
    UserEnt findByEmail(String email);
    //TODO Redundant to have a name & pass fetch.
    /** Finds a user with name and password */
    UserEnt findByEmailAndPass(String email, String pass);
}
  


package se.chalmers.bokforing.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;
import se.chalmers.bokforing.model.user.UserAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAccount,Long> {

    /** Finds the user with a certain name
     * @param email
     * @return  */
    UserAccount findByEmail(String email);

    /** Finds a user with name and password
     * @param email
     * @param pass
     * @return  */
    UserAccount findByEmailAndPass(String email, String pass);
}
  


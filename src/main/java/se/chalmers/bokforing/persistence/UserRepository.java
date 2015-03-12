package se.chalmers.bokforing.persistence;

import se.chalmers.bokforing.model.UserAccount;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.UserGroup;

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
    
    @Modifying(clearAutomatically = true)
    @Query("update UserAccount u set u.name = ?1 where u.email = ?2")
    int updateName(String name, String email);
    
    @Modifying(clearAutomatically = true)
    @Query("update UserAccount u set u.pass = ?1 where u.email = ?2")
    int updatePass(String pass, String email);
    
    @Modifying(clearAutomatically = true)
    @Query("update UserAccount u set u.salt = ?1 where u.email = ?2")
    int updateSalt(String salt, String email);
    
    @Modifying(clearAutomatically = true)
    @Query("update UserAccount u set u.email = ?1 where u.id = ?2")
    int updateEmail(String newEmail, Long id);
    
    @Modifying(clearAutomatically = true)
    @Query("update UserAccount u set u.userGroup = ?1 where u.email = ?2")
    int updateGroup(UserGroup userGroup, String email);
    
    @Modifying(clearAutomatically = true)
    @Query("update UserAccount u set u.sessionid = ?1 where u.email = ?2")
    int updateSessionid(String sessionid, String email);
}
  


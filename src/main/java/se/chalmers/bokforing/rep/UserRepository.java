package se.chalmers.bokforing.rep;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.*;



@Repository
public interface UserRepository extends JpaRepository<UserEnt,Long> {

    //Get a single user
    UserEnt findById(Integer id);
    List<UserEnt> findByName(String name);
   /* List<UserEnt> findByEmail(String emailAddress);
    List<UserEnt> findByGroup2(String group);
    @Modifying
    @Query("update UserEnt u set u.date = ?1 where u.id = ?2")
    int setFixedDateFor(Date d, Integer id);
    */
}
  


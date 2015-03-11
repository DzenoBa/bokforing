
package se.chalmers.bokforing.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.Account;

/**
 *
 * @author Dženan
 */
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    
    Account findByNumber(int number);
    
    List<Account> findByNumberBetween(int first, int last);
       
}

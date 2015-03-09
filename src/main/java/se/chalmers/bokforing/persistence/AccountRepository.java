
package se.chalmers.bokforing.persistence;

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
       
}

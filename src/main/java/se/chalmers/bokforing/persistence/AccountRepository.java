
package se.chalmers.bokforing.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    Page<Account> findByNumberBetween(int first, int last, Pageable pageable);
    
    Page<Account> findByNameLike(String name, Pageable pageable);
       
}

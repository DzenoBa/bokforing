
package se.chalmers.bokforing.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.chalmers.bokforing.model.Account;

/**
 *
 * @author Dženan
 */
public interface AccountService {
    
    Page<Account> findAllAccounts(Pageable pageable);
    
    Page<Account> findAllAccounts(Integer pageNumber, String fieldToSortBy, Boolean ascendingSort);
    
    Account findAccountByNumber(int number);
}

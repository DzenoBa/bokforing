
package se.chalmers.bokforing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.persistence.AccountRepository;

/**
 *
 * @author Dženan
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repository;
    
    @Override
    public Page<Account> findAllAccounts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Account> findAllAccounts(Integer pageNumber, String fieldToSortBy, Boolean ascendingSort) {
        PageRequest request = PageRequestParser.getPageRequest(pageNumber, ascendingSort, fieldToSortBy);
        return repository.findAll(request);
    }

    @Override
    public Account findAccountByNumber(int number) {
        return repository.findByNumber(number);
    }
    
}

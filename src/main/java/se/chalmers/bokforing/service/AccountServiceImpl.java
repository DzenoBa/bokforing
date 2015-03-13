
package se.chalmers.bokforing.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.persistence.AccountRepository;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;

/**
 *
 * @author Dženan
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repository;
    
    @Override
    public Page<Account> findAllAccounts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Account> findAllAccounts(PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();
        return repository.findAll(request);
    }

    @Override
    public Account findAccountByNumber(int number) {
        return repository.findByNumber(number);
    }

    @Override
    public List<Account> findAccountBetween(int first, int last) {
        if (first < 0 || last < 0)
            return null;
        else if (first > last)
            return null;
        else
            return repository.findByNumberBetween(first, last);
    }
}


package se.chalmers.bokforing.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Page<Account> findAccountBetween(int first, int last, PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();
        
        if (first < 0 || last < 0)
            return null;
        else if (first > last)
            return null;
        else
            return repository.findByNumberBetween(first, last, request);
    }
    
    @Override
    public Page<Account> findByNumberLike(int number, PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();
        
        if(number > 999) {
            List<Account> ls = new ArrayList();
            ls.add(repository.findByNumber(number));
            Page<Account> pageLs = new PageImpl(ls);
            return pageLs;
        }
        int first;
        int last;
        if(number > 99) {
            first = number * 10;
            last = first + 9;
            return repository.findByNumberBetween(first, last, request);
        } else if(number >9) {
            first = number * 100;
            last = first + 99;
        } else {
            first = number * 1000;
            last = first + 999;
        }
        return repository.findByNumberBetween(first, last, request);
    }
    
    @Override
    public Page<Account> findByNameLike(String name, PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();
        String tmp = "%"+ name + "%";
        return repository.findByNameLike(tmp, request);
    }

    @Override
    public void save(Account account) {
        repository.save(account);
    }
}

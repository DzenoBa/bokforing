
package se.chalmers.bokforing.service;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<Account> findAccountBetween(int first, int last) {
        if (first < 0 || last < 0)
            return null;
        else if (first > last)
            return null;
        else
            return repository.findByNumberBetween(first, last);
    }
    
    @Override
    public List<Account> findByNumberLike(int number) {
        if(number > 999) {
            List<Account> ls = new ArrayList();
            ls.add(repository.findByNumber(number));
            return ls;
        }
        int first;
        int last;
        if(number > 99) {
            first = number * 10;
            last = first + 9;
            return repository.findByNumberBetween(first, last);
        } else if(number >9) {
            first = number * 100;
            last = first + 99;
        } else {
            first = number * 1000;
            last = first + 999;
        }
        return repository.findByNumberBetween(first, last);
    }
    
    @Override
    public List<Account> findByNameLike(String name) {
        String tmp = "%"+ name + "%";
        return repository.findByNameLike(tmp);
    }
}

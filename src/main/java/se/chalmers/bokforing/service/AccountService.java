package se.chalmers.bokforing.service;

import java.util.List;
import org.springframework.data.domain.Page;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;

/**
 *
 * @author Dženan
 */
public interface AccountService {

    List<Account> findAllAccounts();

    Page<Account> findAllAccounts(PagingAndSortingTerms terms);

    Account findAccountByNumber(int number);

    Page<Account> findAccountBetween(int first, int last, PagingAndSortingTerms terms);

    Page<Account> findByNumberLike(int number, PagingAndSortingTerms terms);

    Page<Account> findByNameLike(String name, PagingAndSortingTerms terms);

    void save(Account account);

    void delete(Account account);

}

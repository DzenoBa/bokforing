/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing;

import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.config.TestApplicationConfig;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.PostType;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.AccountManager;
import se.chalmers.bokforing.service.BalanceSheetPresenter;
import se.chalmers.bokforing.service.CustomerManager;
import se.chalmers.bokforing.service.IncomeStatementPresenter;
import se.chalmers.bokforing.service.PostManager;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.VerificationManager;
import se.chalmers.bokforing.service.VerificationService;

/**
 *
 * @author Isabelle
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class ReportTest extends AbstractIntegrationTest {

    @Autowired
    VerificationManager manager;

    @Autowired
    VerificationService service;

    @Autowired
    CustomerManager customerManager;

    @Autowired
    AccountManager accountManager;

    @Autowired
    UserService userService;

    @Autowired
    PostManager postManager;

    private UserAccount user;

    @Before
    public void setup() {
        user = userService.getUser("apa@test.com").getUA();
        user.getId();
    }

    @Transactional
    @Test
    public void printBalanceSheet() {
        double sum1Amount = 100.0;
        double sum2Amount = 100.0;

        double sum3Amount = 200.0;
        double sum4Amount = 200.0;

        double sum5Amount = 100.0;
        double sum6Amount = 100.0;

        Calendar cal = Calendar.getInstance();

        Account account = accountManager.createAccount(2018, "Egna insättningar");
        Account account2 = accountManager.createAccount(1055, "Saker");
        Account account3 = accountManager.createAccount(3055, "Fel");

        PostSum sum = new PostSum();
        sum.setSumTotal(sum1Amount);
        sum.setType(PostType.Credit);

        PostSum sum2 = new PostSum();
        sum2.setSumTotal(sum2Amount);
        sum2.setType(PostType.Debit);

        PostSum sum3 = new PostSum();
        sum3.setSumTotal(sum3Amount);
        sum3.setType(PostType.Debit);

        PostSum sum4 = new PostSum();
        sum4.setSumTotal(sum4Amount);
        sum4.setType(PostType.Credit);

        PostSum sum5 = new PostSum();
        sum5.setSumTotal(sum5Amount);
        sum5.setType(PostType.Debit);

        PostSum sum6 = new PostSum();
        sum6.setSumTotal(sum6Amount);
        sum6.setType(PostType.Credit);

        Customer customer = customerManager.createCustomer(user, 123, "Jakob", "03484838", null);

        Post post = postManager.createPost(sum, account);
        Post post2 = postManager.createPost(sum2, account2);
        Post post3 = postManager.createPost(sum3, account2);
        Post post4 = postManager.createPost(sum4, account);
        Post post5 = postManager.createPost(sum5, account);
        Post post6 = postManager.createPost(sum6, account);

        Post post7 = postManager.createPost(sum, account3);
        Post post8 = postManager.createPost(sum2, account3);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        postList.add(post2);

        ArrayList<Post> postList2 = new ArrayList<>();
        postList2.add(post3);
        postList2.add(post4);

        ArrayList<Post> postList3 = new ArrayList<>();
        postList3.add(post5);
        postList3.add(post6);

        ArrayList<Post> postList4 = new ArrayList<>();
        postList4.add(post7);
        postList4.add(post8);

        Long verNbr = 7372L;
        Verification verification = manager.createVerification(user, verNbr, postList, cal.getTime(), customer, "");
        assertNotNull(verification);

        Verification verification2 = manager.createVerification(user, verNbr + 1, postList2, cal.getTime(), customer, "");
        assertNotNull(verification2);

        cal.set(500, 10, 10);
        Date pastDate = cal.getTime();
        Verification pastVerification = manager.createVerification(user, postList3, pastDate, customer, "");
        assertNotNull(pastVerification);

        // This will not be included in the final result because it is of account type
        Verification pastVerificationWithWrongAccounts = manager.createVerification(user, postList4, pastDate, customer, "");
        assertNotNull(pastVerificationWithWrongAccounts);

        Verification verificationFromDb = service.findByUserAndVerificationNumber(user, verNbr);
        assertNotNull(verificationFromDb);

        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");

        cal.set(1000, 10, 10);
        Date startDate = cal.getTime();
        cal.set(3000, 1, 1);
        Date endDate = cal.getTime();

        BalanceSheetPresenter presenter = new BalanceSheetPresenter(user, startDate, endDate, terms.getPageRequest());
        try {
            presenter.print();
        } catch (IOException | DocumentException e) {
            assert (false);
            // TODO: fix path
//            assert(false);
        }
    }

    @Transactional
    @Test
    public void printIncomeStatement() {
        double sum1Amount = 100.0;
        double sum2Amount = 100.0;

        double sum3Amount = 200.0;
        double sum4Amount = 200.0;

        double sum5Amount = 100.0;
        double sum6Amount = 100.0;

        Calendar cal = Calendar.getInstance();

        Account account = accountManager.createAccount(2018, "Fel");
        Account account2 = accountManager.createAccount(4055, "Någon kostnad");
        Account account3 = accountManager.createAccount(3055, "Någon inkomst");

        PostSum sum = new PostSum();
        sum.setSumTotal(sum1Amount);
        sum.setType(PostType.Credit);

        PostSum sum2 = new PostSum();
        sum2.setSumTotal(sum2Amount);
        sum2.setType(PostType.Debit);

        PostSum sum3 = new PostSum();
        sum3.setSumTotal(sum3Amount);
        sum3.setType(PostType.Debit);

        PostSum sum4 = new PostSum();
        sum4.setSumTotal(sum4Amount);
        sum4.setType(PostType.Credit);

        PostSum sum5 = new PostSum();
        sum5.setSumTotal(sum5Amount);
        sum5.setType(PostType.Debit);

        PostSum sum6 = new PostSum();
        sum6.setSumTotal(sum6Amount);
        sum6.setType(PostType.Credit);

        Customer customer = customerManager.createCustomer(user, 123, "Jakob", "03484838", null);

        Post post = postManager.createPost(sum, account);
        Post post2 = postManager.createPost(sum2, account2);
        Post post3 = postManager.createPost(sum3, account2);
        Post post4 = postManager.createPost(sum4, account);
        Post post5 = postManager.createPost(sum5, account);
        Post post6 = postManager.createPost(sum6, account);

        Post post7 = postManager.createPost(sum, account3);
        Post post8 = postManager.createPost(sum2, account3);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        postList.add(post2);

        ArrayList<Post> postList2 = new ArrayList<>();
        postList2.add(post3);
        postList2.add(post4);

        ArrayList<Post> postList3 = new ArrayList<>();
        postList3.add(post5);
        postList3.add(post6);

        ArrayList<Post> postList4 = new ArrayList<>();
        postList4.add(post7);
        postList4.add(post8);

        Long verNbr = 7372L;
        Verification verification = manager.createVerification(user, verNbr, postList, cal.getTime(), customer, "");
        assertNotNull(verification);

        Verification verification2 = manager.createVerification(user, verNbr + 1, postList2, cal.getTime(), customer, "");
        assertNotNull(verification2);

        cal.set(500, 10, 10);
        Date pastDate = cal.getTime();
        Verification pastVerification = manager.createVerification(user, postList3, pastDate, customer, "");
        assertNotNull(pastVerification);

        // This will not be included in the final result because it is of account type
        Verification pastVerificationWithWrongAccounts = manager.createVerification(user, postList4, pastDate, customer, "");
        assertNotNull(pastVerificationWithWrongAccounts);

        Verification verificationFromDb = service.findByUserAndVerificationNumber(user, verNbr);
        assertNotNull(verificationFromDb);

        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");

        cal.set(1000, 10, 10);
        Date startDate = cal.getTime();
        cal.set(3000, 1, 1);
        Date endDate = cal.getTime();

        IncomeStatementPresenter presenter = new IncomeStatementPresenter(user, startDate, endDate, terms.getPageRequest());
        try {
            presenter.print();
        } catch (IOException | DocumentException e) {
            assert (false);
            // TODO: fix path
//            assert(false);
        }
    }

}

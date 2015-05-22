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

    @Autowired
    IncomeStatementPresenter incStatement;

    @Autowired
    BalanceSheetPresenter balanceSheet;

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

        double sum5Amount = 300.0;
        double sum6Amount = 300.0;

        Calendar cal = Calendar.getInstance();

        Account account = accountManager.createAccount(2018, "Egna insättningar");
        Account account2 = accountManager.createAccount(1055, "Saker");
        Account account3 = accountManager.createAccount(3055, "Fel");
        Account account4 = accountManager.createAccount(1010, "Kassa");
        Account account5 = accountManager.createAccount(1055, "Bankkonto");
        Account account6 = accountManager.createAccount(1056, "Postgiro");
        Account account7 = accountManager.createAccount(1057, "Tillgång");
        Account account8 = accountManager.createAccount(1065, "Tillgång 2");
        Account account9 = accountManager.createAccount(1001, "Tillgång 3");
        Account account10 = accountManager.createAccount(2003, "Tillgång 4");
        Account account11 = accountManager.createAccount(2055, "Skuld");
        Account account12 = accountManager.createAccount(2065, "Skuld 2");
        Account account13 = accountManager.createAccount(2035, "Skuld 3");
        Account account14 = accountManager.createAccount(2075, "Skuld 4");
        Account account15 = accountManager.createAccount(2085, "Skuld 5");
        Account account16 = accountManager.createAccount(1275, "Tillgång 5");
        Account account17 = accountManager.createAccount(2095, "Skuld 5");
        Account account18 = accountManager.createAccount(1375, "Tillgång 6");
        Account account19 = accountManager.createAccount(2096, "Skuld 6");
        Account account20 = accountManager.createAccount(1475, "Tillgång 7");
        Account account21 = accountManager.createAccount(2097, "Skuld 7");
        Account account22 = accountManager.createAccount(1575, "Tillgång 8");
        Account account23 = accountManager.createAccount(2098, "Skuld 8");
        Account account24 = accountManager.createAccount(1675, "Tillgång 9");
        Account account25 = accountManager.createAccount(2099, "Skuld 9");
        Account account26 = accountManager.createAccount(1775, "Tillgång 10");
        Account account27 = accountManager.createAccount(2999, "Skuld 10");
        Account account28 = accountManager.createAccount(1875, "Tillgång 11");

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
        Post post5 = postManager.createPost(sum5, account15);
        Post post6 = postManager.createPost(sum6, account4);
        Post post7 = postManager.createPost(sum, account5);
        Post post8 = postManager.createPost(sum2, account14);
        Post post9 = postManager.createPost(sum, account6);
        Post post10 = postManager.createPost(sum2, account13);
        Post post11 = postManager.createPost(sum, account7);
        Post post12 = postManager.createPost(sum2, account12);
        Post post13 = postManager.createPost(sum, account8);
        Post post14 = postManager.createPost(sum2, account11);
        Post post15 = postManager.createPost(sum, account9);
        Post post16 = postManager.createPost(sum2, account10);
        Post post17 = postManager.createPost(sum, account16);
        Post post18 = postManager.createPost(sum2, account17);
        Post post19 = postManager.createPost(sum, account18);
        Post post20 = postManager.createPost(sum2, account19);
        Post post21 = postManager.createPost(sum, account20);
        Post post22 = postManager.createPost(sum2, account21);
        Post post23 = postManager.createPost(sum, account22);
        Post post24 = postManager.createPost(sum2, account23);
        Post post25 = postManager.createPost(sum, account24);
        Post post26 = postManager.createPost(sum2, account25);
        Post post27 = postManager.createPost(sum6, account26);
        Post post28 = postManager.createPost(sum5, account27);
        Post post29 = postManager.createPost(sum5, account28);
        Post post30 = postManager.createPost(sum6, account3);

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

        ArrayList<Post> postList5 = new ArrayList<>();
        postList5.add(post9);
        postList5.add(post10);

        ArrayList<Post> postList6 = new ArrayList<>();
        postList6.add(post11);
        postList6.add(post12);

        ArrayList<Post> postList7 = new ArrayList<>();
        postList7.add(post13);
        postList7.add(post14);

        ArrayList<Post> postList8 = new ArrayList<>();
        postList8.add(post15);
        postList8.add(post16);

        ArrayList<Post> postList9 = new ArrayList<>();
        postList9.add(post17);
        postList9.add(post18);

        ArrayList<Post> postList10 = new ArrayList<>();
        postList10.add(post19);
        postList10.add(post20);

        ArrayList<Post> postList11 = new ArrayList<>();
        postList11.add(post21);
        postList11.add(post22);

        ArrayList<Post> postList12 = new ArrayList<>();
        postList12.add(post23);
        postList12.add(post24);

        ArrayList<Post> postList13 = new ArrayList<>();
        postList13.add(post25);
        postList13.add(post26);

        ArrayList<Post> postList14 = new ArrayList<>();
        postList14.add(post27);
        postList14.add(post28);

        ArrayList<Post> postList15 = new ArrayList<>();
        postList15.add(post29);
        postList15.add(post30);

        Long verNbr = 7372L;
        Verification verification = manager.createVerification(user, verNbr, postList, cal.getTime(), customer, "");
        assertNotNull(verification);
        Verification verification2 = manager.createVerification(user, verNbr + 1, postList2, cal.getTime(), customer, "");
        assertNotNull(verification2);
        Verification verification3 = manager.createVerification(user, verNbr + 2, postList3, cal.getTime(), customer, "");
        assertNotNull(verification3);
        Verification verification4 = manager.createVerification(user, verNbr + 3, postList4, cal.getTime(), customer, "");
        assertNotNull(verification4);
        Verification verification5 = manager.createVerification(user, verNbr + 4, postList5, cal.getTime(), customer, "");
        assertNotNull(verification5);
        Verification verification6 = manager.createVerification(user, verNbr + 5, postList6, cal.getTime(), customer, "");
        assertNotNull(verification6);
        Verification verification7 = manager.createVerification(user, verNbr + 6, postList7, cal.getTime(), customer, "");
        assertNotNull(verification7);
        Verification verification8 = manager.createVerification(user, verNbr + 7, postList8, cal.getTime(), customer, "");
        assertNotNull(verification8);
        Verification verification9 = manager.createVerification(user, verNbr + 8, postList9, cal.getTime(), customer, "");
        assertNotNull(verification9);
        Verification verification10 = manager.createVerification(user, verNbr + 9, postList10, cal.getTime(), customer, "");
        assertNotNull(verification10);
        Verification verification11 = manager.createVerification(user, verNbr + 10, postList11, cal.getTime(), customer, "");
        assertNotNull(verification11);
        Verification verification12 = manager.createVerification(user, verNbr + 11, postList12, cal.getTime(), customer, "");
        assertNotNull(verification12);
        Verification verification13 = manager.createVerification(user, verNbr + 12, postList13, cal.getTime(), customer, "");
        assertNotNull(verification13);
        Verification verification14 = manager.createVerification(user, verNbr + 13, postList14, cal.getTime(), customer, "");
        assertNotNull(verification14);
        Verification verification15 = manager.createVerification(user, verNbr + 14, postList15, cal.getTime(), customer, "");
        assertNotNull(verification15);

        cal.set(500, 10, 10);
        Date pastDate = cal.getTime();
        Verification pastVerification = manager.createVerification(user, postList3, pastDate, customer, "");
        assertNotNull(pastVerification);

        Verification pastVerificationWithWrongAccounts = manager.createVerification(user, postList4, pastDate, customer, "");
        assertNotNull(pastVerificationWithWrongAccounts);

        Verification verificationFromDb = service.findByUserAndVerificationNumber(user, verNbr);
        assertNotNull(verificationFromDb);

        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");

        cal.set(1000, 10, 10);
        Date startDate = cal.getTime();
        cal.set(3000, 1, 1);
        Date endDate = cal.getTime();

        try {
            balanceSheet.print(user, startDate, endDate, terms.getPageRequest());
        } catch (IOException | DocumentException e) {
            System.out.println(e.getMessage());
            assert (false);

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
        Account account2 = accountManager.createAccount(4055, "Någon utgift");
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

        Verification pastVerificationWithWrongAccounts = manager.createVerification(user, postList4, pastDate, customer, "");
        assertNotNull(pastVerificationWithWrongAccounts);

        Verification verificationFromDb = service.findByUserAndVerificationNumber(user, verNbr);
        assertNotNull(verificationFromDb);

        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");

        cal.set(1000, 10, 10);
        Date startDate = cal.getTime();
        cal.set(3000, 1, 1);
        Date endDate = cal.getTime();

        try {
            incStatement.print(user, startDate, endDate, terms.getPageRequest());
        } catch (IOException | DocumentException e) {
            assert (false);
        }
    }
}
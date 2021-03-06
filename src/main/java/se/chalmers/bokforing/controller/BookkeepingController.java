package se.chalmers.bokforing.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.chalmers.bokforing.jsonobject.AccountJSON;
import se.chalmers.bokforing.jsonobject.FormJSON;
import se.chalmers.bokforing.jsonobject.PostJSON;
import se.chalmers.bokforing.jsonobject.VerificationJSON;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.PostType;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.service.CustomerManager;
import se.chalmers.bokforing.service.CustomerService;
import se.chalmers.bokforing.service.UserService;
import se.chalmers.bokforing.service.AccountManager;
import se.chalmers.bokforing.service.VerificationManager;
import se.chalmers.bokforing.service.AccountService;
import se.chalmers.bokforing.service.PostManager;
import se.chalmers.bokforing.service.PostService;
import se.chalmers.bokforing.service.VerificationService;
import se.chalmers.bokforing.session.AuthSession;

/**
 *
 * @author Dženan
 */
@Controller
public class BookkeepingController {

    @Autowired
    private VerificationManager verificationManager;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    AccountService accountService;

    @Autowired
    private AuthSession authSession;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerManager customerManager;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private PostService postService;

    @Autowired
    private PostManager postManager;

    /*
     * CREATE VERIFICATION
     */
    @Transactional
    @RequestMapping(value = "/bookkeeping/createman", method = RequestMethod.POST)
    public @ResponseBody
    FormJSON createVerification(@RequestBody final VerificationJSON verification) {

        System.out.println("* PING bookkeeping/createman");
        FormJSON form = new FormJSON();

        // CHECK SESSION
        if (!(authSession.sessionCheck())) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd!");
            return form;
        }
        String email = authSession.getEmail();

        // CHECK DATE
        if (verification.getTransactionDate() == null) {
            form.addError("verificationdate", "Ange ett datum.");
            return form;
        }

        // CHECK DESCRIPTION
        String description = "";
        if (verification.getDescription() != null) {
            description = verification.getDescription();
        }

        // CHECK POSTS
        if (verification.getPosts() == null || verification.getPosts().isEmpty()) {
            form.addError("general", "Ett fel inträffade, vänligen försök igenom om en liten stund.");
            return form;
        }

        // CREATE POSTS
        List<Post> new_posts = new ArrayList();
        for (PostJSON post : verification.getPosts()) {
            if (post == null) {
                form.addError("general", "Ett fel inträffade, vänligen försök igen om en liten stund.");
                return form;
            }

            int index = new_posts.size() + 1;
            // CHECK ACCOUNT
            if (!(post.getAccountid() > 0)) {
                form.addError("general", "Vänligen välj ett konto för rad " + index + "!");
                return form;
            }
            Account temp_account = accountService.findAccountByNumber(post.getAccountid());
            // THIS SHOULD'NT HAPPEN 
            if (temp_account == null) {
                form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            }
            // CHECK DEBIT AND CREDIT
            if (post.getDebit() < 0 || post.getCredit() < 0) {
                form.addError("general", "Både debet och kredit måste innehålla en siffra i rad " + index + ".");
                return form;
            }

            // CREATE A POSTSUM
            PostSum temp_postSum = new PostSum();

            // DEBIT
            if (post.getDebit() > 0 && post.getCredit() == 0) {
                temp_postSum.setType(PostType.Debit);
                temp_postSum.setSumTotal(post.getDebit());
            } // CREDIT
            else if (post.getCredit() > 0 && post.getDebit() == 0) {
                temp_postSum.setType(PostType.Credit);
                temp_postSum.setSumTotal(post.getCredit());
            } // SOMETHING WRONG
            else {
                form.addError("general", "Rad: " + index + ". Debet och kredit får ej vara lika eller en av dem måste vara 0.");
                return form;
            }

            // EVERYTHING SEEMS TO BE IN ORDER; CREATE POST
            Post temp_post = new Post();
            Date temp_date = new Date();
            temp_post.setAccount(temp_account);
            temp_post.setPostSum(temp_postSum);
            temp_post.setCreationDate(temp_date);
            temp_post.setActive(true);

            new_posts.add(temp_post);
        }

        // EVERYTHING SEEMS TO BE IN ORDER; CREATE VERIFICATION
        UserHandler uh = userService.getUser(email);

        // CREATE VERIFICATION
        Verification ver = verificationManager.createVerification(uh.getUA(), new_posts, verification.getTransactionDate(), null, description);
        if (ver == null) {
            form.addError("general", "Verifikationen är inte giltig.");
            return form;
        }
        uh.getVerifications().add(ver);
        userService.storeUser(uh);

        // System.out.println(userService.getUser(authSession.getEmail()).getVerifications().get(0));
        return form;
    }

    /*
     * EDIT VERIFICATION
     */
    @RequestMapping(value = "/bookkeeping/editverification", method = RequestMethod.POST)
    public @ResponseBody
    FormJSON editVerification(@RequestBody final VerificationJSON verification) {

        FormJSON form = new FormJSON();

        // CHECK SESSION
        if (!(authSession.sessionCheck())) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();
        UserHandler uh = userService.getUser(email);

        // VERIFICATION CHECK
        Verification ver = verificationService.findVerificationById(uh.getUA(), verification.getId());
        if (ver == null) {
            form.addError("general", "Ett fel inträffade, vänligen försök igen om en liten stund.");
            return form;
        }

        // CHECK DESCRIPTION
        String description = "";
        if (verification.getDescription() != null) {
            description = verification.getDescription();
        }

        // CREATE NEW POSTS
        List<Post> new_posts = new ArrayList();
        if (verification.getPosts() != null) {
            for (PostJSON post : verification.getPosts()) {
                if (post == null) {
                    form.addError("general", "Ett fel inträffade, vänligen försök igen om en liten stund.");
                    return form;
                }

                int index = new_posts.size() + 1;
                // CHECK ACCOUNT
                if (!(post.getAccountid() > 0)) {
                    form.addError("general", "Vänligen välj ett konto för rad " + index + "!");
                    return form;
                }
                Account temp_account = accountService.findAccountByNumber(post.getAccountid());
                // THIS SHOULD'NT HAPPEN 
                if (temp_account == null) {
                    form.addError("general", "Något gick fel, vänligen försök igen om en liten stund");
                }
                // CHECK DEBIT AND CREDIT
                if (post.getDebit() < 0 || post.getCredit() < 0) {
                    form.addError("general", "Både debet och kredit måste innehålla en siffra i rad " + index + "!");
                    return form;
                }

                // CREATE A POSTSUM
                PostSum temp_postSum = new PostSum();

                // DEBIT
                if (post.getDebit() > 0 && post.getCredit() == 0) {
                    temp_postSum.setType(PostType.Debit);
                    temp_postSum.setSumTotal(post.getDebit());
                } // CREDIT
                else if (post.getCredit() > 0 && post.getDebit() == 0) {
                    temp_postSum.setType(PostType.Credit);
                    temp_postSum.setSumTotal(post.getCredit());
                } // SOMETHING WRONG
                else {
                    form.addError("general", "Rad: " + index + ". Debet och kredit får ej vara lika eller en av dem måste vara 0.");
                    return form;
                }

                // EVERYTHING SEEMS TO BE IN ORDER; CREATE POST
                Post temp_post = postManager.createPost(temp_postSum, temp_account);

                new_posts.add(temp_post);
            }
        }

        // OLD POSTS CHECK
        List<Post> old_posts = new ArrayList();
        if (verification.getOldposts() != null) {
            for (PostJSON post : verification.getOldposts()) {
                Post temp_post = postService.findPostById(post.getId());
                if (temp_post == null) {
                    form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
                    return form;
                }
                old_posts.add(temp_post);
            }
        }

        // EVERYTHING SEEMS TO BE IN ORDER; EDIT VERIFICATION
        ver.setDescription(description);
        // FUNCTION REPLACE-POST SAVES THE VERIFICATION
        // SO THE DESCRIPTION SHOULD ALSO BE CHANGED
        if (new_posts.size() > 0 || old_posts.size() > 0) {
            if (!verificationManager.replacePost(ver, old_posts, new_posts)) {
                form.addError("general", "Något gick fel, kunde inte ändra posterna.");
                return form;
            }
        } else {
            verificationService.save(ver);
        }

        return form;
    }

    @RequestMapping(value = "/bookkeeping/searchaccount", method = RequestMethod.POST)
    public @ResponseBody
    List<Account> searchAccount(@RequestBody final AccountJSON account) {
        List<Account> accLs;
        int start = 0;

        if (account.getStartrange() > 0) {
            start = account.getStartrange();
        }

        if (account.getNumber() > 0) {
            PagingAndSortingTerms terms = new PagingAndSortingTerms(start, true, "number", 10);
            accLs = accountService.findByNumberLike(account.getNumber(), terms).getContent();
        } else if (!(account.getName().isEmpty())) {
            PagingAndSortingTerms terms = new PagingAndSortingTerms(start, true, "name", 10);
            accLs = accountService.findByNameLike(account.getName(), terms).getContent();
        } else {
            // Return a empty list
            accLs = new ArrayList();
        }
        return accLs;
    }

    @RequestMapping(value = "/bookkeeping/countsearchaccount", method = RequestMethod.POST)
    public @ResponseBody
    long countSearchAccount(@RequestBody final AccountJSON account) {
        long size = 0;

        if (account.getNumber() > 0) {
            PagingAndSortingTerms terms = new PagingAndSortingTerms(0, true, "number");
            size = accountService.findByNumberLike(account.getNumber(), terms).getTotalElements();
        } else if (!(account.getName().isEmpty())) {
            PagingAndSortingTerms terms = new PagingAndSortingTerms(0, true, "name");
            size = accountService.findByNameLike(account.getName(), terms).getTotalElements();
        }
        return size;
    }

    @RequestMapping(value = "/bookkeeping/getverifications", method = RequestMethod.POST)
    public @ResponseBody
    List<VerificationJSON> getVerifications(@RequestBody final String start) {
        List<VerificationJSON> verJSONLs = new ArrayList();
        int startPos = 0;

        if (!authSession.sessionCheck()) {
            return verJSONLs;
        }
        UserHandler ua = userService.getUser(authSession.getEmail());

        if (Integer.parseInt(start) > 0) {
            startPos = Integer.parseInt(start);
        }

        PagingAndSortingTerms terms = new PagingAndSortingTerms(startPos, Boolean.FALSE, "creationDate");
        Page<Verification> verPage = verificationService.findAllVerifications(ua.getUA(), terms);

        List<Verification> verLs = verPage.getContent();

        verJSONLs = convertToVerificationJSONLs(verLs);
        return verJSONLs;
    }

    @RequestMapping(value = "/bookkeeping/countverifications", method = RequestMethod.GET)
    public @ResponseBody
    long countVerifications() {
        long size = 0;

        if (!authSession.sessionCheck()) {
            return size;
        }
        UserHandler ua = userService.getUser(authSession.getEmail());

        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");
        Page<Verification> verPage = verificationService.findAllVerifications(ua.getUA(), terms);

        size = verPage.getTotalElements();

        return size;
    }

    @RequestMapping(value = "/bookkeeping/getverificationsbyaccount", method = RequestMethod.POST)
    public @ResponseBody
    List<VerificationJSON> getVerificationByAccount(@RequestBody final AccountJSON account) {

        List<VerificationJSON> verJSONLs = new ArrayList();
        int start = 0;

        if (account.getStartrange() > 0) {
            start = account.getStartrange();
        }

        if (!authSession.sessionCheck()) {
            return verJSONLs;
        }
        UserHandler userHandler = userService.getUser(authSession.getEmail());

        PagingAndSortingTerms terms = new PagingAndSortingTerms(start, Boolean.FALSE, "creationDate", 10);
        Account entityAccount = new Account();
        entityAccount.setNumber(account.getNumber());
        List<Post> postLs = postService.findPostsForUserAndAccount(userHandler.getUA(), entityAccount, true, terms).getContent();

        List<Verification> verLs = new ArrayList();
        for (Post post : postLs) {
            verLs.add(post.getVerification());
        }
        verJSONLs = convertToVerificationJSONLs(verLs);

        return verJSONLs;
    }

    @RequestMapping(value = "/bookkeeping/countverificationsbyaccount", method = RequestMethod.POST)
    public @ResponseBody
    long countVerificationsByAccount(@RequestBody final AccountJSON account) {
        long size = 0;

        if (!authSession.sessionCheck()) {
            return size;
        }
        UserHandler uh = userService.getUser(authSession.getEmail());

        PagingAndSortingTerms terms = new PagingAndSortingTerms(0, Boolean.FALSE, "creationDate");
        Account entityAccount = new Account();
        entityAccount.setNumber(account.getNumber());
        Page<Post> postPage = postService.findPostsForUserAndAccount(uh.getUA(), entityAccount, true, terms);

        size = postPage.getTotalElements();

        return size;
    }

    /**
     * CONVERT TO VERIFICATIONJSON LIST
     *
     * Takes a Verification-List and converts it to a VerificationJSON-List
     *
     * @param verLs
     * @return
     */
    public List<VerificationJSON> convertToVerificationJSONLs(List<Verification> verLs) {
        List<VerificationJSON> verJSONLs = new ArrayList();

        // TRANSLATE VERIFICATION TO VERIFICATION JSON
        for (Verification ver : verLs) {
            VerificationJSON verJSON = new VerificationJSON();

            verJSON.setId(ver.getId());
            verJSON.setTransactionDate(ver.getTransactionDate());
            verJSON.setCreationDate(ver.getCreationDate());
            verJSON.setDescription(ver.getDescription());

            // CREATE POST-JSON'S
            List<PostJSON> debitPostJSONLs = new ArrayList();
            List<PostJSON> creditPostJSONLs = new ArrayList();
            List<PostJSON> oldPostJSONLs = new ArrayList();
            for (Post post : ver.getPosts()) {
                PostJSON postJSON = new PostJSON();
                postJSON.setId(post.getId());
                postJSON.setAccountid(post.getAccount().getNumber());
                postJSON.setAccountname(post.getAccount().getName());

                // ADD POST TO RIGHT LS
                if (!post.isActive()) {
                    // OLD POSTS
                    if (post.getPostSum().getType().equals(PostType.Debit)) {
                        postJSON.setDebit(post.getPostSum().getSumTotal());
                        postJSON.setCredit(0);
                    } else {
                        postJSON.setDebit(0);
                        postJSON.setCredit(post.getPostSum().getSumTotal());
                    }
                    postJSON.setCreationdate(new Date());
                    oldPostJSONLs.add(postJSON);
                } else if (post.getPostSum().getType().equals(PostType.Debit)) {
                    postJSON.setSum(post.getPostSum().getSumTotal());
                    debitPostJSONLs.add(postJSON);
                } else if (post.getPostSum().getType().equals(PostType.Credit)) {
                    postJSON.setSum(post.getPostSum().getSumTotal());
                    creditPostJSONLs.add(postJSON);
                }
            }
            verJSON.setDebitposts(debitPostJSONLs);
            verJSON.setCreditposts(creditPostJSONLs);
            if (oldPostJSONLs.size() > 0) {
                verJSON.setOldposts(oldPostJSONLs);
            }

            // CALC THE TOTAL SUM
            // TOTAL DEBIT AND TOTAL CREDIT SHOULD BE EQUAL
            double totalSum = 0;
            for (PostJSON postJSON : verJSON.getDebitposts()) {
                totalSum = totalSum + postJSON.getSum();
            }
            verJSON.setSum(totalSum);

            verJSONLs.add(verJSON);
        }

        return verJSONLs;
    }

    @Transactional
    @RequestMapping(value = "/bookkeeping/addtofavaccounts", method = RequestMethod.POST)
    public @ResponseBody
    FormJSON addToFavoriteAccounts(@RequestBody final AccountJSON account) {

        FormJSON form = new FormJSON();

        // CHECK SESSION
        if (!(authSession.sessionCheck())) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();

        // ACCOUNT CHECK
        if (!(account.getNumber() > 0)) {
            form.addError("account", "Vänligen ange ett konto.");
            return form;
        }
        Account aDb = accountService.findAccountByNumber(account.getNumber());
        if (aDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }

        // EVERYTHING SEEMS TO BE IN ORDER
        UserHandler uh = userService.getUser(email);
        Set<Account> accountSet = uh.getUA().getFavoriteAccounts();
        if (accountSet.contains(aDb)) {
            form.addError("accounts", "Kontot finns redan i listan.");
            return form;
        }
        accountSet.add(aDb);
        uh.getUA().setFavoriteAccounts(accountSet);
        userService.storeUser(uh);

        return form;
    }

    @Transactional
    @RequestMapping(value = "/bookkeeping/deletefromfavaccounts", method = RequestMethod.POST)
    public @ResponseBody
    FormJSON deleteFromFavoriteAccounts(@RequestBody final AccountJSON account) {

        FormJSON form = new FormJSON();

        // CHECK SESSION
        if (!(authSession.sessionCheck())) {
            form.addError("general", "Ett fel inträffade, du har inte rätt tillstånd för att utföra denna åtgärd.");
            return form;
        }
        String email = authSession.getEmail();

        // ACCOUNT CHECK
        if (!(account.getNumber() > 0)) {
            form.addError("account", "Vänligen ange ett konto.");
            return form;
        }
        Account aDb = accountService.findAccountByNumber(account.getNumber());
        if (aDb == null) {
            form.addError("general", "Något gick fel, vänligen försök igen om en liten stund.");
            return form;
        }

        // EVERYTHING SEEMS TO BE IN ORDER
        UserHandler uh = userService.getUser(email);
        Set<Account> accountSet = uh.getUA().getFavoriteAccounts();
        accountSet.remove(aDb);
        uh.getUA().setFavoriteAccounts(accountSet);
        userService.storeUser(uh);

        return form;
    }

    @Transactional
    @RequestMapping(value = "/bookkeeping/getfavaccounts", method = RequestMethod.GET)
    public @ResponseBody
    List<AccountJSON> getFavoriteAccounts() {

        List<AccountJSON> accountJSONLs = new ArrayList();

        if (!authSession.sessionCheck()) {
            return accountJSONLs;
        }
        UserHandler uh = userService.getUser(authSession.getEmail());
        Set<Account> accountSet = uh.getUA().getFavoriteAccounts();

        for (Account a : accountSet) {
            AccountJSON a_json = new AccountJSON();
            a_json.setNumber(a.getNumber());
            a_json.setName(a.getName());

            accountJSONLs.add(a_json);
        }

        return accountJSONLs;
    }
}

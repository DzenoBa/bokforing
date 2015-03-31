///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package se.chalmers.bokforing;
//
//import com.lowagie.text.DocumentException;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import static org.junit.Assert.assertNotNull;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.w3c.dom.Document;
//import org.xhtmlrenderer.pdf.ITextOutputDevice;
//import org.xhtmlrenderer.pdf.ITextRenderer;
//import org.xhtmlrenderer.pdf.ITextUserAgent;
//import org.xml.sax.InputSource;
//import se.chalmers.bokforing.config.TestApplicationConfig;
//import se.chalmers.bokforing.model.Account;
//import se.chalmers.bokforing.model.Customer;
//import se.chalmers.bokforing.model.Post;
//import se.chalmers.bokforing.model.PostSum;
//import se.chalmers.bokforing.model.PostType;
//import se.chalmers.bokforing.model.Verification;
//import se.chalmers.bokforing.model.user.UserAccount;
//import se.chalmers.bokforing.service.AccountService;
//import se.chalmers.bokforing.service.CustomerManager;
//import se.chalmers.bokforing.service.UserManager;
//import se.chalmers.bokforing.service.VerificationManager;
//import se.chalmers.bokforing.service.VerificationService;
//import org.xhtmlrenderer.resource.XMLResource;
//
///**
// *
// * @author Jakob
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestApplicationConfig.class)
//public class PDFTest {
//
//    @Autowired
//    VerificationManager verMan;
//
//    @Autowired
//    CustomerManager customerManager;
//
//    @Autowired
//    AccountService accountService;
//
//    @Autowired
//    VerificationService verService;
//
//    @Autowired
//    UserManager userManager;
//
//    @Test
//    public void testCreatePDF() throws IOException {
//        UserAccount userAccount = new UserAccount();
//        userAccount.setEmail("jakob@jakob.com");
//        userManager.createUser(userAccount);
//        userAccount.setId(1L);
//        long number = 124;
//        createVerification(userAccount, number);
//        Verification ver = verService.findByUserAndVerificationNumber(userAccount, number);
//
//        File pdf = new File("C:/hello/itext.pdf");
//        String url = "C:/hello/test.html";
//        
//        
//        OutputStream os = null;
//        try {
//            os = new FileOutputStream(pdf);
//
//            /* standard approach
//             ITextRenderer renderer = new ITextRenderer();
//             renderer.setDocument(url);
//             renderer.layout();
//             renderer.createPDF(os);
//             */
//            ITextRenderer renderer = new ITextRenderer();
//            ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(renderer.getOutputDevice());
//            callback.setSharedContext(renderer.getSharedContext());
//            renderer.getSharedContext().setUserAgentCallback(callback);
//
//            Document doc = XMLResource.load(new InputSource(url)).getDocument();
//
//            renderer.setDocument(doc, url);
//            renderer.layout();
//            renderer.createPDF(os);
//
//            os.close();
//            os = null;
//        } catch (DocumentException ex) {
//            Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            if (os != null) {
//                os.close();
//            }
//        }
//    }
//
//    private static class ResourceLoaderUserAgent extends ITextUserAgent {
//
//        public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
//            super(outputDevice);
//        }
//
//        @Override
//        protected InputStream resolveAndOpenStream(String uri) {
//            InputStream is = super.resolveAndOpenStream(uri);
//            System.out.println("IN resolveAndOpenStream() " + uri);
//            return is;
//        }
//    }
//
//public Verification createVerification(UserAccount user, long verificationNumber) {
//        double sum1Amount = 100;
//        double sum2Amount = 100;
//        
//        double sum3Amount = 200;
//        double sum4Amount = 200;
//       
//        Calendar cal = Calendar.getInstance();
//        Account account = new Account();
//        account.setName("Egna insättningar");
//        account.setNumber(2018);
//        
//        accountService.save(account);
//        
//        PostSum sum = new PostSum();
//        sum.setSumTotal(sum1Amount);
//        sum.setType(PostType.Credit);
//        
//        PostSum sum2 = new PostSum();
//        sum2.setSumTotal(sum2Amount);
//        sum2.setType(PostType.Debit);
//        
//        PostSum sum3 = new PostSum();
//        sum3.setSumTotal(sum3Amount);
//        sum3.setType(PostType.Debit);
//        
//        PostSum sum4 = new PostSum();
//        sum4.setSumTotal(sum4Amount);
//        sum4.setType(PostType.Credit);
//        
//        Customer customer = customerManager.createCustomer(user, 123, null, null, null);
//        customer.setCustomerNumber(1L);
//        customer.setName("Jakob");
//        customer.setPhoneNumber("031132314");
//        
//        Post post = new Post();
//        post.setSum(sum);
//        post.setAccount(account);
//        
//        Post post2 = new Post();
//        post2.setSum(sum2);
//        post2.setAccount(account);
//
//        Post post3 = new Post();
//        post3.setSum(sum3);
//        post3.setAccount(account);
//        
//        Post post4 = new Post();
//        post4.setSum(sum4);
//        post4.setAccount(account);
//        
//        ArrayList<Post> postList = new ArrayList<>();
//        postList.add(post);
//        postList.add(post2);
//        
//        ArrayList<Post> postList2 = new ArrayList<>();
//        postList.add(post3);
//        postList.add(post4);
//        
//        Long verNbr = 7372L; // one higher than the highest inserted row
//        Verification verification = verMan.createVerification(user, verificationNumber, postList, cal.getTime(), customer);
//        assertNotNull(verification);
//        
//        Verification verification2 = verMan.createVerification(user, verificationNumber+1, postList2, cal.getTime(), customer);
//        assertNotNull(verification2);
//        
//        return verification;
//    }
//
//}

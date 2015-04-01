    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.Verification;

/**
 *
 * @author Jakob
 */
@Service
public class VerificationManagerImpl implements VerificationManager {
    
    @Autowired
    private VerificationService service;

    @Override
    public Verification createVerification(UserAccount user, List<Post> posts, Date transactionDate, Customer customer) {
        // Set to one higher than the highest ID, as Verification have to be
        // perfectly chronological
        long verificationNbr = service.findHighestVerificationNumber(user) + 1;
        
        return createVerification(user, verificationNbr, posts, transactionDate, customer);
    }
    
    @Override
    public Verification createVerification(UserAccount user, long verificationNumber, List<Post> posts, Date transactionDate, Customer customer) {
        if(!isVerificationValid(user, verificationNumber, posts, transactionDate)) {
            return null;
        }
        
        Date todaysDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(todaysDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Verification ver = new Verification();
        
        for(Post post : posts) {
            post.setVerification(ver);
        }
        
        ver.setPosts(posts);
        ver.setTransactionDate(transactionDate);
        ver.setCreationDate(todaysDate);
        ver.setCustomer(customer);
        ver.setUserAccount(user);
        ver.setVerificationNumber(verificationNumber);
        service.save(ver);
        
        return ver;
    }

    private boolean isVerificationValid(UserAccount user, long verificationNumber, List<Post> posts, Date transactionDate) {
        /*if(DateUtil.isDateBeforeToday(transactionDate)) {
            return false;
        }*/
        
        Long highestVerificationNumber = service.findHighestVerificationNumber(user);
//        if(highestVerificationNumber == null || verificationNumber != highestVerificationNumber + 1) {
//            return false;
//        }
        
        if(getBalance(posts) != 0) {
            return false;
        }
        
        return true;
    }
    
    private double getBalance(List<Post> posts) {
        double balance = 0;
        
        for(Post post : posts) {
            PostSum sum = post.getPostSum();
            if(sum != null && sum.getType() != null) {
                switch(sum.getType()) {
                    case Credit:
                        balance -= sum.getSumTotal();
                        break;
                    case Debit:
                        balance += sum.getSumTotal();
                        break;
                }
            }
        }
        
        return balance;
    }
}

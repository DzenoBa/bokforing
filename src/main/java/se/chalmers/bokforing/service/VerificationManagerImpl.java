/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.Date;
import se.chalmers.bokforing.service.VerificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.model.PostType;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.util.DateUtil;

/**
 *
 * @author Jakob
 */
@Service
public class VerificationManagerImpl implements VerificationManager {
    
    @Autowired
    private VerificationService service;

    @Override
    public Verification createVerification(long verificationNbr, List<Post> posts, Date transactionDate, Customer customer) {
        if(!isVerificationValid(verificationNbr, posts, transactionDate)) {
            return null;
        }
        
        Verification ver = new Verification();
        ver.setId(verificationNbr);
        ver.setPosts(posts);
        ver.setTransactionDate(transactionDate);
        ver.setCustomer(customer);
        service.save(ver);
        
        return ver;
    }

    private boolean isVerificationValid(long verificationNbr, List<Post> posts, Date transactionDate) {
        if(DateUtil.isDateBeforeToday(transactionDate)) {
            return false;
        }
        
        Long highestVerificationNumber = service.findHighestId();
        if(highestVerificationNumber == null || verificationNbr != highestVerificationNumber + 1) {
            return false;
        }
        
        if(getBalance(posts) != 0) {
            return false;
        }
        
        return true;
    }
    
    private double getBalance(List<Post> posts) {
        double balance = 0;
        
        for(Post post : posts) {
            PostSum sum = post.getPostSum();
            
            switch(sum.getType()) {
                case Credit:
                    balance -= sum.getSumTotal();
                    break;
                case Debit:
                    balance += sum.getSumTotal();
                    break;
            }
        }
        
        return balance;
    }
    
    
}

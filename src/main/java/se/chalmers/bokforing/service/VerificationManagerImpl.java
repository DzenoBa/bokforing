    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.ArrayList;
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
        
        Date todaysDate = DateUtil.getTodaysDate();
        
        Verification ver = new Verification();
        
        for(Post post : posts) {
            post.setCorrection(false); // safeguard
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
        
        if(!arePostsValid(posts)) {
            return false;
        }
        
        return true;
    }

    private boolean arePostsValid(List<Post> posts) {
        return getBalance(posts) == 0;
    }
    
    private double getBalance(List<Post> posts) {
        double balance = 0;
        
        for(Post post : posts) {
            if(post.isActive()) { // we only care about ones that haven't been replaced
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
        }
        
        return balance;
    }

    @Override
    public boolean replacePost(Verification verification, Post oldPost, Post newPost) {
        List<Post> oldPosts = new ArrayList<>();
        oldPosts.add(oldPost);
        
        List<Post> newPosts = new ArrayList<>();
        newPosts.add(newPost);
        
        return replacePost(verification, oldPosts, newPosts);
    }
    
    @Override
    public boolean replacePost(Verification verification, List<Post> oldPosts, List<Post> newPosts) {
        List<Post> tempPosts = new ArrayList<>(verification.getPosts());
        
        for(Post newPost : newPosts) {
            newPost.setCorrection(true);
            tempPosts.add(newPost);
        }
        
        for(Post postInVer : tempPosts) {
            if(oldPosts.contains(postInVer)) {
                // Posts to be replaced should no longer be active
                postInVer.setActive(false);
            }
        }
        
        if(arePostsValid(tempPosts)) {
            verification.setPosts(tempPosts);
            service.save(verification);
            return true;
        } else {
            // Posts weren't valid, so don't save. We need to set active
            // flag back to true again, to not affect the posts that are actually
            // valid
            for(Post postInVer : tempPosts) {
                if(oldPosts.contains(postInVer)) {
                    postInVer.setActive(true);
                }
            }
            return false;
        }
    }
}

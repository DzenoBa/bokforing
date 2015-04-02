/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.Date;
import java.util.List;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.Verification;

/**
 * Manager class for taking care of logic concerning verifications
 * 
 * @author Jakob
 */
public interface VerificationManager {
    
    Verification createVerification(UserAccount user, List<Post> posts, Date transactionDate, Customer customer);
    
    Verification createVerification(UserAccount user, long verificationNbr, List<Post> posts, Date transactionDate, Customer customer);
    
    boolean replacePost(Verification verification, Post oldPost, Post newPost);
    
    boolean replacePost(Verification verification, List<Post> oldPosts, List<Post> newPosts);
}

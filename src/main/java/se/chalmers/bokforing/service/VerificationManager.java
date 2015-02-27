/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.List;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.Verification;

/**
 * Manager class for taking care of logic concerning verifications
 * 
 * @author Jakob
 */
public interface VerificationManager {
    
    Verification createVerification(String verificationNbr, List<Post> posts);
    
    
}

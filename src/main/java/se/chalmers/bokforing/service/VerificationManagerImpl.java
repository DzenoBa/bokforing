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
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.Post;
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
    public Verification createVerification(String verificationNbr, List<Post> posts, Date transactionDate) {
        if(isVerificationValid(verificationNbr, posts, transactionDate)) {
            Verification ver = new Verification();
            ver.setId(verificationNbr);
            ver.setPosts(posts);
            ver.setTransactionDate(transactionDate);
            service.save(ver);
            return ver;
        }
        
        return null;
    }

    private boolean isVerificationValid(String verificationNbr, List<Post> posts, Date transactionDate) {
        if(DateUtil.isDateBeforeToday(transactionDate)) {
            return false;
        }
        // Some more validation here I guess
        
        
        return true;
    }
    
    
}

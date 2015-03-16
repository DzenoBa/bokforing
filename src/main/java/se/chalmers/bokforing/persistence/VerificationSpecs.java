/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.model.Verification_;

/**
 *
 * @author Jakob
 */
public class VerificationSpecs {
    
    public static Specification<Verification> hasUserAccount(final UserAccount userAccount) {
        return new Specification<Verification>() {
           @Override
           public Predicate toPredicate(Root<Verification> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(Verification_.userAccount), userAccount); 
           }
        };
    }
    
}

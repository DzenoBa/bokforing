/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.user.UserAccount;
import se.chalmers.bokforing.model.Verification;


/**
 *
 * @author Jakob
 */
@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long>, JpaSpecificationExecutor<Verification> {
    
    Verification findByIdAndUserAccount(Long id, UserAccount userAccount);
    
    @Query("SELECT MAX(verificationNumber) FROM Verification v WHERE v.userAccount.id = :userId")
    Long findHighestVerificationNumber(@Param("userId") long userId);
    
    Page<Verification> findByUserAccount(UserAccount userAccount, Pageable pageable);
    
    Page<Verification> findByUserAccountAndCreationDateBetween(UserAccount userAccount, Date startDate, Date endDate, Pageable pageable);
    
    Page<Verification> findByUserAccountOrderByCreationDateAsc(UserAccount userAccount, Pageable pageable);
    
    Page<Verification> findByUserAccountOrderByCreationDateDesc(UserAccount userAccount, Pageable pageable);
    
    Verification findByUserAccountAndVerificationNumber(UserAccount userAccount, Long verificationNumber);
    
    // it's not this simple since account is in post. maybe we don't want this anyway?
    // Page<Verification> findByAccount(Account account, Pageable pageable);
    
}

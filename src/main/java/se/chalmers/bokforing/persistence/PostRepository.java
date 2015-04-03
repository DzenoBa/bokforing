/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.chalmers.bokforing.model.Post;

/**
 *
 * @author Jakob
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    
    @Query(""
            + "select p "
            + "from Post p "
            + "where p.verification.userAccount.id = :userId "
            + "and p.account.number = :accountNumber"
    )
    Page<Post> findPostsForUserAndAccount(@Param("userId") long userId, @Param("accountNumber") int accountNumber, Pageable pageable);
    
}

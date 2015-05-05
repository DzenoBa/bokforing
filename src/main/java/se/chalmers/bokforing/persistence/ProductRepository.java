/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.UserAccount;

/**
 *
 * @author Jakob
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByIdAndUserAccount(Long id, UserAccount userAccount);

    Page<Product> findByUserAccount(UserAccount userAccount, Pageable pageable);

    Page<Product> findByUserAccountAndNameContainingIgnoreCase(UserAccount userAccount, String name, Pageable pageable);
}

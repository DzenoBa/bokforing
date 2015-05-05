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
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.UserAccount;

/**
 *
 * @author Jakob
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByUserAccountAndCustomerNumber(UserAccount user, long customerNumber);

    Page<Customer> findByUserAccount(UserAccount user, Pageable request);

    Page<Customer> findByUserAccountAndNameContainingIgnoreCase(UserAccount userAccount, String name, Pageable pageable);
}

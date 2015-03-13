/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import se.chalmers.bokforing.model.UserInfo;

/**
 *
 * @author Isabelle
 */
public interface CustomerRepository extends JpaRepository<UserInfo,Long> {
    
}

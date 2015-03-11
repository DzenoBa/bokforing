/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

/**
 *
 * @author Isabelle
 * 
 * Interface for UserManagerImpl
 */
public interface UserManger {
    
     /**
     * Called from the frontend when a new user is created
     */
    public void createUser();
}

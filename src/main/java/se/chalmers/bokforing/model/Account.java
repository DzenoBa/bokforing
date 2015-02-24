/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

/**
 *
 * @author Isabelle
 */
public class Account {
    int number;
    String name;
    
    //Constructor for creating a new account
    public Account(int number, String name){
        this.number = number;
        this.name = name;
    }
    //Constructor for assigning a post an existing account 
    public Account(int number){
        this.number = number;
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.util.Calendar;

/**
 *
 * @author Isabelle
 */
public class Post {
    private Sum sum;
    private Account account;
    private Calendar date;
    
    public Post(Calendar date, int sum, int account, enum type){
        this.date = date;
        this.account = new Account(account);
        this.sum = new Sum(sum, type);
    }
}

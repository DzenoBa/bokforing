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
public class Verification {
    private Post[] posts;
    private int verificationNbr;
    public Verification(int verificationNbr, Post[] posts){
        this.verificationNbr = verifikationNbr;
        this.posts = posts;
    }
}

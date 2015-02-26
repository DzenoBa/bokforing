/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Isabelle
 */
@Entity
public class Verification implements Serializable {
    
    @Id
    private String verificationNbr;
    
    @OneToMany
    private List<Post> posts;
    
    public Verification(String verificationNbr, List<Post> posts){
        this.verificationNbr = verificationNbr;
        this.posts = posts;
    }

    /**
     * @return the posts
     */
    public List<Post> getPosts() {
        return posts;
    }

    /**
     * @param posts the posts to set
     */
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    /**
     * @return the verificationNbr
     */
    public String getVerificationNbr() {
        return verificationNbr;
    }

    /**
     * @param verificationNbr the verificationNbr to set
     */
    public void setVerificationNbr(String verificationNbr) {
        this.verificationNbr = verificationNbr;
    }
}

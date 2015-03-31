/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Isabelle
 */
@Entity
public class PostSum implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private double sumTotal;
    
    @Enumerated(EnumType.STRING)
    private PostType type;
    
    /**
     * @return the type
     */
    public PostType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(PostType type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the sumTotal
     */
    public double getSumTotal() {
        return sumTotal;
    }

    /**
     * @param sumTotal the sumTotal to set
     */
    public void setSumTotal(double sumTotal) {
        this.sumTotal = sumTotal;
    }
}

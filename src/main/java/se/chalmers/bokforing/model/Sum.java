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
public class Sum implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private double value;
    
    @Enumerated(EnumType.STRING)
    private PostType type;
    
    public Sum(double value, PostType type){
        this.value = value;
        this.type = type;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

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
}

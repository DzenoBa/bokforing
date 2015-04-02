/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.util.Objects;
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
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.sumTotal) ^ (Double.doubleToLongBits(this.sumTotal) >>> 32));
        hash = 41 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PostSum other = (PostSum) obj;
        if (Double.doubleToLongBits(this.sumTotal) != Double.doubleToLongBits(other.sumTotal)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }
}

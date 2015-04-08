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
import javax.persistence.ManyToOne;
import se.chalmers.bokforing.model.user.UserAccount;

/**
 *
 * @author Jakob
 */
@Entity
public class Product implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private String description;
    private Double price;
    
    @Enumerated(EnumType.STRING)
    private QuantityType quantityType;
    
    @ManyToOne
    private UserAccount userAccount;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the quantityType
     */
    public QuantityType getQuantityType() {
        return quantityType;
    }

    /**
     * @param quantityType the quantityType to set
     */
    public void setQuantityType(QuantityType quantityType) {
        this.quantityType = quantityType;
    }
    
    public double getTotal(double quantity) {
        return getPrice() * quantity;
    }

    /**
     * @return the userAccount
     */
    public UserAccount getUserAccount() {
        return userAccount;
    }

    /**
     * @param userAccount the userAccount to set
     */
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
    
    public enum QuantityType {
        KILOGRAM("kg"),
        HOUR("h"),
        UNIT("st"),
        CENTIMETER("cm"),
        METER("m"),
        KILOMETER("km");
        
        private final String acronym;
        
        QuantityType(String acronym) {
            this.acronym = acronym;
        }
        
        public String getAcronym() {
            return this.acronym;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import se.chalmers.bokforing.model.Product;

/**
 *
 * @author victor
 */
@Entity
public class Invoice implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fakturaId;
    
    @ManyToOne
    private OrderEntity orderEntity;
    
    public OrderEntity getOrderEntity(){
        return orderEntity;
    }
    
    public void setOrderEntity(OrderEntity oe){
        orderEntity = oe;
    }
    
    @ManyToMany
    private final List<Product> prod = new LinkedList<>();

    @ElementCollection
    private final List<Integer> countList = new LinkedList<>();
    
    private boolean valid = true;
    //Date
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fakturaDate = new Date();
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expireDate = fakturaDate;

    /**
     * @return the fakturaId
     */
    public Long getFakturaId() {
        return fakturaId;
    }

    /**
     * @param fakturaId the fakturaId to set
     */
    public void setFakturaId(Long fakturaId) {
        this.fakturaId = fakturaId;
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return the fakturaDate
     */
    public Date getFakturaDate() {
        return fakturaDate;
    }

    /**
     * @param fakturaDate
     */
    public void setFakturaDate(Date fakturaDate) {
        this.fakturaDate = fakturaDate;
    }

    /**
     * @return the expireDate
     */
    public Date getExpireDate() {
        return expireDate;
    }

    /**
     * @param expireDate the expireDate to set
     */
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
    
    public void addProduct(Product p, Integer amount) {
            getProd().add(p);
            getCountList().add(amount);
    }
    
    public HashMap Products(){
        HashMap<Product,Integer> hm = new HashMap<>();
        for(int i = 0; i < getProd().size(); i++){
            hm.put(getProd().get(i), getCountList().get(i));
        }
        return hm;
    }

    /**
     * @return the prod
     */
    public List<Product> getProd() {
        return prod;
    }

    /**
     * @return the countList
     */
    public List<Integer> getCountList() {
        return countList;
    }
}

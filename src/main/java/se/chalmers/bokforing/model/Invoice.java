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

/**
 *
 * @author victor
 */
@Entity
public class Invoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fakturaId;
    
    private boolean ftax = false;
    private String momsNumber;
    private Double moms;

    @ManyToOne
    private UserInfo seller;
    @ManyToOne
    private Customer buyer;

    @ManyToMany
    private List<Product> prod = new LinkedList<>();

    @ElementCollection
    private List<Integer> countList = new LinkedList<>();

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

    public HashMap products() {
        HashMap<Product, Integer> hm = new HashMap<>();
        for (int i = 0; i < getProd().size(); i++) {
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
    
    public void addProduct(Product p) {
        addProduct(p, 1);
    }

    public void addProduct(Product p, Integer amount) {
        if (getProd().contains(p)) {
            int i = getProd().indexOf(p);
            int x = getCountList().get(i);
            x += amount;
            if (x > 0) {
                getCountList().set(i, x);
            } else {
                removeAllOfProduct(p);
            }
        } else {
            getProd().add(p);
            getCountList().add(amount);
        }
    }

    public void removeProduct(Product p) {
        addProduct(p, -1);
    }

    public void removeProduct(Product p, Integer amount) {
        addProduct(p, -amount);
    }

    public void removeAllOfProduct(Product p) {
        int i = getProd().indexOf(p);
        getProd().remove(i);
        getCountList().remove(i);
    }
    
        public UserInfo getSeller() {
        return seller;
    }

    public Customer getBuyer() {
        return buyer;
    }

    public void setSeller(UserInfo ui) {
        seller = ui;
    }

    public void setSeller(UserHandler uh) {
        setSeller(uh.getUI());
    }

    public void setBuyer(Customer cus) {
        buyer = cus;
    }

    public void setFskatt(boolean ftax) {
        this.setFtax(ftax);
    }

    public void setMomsNumber(String vatnumber) {
        this.momsNumber = vatnumber;
    }

    public void setMoms(double vat) {
        this.setMoms((Double) vat);
    }

    /**
     * @return the ftax
     */
    public boolean isFtax() {
        return ftax;
    }

    /**
     * @param ftax the ftax to set
     */
    public void setFtax(boolean ftax) {
        this.ftax = ftax;
    }

    /**
     * @return the momsNumber
     */
    public String getMomsNumber() {
        return momsNumber;
    }

    /**
     * @return the moms
     */
    public Double getMoms() {
        return moms;
    }

    /**
     * @param moms the moms to set
     */
    public void setMoms(Double moms) {
        this.moms = moms;
    }

    /**
     * @param prod the prod to set
     */
    public void setProd(List<Product> prod) {
        this.prod = prod;
    }

    /**
     * @param countList the countList to set
     */
    public void setCountList(List<Integer> countList) {
        this.countList = countList;
    }
}

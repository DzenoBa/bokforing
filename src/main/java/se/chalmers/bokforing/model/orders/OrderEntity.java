/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.orders;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.beans.factory.annotation.Autowired;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;
import se.chalmers.bokforing.service.FakturaService;

/**
 *
 * @author victor
 */
@Entity
public class OrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderEntityId;

    @ManyToOne
    private UserInfo seller;
    @ManyToOne
    private Customer buyer;

    @OneToMany(mappedBy = "orderEntity")
    private final List<Faktura> fakturas = new LinkedList<>();

    @ManyToMany
    private final List<Product> prod = new LinkedList<>();

    @ElementCollection
    private final List<Integer> countList = new LinkedList<>();

    public void setFskatt(boolean fskatt) {
        this.fskatt = fskatt;
    }

    public void setMomsRegistredNumber(String momsRegistredNumber) {
        this.momsRegistredNumber = momsRegistredNumber;
    }

    public void setMomsPrecentage(Double momsPrecentage) {
        this.momsPrecentage = momsPrecentage;
    }

    public List<Product> getProd() {
        return prod;
    }

    public List<Integer> getCountList() {
        return countList;
    }

    public boolean isFskatt() {
        return fskatt;
    }

    public String getMomsRegistredNumber() {
        return momsRegistredNumber;
    }

    public Double getMomsPrecentage() {
        return momsPrecentage;
    }

    private boolean fskatt = false;
    private String momsRegistredNumber;
    private Double momsPrecentage;

    public List<Faktura> getFakturas() {
        return fakturas;
    }

    public Long getOrderEntityId() {
        return orderEntityId;
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
        seller = uh.getUI();
    }

    public void setBuyer(Customer cus) {
        buyer = cus;
    }

    public void addFaktura(Faktura fak) {
        fakturas.add(fak);
    }

    public void addProduct(Product p) {
        addProduct(p, 1);
    }

    public void addProduct(Product p, Integer amount) {
        if (prod.contains(p)) {
            int i = prod.indexOf(p);
            int x = countList.get(i);
            x += amount;
            if (x > 0) {
                countList.set(i, x);
            } else {
                removeAllOfProduct(p);
            }
        } else {
            prod.add(p);
            countList.add(amount);
        }
    }

    public void removeProduct(Product p) {
        addProduct(p, -1);
    }

    public void removeProduct(Product p, Integer amount) {
        addProduct(p, -amount);
    }

    public void removeAllOfProduct(Product p) {
        int i = prod.indexOf(p);
        prod.remove(i);
        countList.remove(i);
    }
}

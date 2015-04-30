/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.orders;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;

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
    
    public Long getOrderEntityId(){
        return orderEntityId;
    }
    
    public UserInfo getSeller(){
        return seller;
    }
    
    public Customer getBuyer(){
        return buyer;
    }
    
    public void setSeller(UserInfo ui){
        seller = ui;
    }
    
    public void setSeller(UserHandler uh){
        seller = uh.getUI();
    }
    public void setBuyer(Customer cus){
        buyer = cus;
    }
    
    public void addFaktura(Faktura fak){
        fakturas.add(fak);
    }
    public List<Faktura> getFaktura(){
        return fakturas;
    }
}

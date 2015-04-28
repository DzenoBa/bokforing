/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.user;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.Timesheet;
import se.chalmers.bokforing.model.Verification;
import se.chalmers.bokforing.util.PasswordUtil;

/**
 *
 * @author victor
 */
@Entity
public class UserAccount implements Serializable {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userInfoId")
    private UserInfo userInfo;
    
    /** The password of the user */
    private String pass;
    
    private String salt = PasswordUtil.randomString(8);
    
    /** The unique email of the users */
    @Column(unique = true)
    private String email;
    
    //Group is a reserved word. We cannot have it as a column name.   
    @Enumerated(EnumType.STRING)
    private UserGroup userGroup = UserGroup.User;
   
    private String sessionid;
    
    @OneToMany(mappedBy = "userAccount")
    private List<Verification> verifications;
    
    @OneToMany(mappedBy = "userAccount")
    private List<Customer> customers;
    
    @OneToMany(mappedBy = "userAccount")
    private List<Timesheet> timesheets;
    
    @OneToMany(mappedBy = "userAccount")
    private List<Product> products;
    
    @OneToMany
    private Set<Account> favoriteAccounts;
    
    protected UserAccount(){
        
    }
    public Long getId(){
        return id;
    }
    
    void setUserInfo(UserInfo userInfo){
        this.userInfo = userInfo;
    }
    /**
     * @return the name
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    void setPass(String pass) {
        this.pass = pass;
    }
    
    void setSalt(String salt){
        this.salt = salt;
    }
    /**
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    void setEmail(String email) {
        email = email.toLowerCase();
        this.email = email;
    }

    /**
     * @return String session id
     */
    public String getSessionid() {
        return sessionid;
    }
    void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    /**
     * @return the verifications
     */
    public List<Verification> getVerifications() {
        return verifications;
    }

    /**
     * @param verifications the verifications to set
     */
    void setVerifications(List<Verification> verifications) {
        this.verifications = verifications;
    }

    /**
     * @param id the id to set
     */
    void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the userGroup
     */
    public UserGroup getUserGroup() {
        return userGroup;
    }

    /**
     * @param userGroup the userGroup to set
     */
    void setUserGroup(UserGroup userGroup) {
        if(userGroup == null)
            throw new IllegalArgumentException("null is not allowed");
        this.userGroup = userGroup;
    }

    /**
     * @return the customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * @param customers the customers to set
     */
    void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
    String toStringLight(){
        return getId() + ":" + getEmail();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserEnt{ id= ").append(getId());
        sb.append(", userInfo=");
        if(getUserInfo() != null)
            sb.append(getUserInfo().toStringLight());
        else
            sb.append("null");
        sb.append(", pass=").append(getPass());
        sb.append(", salt= ").append(getSalt());
        sb.append(", email=").append(getEmail());
        sb.append(", group=").append(getUserGroup());
        sb.append(", sessionid=").append(getSessionid());
        sb.append(" }");
        return  sb.toString();  
    }
    
    @Override
    public boolean equals(Object obj){
    if(obj == this)
            return true;
    else if((obj == null) || (obj.getClass() != this.getClass()))
        return false;
    else{
        UserAccount other = (UserAccount) obj; 
            return (getId() == null ? other.getId() == null : getId().equals(other.getId()))
                && (getEmail() == null ? other.getEmail() == null : getEmail().equals(other.getEmail()))
                && (getPass() == null ? other.getPass() == null : getPass().equals(other.getPass()))
                && (getSalt() == null ? other.getSalt() == null : getSalt().equals(other.getSalt()))
                && (getSessionid() == null ? other.getSessionid() == null : getSessionid().equals(other.getSessionid()))
                && (getUserGroup() == null ? other.getUserGroup() == null : getUserGroup().equals(other.getUserGroup()))
                //&& (userInfo == other.userInfo || (userInfo != null && userInfo.equals(other.userInfo)))
                //&& (verifications == null ? other.verifications == null : verifications.equals(other.verifications))
                //&& (customers == null ? other.customers == null : customers.equals(other.customers))
                ;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.getId());
        hash = 83 * hash + Objects.hashCode(this.getUserInfo());
        hash = 83 * hash + Objects.hashCode(this.getPass());
        hash = 83 * hash + Objects.hashCode(this.getSalt());
        hash = 83 * hash + Objects.hashCode(this.getEmail());
        hash = 83 * hash + Objects.hashCode(this.getUserGroup());
        hash = 83 * hash + Objects.hashCode(this.getSessionid());
        hash = 83 * hash + Objects.hashCode(this.getVerifications());
        hash = 83 * hash + Objects.hashCode(this.getCustomers());
        return hash;
    }

    /**
     * @return the timesheets
     */
    public List<Timesheet> getTimesheets() {
        return timesheets;
    }

    /**
     * @param timesheets the timesheets to set
     */
    public void setTimesheets(List<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }

    /**
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * @return the favoriteAccounts
     */
    public Set<Account> getFavoriteAccounts() {
        return favoriteAccounts;
    }

    /**
     * @param favoriteAccounts the favoriteAccounts to set
     */
    public void setFavoriteAccounts(Set<Account> favoriteAccounts) {
        this.favoriteAccounts = favoriteAccounts;
    }
}

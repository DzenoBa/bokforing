/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.user;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import se.chalmers.bokforing.model.Address;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Verification;

/**
 *
 * @author victor
 */
public class UserHandler {
    private final UserAccount ua;
    private final UserInfo ui;
    
    private boolean modUA = false;
    private boolean modUI = false;
    
    public UserHandler(){
        ua = new UserAccount();
        ui = new UserInfo();
        link();
        newInfo();
    }
    
    private void link(){
        ua.setUserInfo(ui);
        ui.setUa(ua);
    }
    
    public UserHandler(final UserAccount ua){
        this.ua = ua;
        if(ua.getUserInfo()!=null)
            this.ui = ua.getUserInfo();
        else{
            ui = new UserInfo();
            link();
            newInfo();
        }
    }
    
    private void newInfo(){
        modUA = true;
        modUI = true;
    }
    /*
    public UserHandler(final UserInfo ui){
        this.ui = ui;
        if(ui.getUa() != null)
            this.ua = ui.getUa();
        else{
            throw new NullPointerException();
        }
    }*/
    
    //************
    //UserAccount functions
    //************
    public void setEmail(String email){
        if(email == null || email.equals("")){
            throw new IllegalArgumentException("That is not a valid email");
        }
        if(ua.getEmail() == null || !ua.getEmail().equalsIgnoreCase(email)){
            ua.setEmail(email);
            modUA = true;
        }
    }
    public String getEmail(){
        return ua.getEmail();
    }
    
    public void setPass(String pass){
        if(ua.getPass()== null || !ua.getPass().equals(pass)){
            ua.setPass(pass);
            modUA = true;
        }
    }
    public String getPass(){
        return ua.getPass();
    }
    
    public void setSalt(String salt){
        if(ua.getSalt()== null || !ua.getSalt().equals(salt)){
        ua.setSalt(salt);
        modUA = true;
        }
    }
    public String getSalt(){
        return ua.getSalt();
    }
    
    public void setUserGroup(UserGroup group){
        if(ua.getUserGroup() == null || !ua.getUserGroup().equals(group)){
            ua.setUserGroup(group);
            modUA = true;
        }
    }
    public UserGroup getUserGroup() {
        return ua.getUserGroup();
    }
    
    public void setVerifications(List<Verification> verifications){
        if(ua.getVerifications()== null || !ua.getVerifications().equals(verifications)){
        ua.setVerifications(verifications);
        modUA=true;
    }
    }
    public List<Verification> getVerifications(){
        return ua.getVerifications();
    }
    
    public void setSessionid(String sessionid){
        if(ua.getSessionid() == null || !ua.getSessionid().equals(sessionid)){
        ua.setSessionid(sessionid);
        modUA = true;
        }
    }
    public String getSessionid() {
        return ua.getSessionid();
    }
    
    void setCustomers(List<Customer> customers) {
        if(ua.getCustomers()==null || !ua.getCustomers().equals(customers)){
        ua.setCustomers(customers);
        modUA = true;
        }
    }
    public List<Customer> getCustomers() {
        return ua.getCustomers();
    }
    
    //************
    //UserInfo
    //************
    public void setName(String name){
        if(ui.getName() == null || !name.equals(ui.getName())){
            ui.setName(name);
            modUI = true;
        }
    }
    public String getName(){
        return ui.getName();
    }
    
    public void setPhoneNumber(String number){
        if(ui.getPhoneNumber() == null || !number.equals(ui.getPhoneNumber())){
            ui.setPhoneNumber(number);
            modUI = true;
        }
    }
    public String getPhoneNumber(){
        return ui.getPhoneNumber();
    }
    
    public void setAddress(Address adr){
        if(ui.getAddress()== null || !adr.equals(ui.getAddress())){
            ui.setAddress(adr);
            modUI = true;
        }
    }
    
    public Address getAddress(){
        return ui.getAddress();
    }
    
    public void setBankgiro(String adr){
        if(ui.getBankgiro() == null || !adr.equals(ui.getBankgiro())){
            ui.setBankgiro(adr);
            modUI = true;
        }
    }
    public String getBankgiro(){
        return ui.getBankgiro();
    }

public void setLogo(final URI logo){
    if(ui.getLogo() == null || !logo.equals(ui.getLogo())){
        ui.setLogo(logo);
        modUI = true;
    }
}
public URI getLogo(){
    return ui.getLogo();
}

public void setLastLogIn(){
    ui.setLastLogIn();
    modUI = true;
}

    //************
    //UserHandler
    //************
    
    public UserAccount getUA(){
        return ua;
    }
    public UserInfo getUI(){
        return ui;
    }
    
    public void resetMod(){
        modUA = false;
        modUI = false;
    }
    public boolean getModifedUser(){
        return modUA;
    }
    public boolean getModifedInfo(){
        return modUI;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(ua.toString());
        sb.append("\n");
        sb.append(ui.toString());
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj){
    if(obj == this)
        return true;
    else if((obj == null) || (obj.getClass() != this.getClass()))
        return false;
    else{
        UserHandler other = (UserHandler) obj;
        return this.ua.equals(other.ua);
    }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.ua);
        hash = 89 * hash + Objects.hashCode(this.ui);
        return hash;
    }
}

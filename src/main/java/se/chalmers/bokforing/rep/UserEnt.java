/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.rep;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author victor
 */
@Entity
public class UserEnt{
    @Id
    @GeneratedValue
    private Integer id;
    private Integer id2;
    private String name;
    private String pass;
    private String email;
    private String group;//Group String? Maybe we should define our own Group class. But string will do for now
    private Date lastLogIn;
    
    void setId(int i){
        id=i;
    }
    Integer getId(){
        return id;
    }
    void setSesId(int i){
        id2 = i;
    }
    Integer getSesId(){
        return id2;
    }
    void setName(String n){
        name = n;
    }
    String getName(){
        return name;
    }
    void setPass(String p){
        pass = p;
    }
    String getPass(){
        return pass;
    }
    void setEmail(String em){
        email = em;
    }
    String getEmail(){
        return email;
    }
    void setGroup(String gr){
        group = gr;
    }
    String getGroup(){
        return group;
    }
    void updateDate(){
	Calendar calendar = new GregorianCalendar();
        lastLogIn = calendar.getTime();
    }
    Date getDate(){
        return lastLogIn;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: " + id.toString() + "\n");
        sb.append("Session Id: " + id2.toString() + "\n");
        sb.append("Name: " + name + "\n");
        sb.append("Email: " + email + "\n");
        sb.append("Group: " + group + "\n");
        sb.append("Last login: " + lastLogIn.toString());
        return sb.toString();
    }
    
}

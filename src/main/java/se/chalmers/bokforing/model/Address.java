/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author Jakob
 */
@Entity
public class Address {
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String streetNameAndNumber;
    private String postalCode;
    private String postTown;
    private String country;

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
     * @return the streetNameAndNumber
     */
    public String getStreetNameAndNumber() {
        return streetNameAndNumber;
    }

    /**
     * @param streetNameAndNumber the streetNameAndNumber to set
     */
    public void setStreetNameAndNumber(String streetNameAndNumber) {
        this.streetNameAndNumber = streetNameAndNumber;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the postTown
     */
    public String getPostTown() {
        return postTown;
    }

    /**
     * @param postTown the postTown to set
     */
    public void setPostTown(String postTown) {
        this.postTown = postTown;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
    
}

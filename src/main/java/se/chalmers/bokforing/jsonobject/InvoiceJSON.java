
package se.chalmers.bokforing.jsonobject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Dženan
 */
public class InvoiceJSON implements Serializable {
    
    private Long id;
    private CustomerJSON customer;
    private List<ProductJSON> productls;
    private boolean ftax;
    private String vatnumber;
    private double vat;
    private Date creationdate;
    private Date expiredate;
    private int startrange;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public CustomerJSON getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerJSON customer) {
        this.customer = customer;
    }
    
    public List<ProductJSON> getProductls() {
        return productls;
    }
    
    public void setProductls(List<ProductJSON> productls) {
        this.productls = productls;
    }
    
    public boolean getFtax() {
        return ftax;
    }
    
    public void setFtax(boolean ftax) {
        this.ftax = ftax;
    }
    
    public String getVatnumber() {
        return vatnumber;
    }
    
    public void setVatnumber(String vatnumber) {
        this.vatnumber = vatnumber;
    }
    
    public double getVat() {
        return vat;
    }
    
    public void setVat(double vat) {
        this.vat = vat;
    }
    
    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }
    
    public Date getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(Date expiredate) {
        this.expiredate = expiredate;
    }
    
    public int getStartrange() {
        return startrange;
    }

    public void setStartragne(int startrange) {
        this.startrange = startrange;
    }
}

package se.chalmers.bokforing.jsonobject;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Dženan
 */
public class TimesheetJSON implements Serializable {

    private Long id;
    private ProductJSON product;
    private CustomerJSON customer;
    private Double quantity;
    private String description;
    private Date date;
    private int startrange;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductJSON getProduct() {
        return product;
    }

    public void setProduct(ProductJSON product) {
        this.product = product;
    }

    public CustomerJSON getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerJSON customer) {
        this.customer = customer;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStartrange() {
        return startrange;
    }

    public void setStartragne(int startrange) {
        this.startrange = startrange;
    }
}

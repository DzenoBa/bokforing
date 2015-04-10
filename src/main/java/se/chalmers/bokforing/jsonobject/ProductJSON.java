
package se.chalmers.bokforing.jsonobject;

import se.chalmers.bokforing.model.Product.QuantityType;

/**
 *
 * @author Dženan
 */
public class ProductJSON {
    
    private String name;
    private String description;
    private double price;
    private String quantitytype;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getQuantitytype() {
        return quantitytype;
    }
    public void setQuantitytype(String quantitytype) {
        this.quantitytype = quantitytype;
    }
}

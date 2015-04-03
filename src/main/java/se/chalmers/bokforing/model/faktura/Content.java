/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.faktura;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * This is a very easy mockup code. I don't know if this information is stored elsewhere.
 * But I just wanted something to work with.
 * It got bugs.
 * @author victor
 */
public class Content {
    private final LinkedList<Product> list = new LinkedList<>();
    
    void addProduct(Product add){
        list.add(add);
    }
    
    public String recipt(){
        StringBuilder sb = new StringBuilder();
        Iterator<Product> it = list.iterator();
        while(it.hasNext()){
            Product cur = it.next();
            sb.append("#").append(cur.units);
            sb.append(" ").append(cur.name);
            sb.append(" ").append(cur.price);
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public Float getTotalPrice(){
        Float total = 0.0f;
        Iterator<Product> it = list.iterator();
        while(it.hasNext()){
            Product cur = it.next();
            total = cur.getPrice() * cur.getUnits();
        }
        return total;
    }
    
    class Product{
        private final String name;
        private final Float price;
        private int units;
        Product(String name, Float price, int units){
            this.name = name;
            this.price = price;
            this.units = units;
        }
        
        public int addUnits(int i){
            return units += i;
        }
        public int removeUnits(int i){
            return addUnits(-i);
        }
        
        public String getName(){
            return name;
        }
        public Float getPrice(){
            return price;
        }
        public int getUnits(){
            return units;
        }
    }
}

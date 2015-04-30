/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model.orders;

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
    
    public void addProduct(String str, Double price){
        addProduct(str,price,1);
    }
    
    public void addProduct(String str, Double price, Integer amount){
        list.add(new Product(str,price,amount));
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
    
    public Iterator<Product> getIterator(){
        return list.iterator();
    }
    
    public Double getTotalPrice(){
        Double total = 0.0;
        Iterator<Product> it = list.iterator();
        while(it.hasNext()){
            Product cur = it.next();
            total += cur.getPrice() * cur.getUnits();
        }
        return total;
    }
    
    public class Product{
        private final String name;
        private final Double price;
        private int units;
        Product(String name, Double price, int units){
            this.name = name;
            this.price = price;
            this.units = units;
        }
        
        int addUnits(int i){
            return units += i;
        }
        int removeUnits(int i){
            return addUnits(-i);
        }
        
        public String getName(){
            return name;
        }
        public Double getPrice(){
            return price;
        }
        public int getUnits(){
            return units;
        }
    }
}

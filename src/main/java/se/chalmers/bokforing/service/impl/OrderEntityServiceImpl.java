/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.orders.Invoice;
import se.chalmers.bokforing.model.orders.OrderEntity;
import se.chalmers.bokforing.model.user.UserHandler;
import se.chalmers.bokforing.model.user.UserInfo;
import se.chalmers.bokforing.persistence.orders.InvoiceRepository;
import se.chalmers.bokforing.persistence.orders.OrderEntityRepository;
import se.chalmers.bokforing.service.InvoiceService;
import se.chalmers.bokforing.service.OrderEntityService;

/**
 *
 * @author victor
 */
@Service
public class OrderEntityServiceImpl implements OrderEntityService {
    
    @Autowired
    private OrderEntityRepository oeRep;
    
    @Autowired
    private InvoiceRepository fakDb;
    
    @Override
    public OrderEntity getById(Long id) {
        return oeRep.findByOrderEntityId(id);
    }

    @Override
    public List<OrderEntity> findByFromUser(UserInfo fromUser) {
        return oeRep.findBySeller(fromUser);
    }

    @Override
    public void storeOrderEntity(OrderEntity oe) {
        oeRep.save(oe);
    }
    
    @Override
    public void invalidate(OrderEntity oe){
        List<Invoice> faks = oe.getInvoices();
        for (Invoice fak : faks) {
            fak.setValid(false);
        }
    }
    
    @Override
    public int generateInvoice(OrderEntity oe) {
        invalidate(oe);
        
        List<Product> prod= oe.getProd();
        List<Integer> countList = oe.getCountList();
        
        int offset = oe.getInvoices().size();
        int prodOffset = 0;
        for(Invoice fak:oe.getInvoices()){
        prodOffset += fak.Products().size();
    }
        
        int fakNum = offset - 1;
        Invoice fak;
        for (int i = 0; i < prod.size() - prodOffset; i++) {
            if (i % 15 == 0) {
                fak = new Invoice();
                fak.setOrderEntity(oe);
                oe.addFaktura(fak);
                
                Calendar cal = Calendar.getInstance();
                cal.setTime(fak.getFakturaDate());
                cal.add(Calendar.DAY_OF_MONTH, 30);
                fak.setExpireDate(cal.getTime());
                fakNum++;
                fakDb.save(fak);
                //Do the next page
            }
            System.out.println(prod.get( i).getName() + "/" +  countList.get( i));
            oe.getInvoices().get(fakNum).addProduct(prod.get(i), countList.get(i));
        }
        oeRep.save(oe);
        return fakNum + 1;
    }
    
    @Override
    public List<OrderEntity> findByToUser(Customer toUser) {
        return oeRep.findByBuyer(toUser);
    }

    @Override
    public List<OrderEntity> findByFromUser(UserHandler fromUser) {
        return oeRep.findBySeller(fromUser.getUI());
    }
    
}

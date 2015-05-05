/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.Product;
import se.chalmers.bokforing.model.Invoice;
import se.chalmers.bokforing.model.OrderEntity;
import se.chalmers.bokforing.model.UserHandler;
import se.chalmers.bokforing.model.UserInfo;
import se.chalmers.bokforing.persistence.InvoiceRepository;
import se.chalmers.bokforing.persistence.OrderEntityRepository;
import se.chalmers.bokforing.persistence.PagingAndSortingTerms;
import se.chalmers.bokforing.service.OrderEntityService;

/**
 *
 * @author victor
 */
@Service
@Transactional
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
    public Page<OrderEntity> findByFromUser(UserInfo fromUser, PagingAndSortingTerms terms) {
        PageRequest request = terms.getPageRequest();
        
        return oeRep.findBySeller(fromUser, request);
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
            oe.getInvoices().get(fakNum).addProduct(prod.get(i), countList.get(i));
        }
        oeRep.save(oe);
        return fakNum + 1;
    }
    
    @Override
    public Page<OrderEntity> findByToUser(Customer toUser, PagingAndSortingTerms terms) {
        PageRequest pageRequest = terms.getPageRequest();
        
        return oeRep.findByBuyer(toUser, pageRequest);
    }

    @Override
    public Page<OrderEntity> findByFromUser(UserHandler fromUser, PagingAndSortingTerms terms) {
        PageRequest pageRequest = terms.getPageRequest();
        
        return oeRep.findBySeller(fromUser.getUI(), pageRequest);
    }
    
}

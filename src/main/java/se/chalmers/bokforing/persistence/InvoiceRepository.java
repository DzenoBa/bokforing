package se.chalmers.bokforing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Invoice findByFakturaId(Long fakturaId);

    //Page<Invoice> findByOrderEntity_Seller_UaAndOrderEntity_Buyer(UserAccount user, Customer customer, Pageable pageable);
    
    //Page<Invoice> findByOrderEntity_Seller_Ua(UserAccount user, Pageable pageable);

}

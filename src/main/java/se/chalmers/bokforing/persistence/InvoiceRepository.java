package se.chalmers.bokforing.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.UserAccount;
import se.chalmers.bokforing.model.Invoice;
import se.chalmers.bokforing.model.OrderEntity;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Invoice findByFakturaId(Long fakturaId);

    Page<Invoice> findByOrderEntity(OrderEntity oe, Pageable pageable);

    Page<Invoice> findByOrderEntity_Seller_UaAndOrderEntity_Buyer(UserAccount user, Customer customer, Pageable pageable);

}

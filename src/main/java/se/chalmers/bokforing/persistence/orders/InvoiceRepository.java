package se.chalmers.bokforing.persistence.orders;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.orders.Invoice;
import se.chalmers.bokforing.model.orders.OrderEntity;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {


    Invoice findByFakturaId(Long fakturaId);

    List<Invoice> findByOrderEntity(OrderEntity oe);
}
  


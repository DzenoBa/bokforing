package se.chalmers.bokforing.persistence.orders;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.orders.Faktura;
import se.chalmers.bokforing.model.orders.OrderEntity;

@Repository
public interface FakturaRepository extends JpaRepository<Faktura,Long> {


    Faktura findByFakturaId(Long fakturaId);

    List<Faktura> findByOrderEntity(OrderEntity oe);
}
  


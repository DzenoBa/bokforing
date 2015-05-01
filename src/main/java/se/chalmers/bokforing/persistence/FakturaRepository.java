package se.chalmers.bokforing.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.Faktura;
import se.chalmers.bokforing.model.UserInfo;

@Repository
public interface FakturaRepository extends JpaRepository<Faktura,Long> {


    Faktura findByFakturaId(Long fakturaId);

    List<Faktura> findByFromUser(UserInfo fromUser);
    List<Faktura> findByToUser(UserInfo toUser);
}
  


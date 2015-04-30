/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence.orders;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.Customer;
import se.chalmers.bokforing.model.orders.OrderEntity;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity,Long>{
    OrderEntity findByOrderEntityId(Long OrderEntityId);

    List<OrderEntity> findBySeller(UserInfo fromUser);

    List<OrderEntity> findByBuyer(Customer toUser);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.chalmers.bokforing.model.UserInfo;

/**
 *
 * @author victor
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByUserInfoId(int id);

    List<UserInfo> findByUserName(String userName);
}

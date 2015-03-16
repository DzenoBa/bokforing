/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence.user;

import java.net.URI;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import se.chalmers.bokforing.model.user.UserInfo;

/**
 *
 * @author victor
 */
interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    UserInfo findByUserInfoId(int id);
    List<UserInfo> findByUserName(String userName);
    
    @Modifying(clearAutomatically = true)
    @Query("update UserInfo u set u.userName = ?1 where u.userInfoId = ?2")
    int updateName(String userName, int id);
    
    @Modifying(clearAutomatically = true)
    @Query("update UserInfo u set u.companyName = ?1 where u.userInfoId = ?2")
    int updateCompanyName(String companyName, int id);
    
    @Modifying(clearAutomatically = true)
    @Query("update UserInfo u set u.phoneNumber = ?1 where u.userInfoId = ?2")
    int updatePhoneNumber(String phoneNumber, int id);

    @Modifying(clearAutomatically = true)
    @Query("update UserInfo u set u.logo = ?1 where u.userInfoId = ?2")
    int updateLogo(URI logo, int id);

    @Modifying(clearAutomatically = true)
    @Query("update UserInfo u set u.lastLogIn = ?1 where u.userInfoId = ?2")
    int updateLastLogIn(Date date, int id);
}

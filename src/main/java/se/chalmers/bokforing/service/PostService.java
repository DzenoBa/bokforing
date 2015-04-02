/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.user.UserAccount;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Jakob
 */
public interface PostService {

    Map<Account, List<Post>> getGeneralLedger(UserAccount user);

    Map<Account, List<Double>> getBalanceSheet(UserAccount user, Date startDate, Date endDate, Pageable pageable);

}

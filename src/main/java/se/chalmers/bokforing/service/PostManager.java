/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;

/**
 *
 * @author Jakob
 */
public interface PostManager {

    Post createPost(PostSum sum, Account account);

}

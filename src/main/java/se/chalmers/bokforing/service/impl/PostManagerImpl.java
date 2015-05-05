/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.chalmers.bokforing.model.Account;
import se.chalmers.bokforing.model.Post;
import se.chalmers.bokforing.model.PostSum;
import se.chalmers.bokforing.service.PostManager;
import se.chalmers.bokforing.service.PostService;
import se.chalmers.bokforing.util.DateUtil;

/**
 *
 * @author Jakob
 */
@Service
public class PostManagerImpl implements PostManager {

    @Autowired
    private PostService service;

    @Override
    public Post createPost(PostSum sum, Account account) {
        Post post = new Post();

        post.setPostSum(sum);
        post.setAccount(account);
        post.setCreationDate(DateUtil.getTodaysDate());
        post.setCorrection(false);
        post.setActive(true);

        service.save(post);

        return post;
    }
}

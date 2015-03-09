/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import se.chalmers.bokforing.util.Constants;

/**
 *
 * @author Jakob
 */
public class PageRequestParser {

    public static PageRequest getPageRequest(Integer pageNumber, Boolean ascendingSort, String fieldToSortBy) {
        if (pageNumber == null) {
            pageNumber = 0;
        }

        Sort.Direction dir;
        if (ascendingSort == null) {
            dir = Constants.DEFAULT_SORT_DIRECTION;
        } else if (ascendingSort) {
            dir = Sort.Direction.ASC;
        } else {
            dir = Sort.Direction.DESC;
        }

        Sort sort = new Sort(dir, fieldToSortBy);
        PageRequest request = new PageRequest(pageNumber, Constants.DEFAULT_PAGE_SIZE, sort);
        return request;
    }
}

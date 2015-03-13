/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.persistence;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import se.chalmers.bokforing.util.Constants;

/**
 * The point of this class is to encapsulate sorting terms for a PageRequest.
 * It seemed more intuitive to put this in its own class to make creating
 * PageRequests simpler. The intended usage is to construct one of these, and
 * then to send it to a service method. The service method should then
 * use the PagingAndSortingTerms#getPageRequest method to send the paging request.
 *
 * @author Jakob
 */
public class PagingAndSortingTerms {
    
    private Integer pageNumber;
    private Boolean ascendingSort;
    private String fieldToSortBy;

    public PagingAndSortingTerms(Integer pageNumber, Boolean ascendingSort, String fieldToSortBy) {
        this.pageNumber = pageNumber;
        this.ascendingSort = ascendingSort;
        this.fieldToSortBy = fieldToSortBy;
    }
    
    public PageRequest getPageRequest() {
        Integer tempPageNumber = pageNumber;
        
        if (pageNumber == null) {
            tempPageNumber = 0;
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
        PageRequest request = new PageRequest(tempPageNumber, Constants.DEFAULT_PAGE_SIZE, sort);
        return request;
    }

    /**
     * @return the pageNumber
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * @param pageNumber the pageNumber to set
     */
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * @return the ascendingSort
     */
    public Boolean isAscendingSort() {
        return ascendingSort;
    }

    /**
     * @param ascendingSort the ascendingSort to set
     */
    public void setAscendingSort(Boolean ascendingSort) {
        this.ascendingSort = ascendingSort;
    }

    /**
     * @return the fieldToSortBy
     */
    public String getFieldToSortBy() {
        return fieldToSortBy;
    }

    /**
     * @param fieldToSortBy the fieldToSortBy to set
     */
    public void setFieldToSortBy(String fieldToSortBy) {
        this.fieldToSortBy = fieldToSortBy;
    }
        
    
}

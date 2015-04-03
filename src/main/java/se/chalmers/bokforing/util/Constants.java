/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.util;

import org.springframework.data.domain.Sort;

/**
 *
 * @author Jakob
 */
public class Constants {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int ASSET_ACCOUNTS = 1000;
    public static final int FUNDS_AND_DEBT_ACCOUNTS = 2000;
    public static final int REVENUE_ACCOUNTS = 3000;
    public static final int MATERIAL_AND_PRODUCT_COSTS_ACCOUNTS = 4000;
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

}

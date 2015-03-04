/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Jakob
 */
public class DateUtil {

    /**
     * Given two dates, checks if they are valid. This means that neither of
     * them are null, and that the later date is either the same or later than
     * the earlier date.
     *
     * @param from the earlier date
     * @param to the later date
     * @return
     */
    public static boolean isDateRangeValid(Date from, Date to) {
        return from != null && to != null && from.compareTo(to) <= 0;
    }
    
    /**
     * Note that this takes no consideration for time zones, as this application
     * is intended for use only in Sweden.
     * 
     * @param inputDate
     * @return 
     */
    public static boolean isDateBeforeToday(Date inputDate) {
        LocalDate today = LocalDate.now();
        
        // Convert the java.util.Date to java.time.LocalDate
        LocalDate date = inputDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); 

        return date.isBefore(today);
    }

}

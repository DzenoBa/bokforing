
package se.chalmers.bokforing.helperfunctions;

import java.util.Random;

/**
 * A class that contains helper functions
 * 
 * @author Dženan
 */
public class HelpY {
    
    public HelpY() {
        ;
    }
    
    public String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        String comb = "0123456789" 
                + "abcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();
        for(int i=0; i<length; i++) {
            sb.append(comb.charAt(rnd.nextInt(comb.length())));
        }
        return sb.toString();
    }
}

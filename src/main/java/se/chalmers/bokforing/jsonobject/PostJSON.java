
package se.chalmers.bokforing.jsonobject;

import java.io.Serializable;

/**
 *
 * @author Dženan
 */
public class PostJSON implements Serializable {
    
    private int accountid;
    private double debit;
    private double credit;
    
    public int getAccountid() {
        return accountid;
    }
    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }
    
    public double getDebit() {
        return debit;
    }
    public void setDebit(double debit) {
        this.debit = debit;
    }
    
    public double getCredit() {
        return credit;
    }
    public void setCredit(double credit) {
        this.credit = credit;
    }
    
}

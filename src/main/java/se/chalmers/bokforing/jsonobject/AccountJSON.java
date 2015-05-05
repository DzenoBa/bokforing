package se.chalmers.bokforing.jsonobject;

import java.io.Serializable;

/**
 *
 * @author Dženan
 */
public class AccountJSON implements Serializable {

    private static final long serialVersionUID = 1L;

    private int number;
    private String name;
    private int startrange;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartrange() {
        return startrange;
    }

    public void setStartragne(int startrange) {
        this.startrange = startrange;
    }
}

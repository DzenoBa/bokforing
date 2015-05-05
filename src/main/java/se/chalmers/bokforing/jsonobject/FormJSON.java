package se.chalmers.bokforing.jsonobject;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Dženan
 */
public class FormJSON implements Serializable {

    private static final long serialVersionUID = 1L;

    private int numErrors;
    private final HashMap<String, String> errors;

    public FormJSON() {
        numErrors = 0;
        errors = new HashMap<>();
    }

    public int getNumErrors() {
        return numErrors;
    }

    public void increaseErrors() {
        numErrors = numErrors + 1;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }

    public void addError(String name, String message) {
        errors.put(name, message);
        increaseErrors();
    }
}

package exception;

/**
 * The uppermost exception method of GADC.
 * All exceptions in this package extends this method.
 * Use this exception as a classifier about Java Exceptions and GADC Exceptions.
 * @since 1.0.0
 */
public class GADCException extends Exception{
    public GADCException() {
        super();
    }
    public GADCException(String msg) {
        super(msg);
    }
}

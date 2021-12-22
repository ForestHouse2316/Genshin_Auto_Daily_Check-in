package exception;

/**
 * Throw if there is a problem during initializing the driver
 */
public class DriverInitFailedError extends GADCException {
    public DriverInitFailedError() {
        super("Cannot continue initialization because there is a problem in driver or Chrome");
    }
    public DriverInitFailedError(String msg) {
        super(msg);
    }
}
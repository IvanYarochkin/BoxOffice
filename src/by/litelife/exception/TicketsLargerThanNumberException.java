package by.litelife.exception;

/**
 * Created by John on 14.05.2017.
 */
public class TicketsLargerThanNumberException extends SpectacleException {
    public TicketsLargerThanNumberException() {
    }

    public TicketsLargerThanNumberException(String message) {
        super(message);
    }

    public TicketsLargerThanNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketsLargerThanNumberException(Throwable cause) {
        super(cause);
    }

    public TicketsLargerThanNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

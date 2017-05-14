package by.litelife.exception;

/**
 * Created by John on 14.05.2017.
 */
public class SpectacleException extends Exception{
    public SpectacleException() {
    }

    public SpectacleException(String message) {
        super(message);
    }

    public SpectacleException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpectacleException(Throwable cause) {
        super(cause);
    }

    public SpectacleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

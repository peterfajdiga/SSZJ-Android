package peterfajdiga.sszj.obb;

public class InvalidStateException extends Exception {
    InvalidStateException(final int state) {
        super("Invalid state: " + state);
    }
}

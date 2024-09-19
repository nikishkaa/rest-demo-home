package org.example.exception;

public class JpaException extends RuntimeException {

    private static final long serialVersionUID = -1235059045244851555L;

    public JpaException(Exception cause) {
        super(cause);
    }

    public JpaException(String message) {
        super(message);
    }

    public JpaException(String message, Exception cause) {
        super(message, cause);
    }
}

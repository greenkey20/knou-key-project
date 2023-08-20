package org.knou.keyproject.global.exception;

// 2023.8.20(일) 16h30
public class ViolationException extends RuntimeException {
    private static final long serialVersionUid = 1L;

    public ViolationException() {
        super();
    }

    public ViolationException(String message) {
        super(message);
    }

    public ViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViolationException(Throwable cause) {
        super(cause);
    }
}

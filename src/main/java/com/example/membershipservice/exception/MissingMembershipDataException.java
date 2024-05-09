package com.example.membershipservice.exception;

/**
 * @author Miroslav Kološnjaji
 */
public class MissingMembershipDataException extends RuntimeException {

    public MissingMembershipDataException() {
    }

    public MissingMembershipDataException(String message) {
        super(message);
    }

    public MissingMembershipDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingMembershipDataException(Throwable cause) {
        super(cause);
    }

    public MissingMembershipDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

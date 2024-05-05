package com.example.membershipservice.exception;

/**
 * @author Miroslav Kološnjaji
 */
public class MembershipNotFoundException extends RuntimeException {

    public MembershipNotFoundException() {
    }

    public MembershipNotFoundException(String message) {
        super(message);
    }

    public MembershipNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MembershipNotFoundException(Throwable cause) {
        super(cause);
    }

    public MembershipNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

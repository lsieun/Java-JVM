package lsieun.bytecode.exceptions;

/**
 * Instances of this class are thrown by BCEL's class file verifier "JustIce"
 * whenever
 * verification proves that some constraint of a class file (as stated in the
 * Java Virtual Machine Specification, Edition 2) is violated.
 * This is roughly equivalent to the VerifyError the JVM-internal verifiers
 * throw.
 *
 */
public class VerifierConstraintViolatedException extends RuntimeException {
    /** The specified error message. */
    private String detailMessage;

    /**
     * Constructs a new VerifierConstraintViolatedException with null as its error message string.
     */
    VerifierConstraintViolatedException() {
        super();
    }

    /**
     * Constructs a new VerifierConstraintViolatedException with the specified error message.
     */
    VerifierConstraintViolatedException(final String message) {
        super(message); // Not that important
        detailMessage = message;
    }

    /**
     * Constructs a new VerifierConstraintViolationException with the specified error message and cause
     */
    VerifierConstraintViolatedException(final String message, final Throwable initCause) {
        super(message, initCause);
        detailMessage = message;
    }


    /** Extends the error message with a string before ("pre") and after ("post") the
     'old' error message. All of these three strings are allowed to be null, and null
     is always replaced by the empty string (""). In particular, after invoking this
     method, the error message of this object can no longer be null.
     */
    public void extendMessage(String pre, String post) {
        if (pre  == null) {
            pre="";
        }
        if (detailMessage == null) {
            detailMessage="";
        }
        if (post == null) {
            post="";
        }
        detailMessage = pre+detailMessage+post;
    }

    /**
     * Returns the error message string of this VerifierConstraintViolatedException object.
     * @return the error message string of this VerifierConstraintViolatedException.
     */
    @Override
    public String getMessage() {
        return detailMessage;
    }
}

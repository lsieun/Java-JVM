package lsieun.bytecode.exceptions;

/**
 * Instances of this class are thrown by BCEL's class file verifier "JustIce" when a
 * class file to verify does not pass one of the verification passes 2 or 3.
 * Note that the pass 3 used by "JustIce" involves verification that is usually
 * delayed to pass 4.
 * The name of this class is justified by the Java Virtual Machine Specification, 2nd
 * edition, page 164, 5.4.1 where verification as a part of the linking process is
 * defined to be the verification happening in passes 2 and 3.
 *
 */
public class VerificationException extends VerifierConstraintViolatedException {
    /**
     * Constructs a new VerificationException with null as its error message string.
     */
    VerificationException() {
        super();
    }

    /**
     * Constructs a new VerificationException with the specified error message.
     */
    VerificationException(final String message) {
        super(message);
    }


    /**
     * Constructs a new VerificationException with the specified error message and exception
     */
    VerificationException(final String message, final Throwable initCause) {
        super(message, initCause);
    }
}

package lsieun.bytecode.exceptions;

/**
 * Instances of this class are thrown by BCEL's class file verifier "JustIce" when
 * a class file does not pass the verification pass 3. Note that the pass 3 used by
 * "JustIce" involves verification that is usually delayed to pass 4.
 *
 */
public class CodeConstraintException extends VerificationException {
    /**
     * Constructs a new CodeConstraintException with null as its error message string.
     */
    CodeConstraintException() {
        super();
    }

    /**
     * Constructs a new CodeConstraintException with the specified error message.
     */
    CodeConstraintException(final String message) {
        super(message);
    }
}

package lsieun.bytecode.exceptions;

/**
 * Instances of this class should never be thrown. When such an instance is thrown,
 * this is due to an INTERNAL ERROR of BCEL's class file verifier &quot;JustIce&quot;.
 */
public final class AssertionViolatedException extends RuntimeException {
    /** The error message. */
    private String detailMessage;

    /** Constructs a new AssertionViolatedException with null as its error message string. */
    public AssertionViolatedException() {
        super();
    }

    /**
     * Constructs a new AssertionViolatedException with the specified error message preceded
     * by &quot;INTERNAL ERROR: &quot;.
     */
    public AssertionViolatedException(String message) {
        super(message = "INTERNAL ERROR: "+message); // Thanks to Java, the constructor call here must be first.
        detailMessage=message;
    }

    /**
     * Constructs a new AssertionViolationException with the specified error message and initial cause
     * @since 6.0
     */
    public AssertionViolatedException(String message, final Throwable initCause) {
        super(message = "INTERNAL ERROR: "+message, initCause);
        detailMessage=message;
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
     * Returns the error message string of this AssertionViolatedException object.
     * @return the error message string of this AssertionViolatedException.
     */
    @Override
    public String getMessage() {
        return detailMessage;
    }

    /**
     * DO NOT USE. It's for experimental testing during development only.
     */
    public static void main(final String[] args) {
        final AssertionViolatedException ave = new AssertionViolatedException("Oops!");
        ave.extendMessage("\nFOUND:\n\t","\nExiting!!\n");
        throw ave;
    }
}

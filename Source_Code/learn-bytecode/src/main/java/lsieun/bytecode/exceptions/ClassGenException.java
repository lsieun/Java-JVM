package lsieun.bytecode.exceptions;

/**
 * Thrown on internal errors. Extends RuntimeException so it hasn't to be declared
 * in the throws clause every time.
 *
 */
public class ClassGenException extends RuntimeException {
    public ClassGenException() {
        super();
    }


    public ClassGenException(final String s) {
        super(s);
    }

    public ClassGenException(final String s, final Throwable initCause) {
        super(s, initCause);
    }
}

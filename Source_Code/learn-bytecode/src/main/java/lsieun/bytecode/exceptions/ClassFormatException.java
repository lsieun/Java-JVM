package lsieun.bytecode.exceptions;

/**
 * Thrown when the program attempts to read a class file and determines
 * that the file is malformed or otherwise cannot be interpreted as a
 * class file.
 *
 */
public class ClassFormatException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClassFormatException() {
        super();
    }


    public ClassFormatException(final String s) {
        super(s);
    }

    public ClassFormatException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

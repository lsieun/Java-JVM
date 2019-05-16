package lsieun.bytecode.exceptions;

/**
 * Instances of this class are thrown by BCEL's class file verifier "JustIce" when
 * a class file to verify does not pass the verification pass 3 because of a violation
 * of a structural constraint as described in the Java Virtual Machine Specification,
 * 2nd edition, 4.8.2, pages 137-139.
 * Note that the notion of a "structural" constraint is somewhat misleading. Structural
 * constraints are constraints on relationships between Java virtual machine instructions.
 * These are the constraints where data-flow analysis is needed to verify if they hold.
 * The data flow analysis of pass 3 is called pass 3b in JustIce.
 *
 */
public class StructuralCodeConstraintException extends CodeConstraintException {
    /**
     * Constructs a new StructuralCodeConstraintException with the specified error message.
     */
    public StructuralCodeConstraintException(final String message) {
        super(message);
    }

    /**
     * Constructs a new StructuralCodeConstraintException with null as its error message string.
     */
    public StructuralCodeConstraintException() {
        super();
    }
}

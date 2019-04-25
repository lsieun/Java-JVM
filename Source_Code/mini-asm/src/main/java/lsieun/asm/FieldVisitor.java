package lsieun.asm;

/**
 * A visitor to visit a Java field. The methods of this class must be called in the following order:
 * ( {@code visitAnnotation} | {@code visitTypeAnnotation} | {@code visitAttribute} )* {@code
 * visitEnd}.
 *
 */
public abstract class FieldVisitor {
    /** The field visitor to which this visitor must delegate method calls. May be {@literal null}. */
    protected FieldVisitor fv;

    /**
     * Constructs a new {@link FieldVisitor}.
     *
     * @param fieldVisitor the field visitor to which this visitor must delegate method calls. May be
     *     null.
     */
    public FieldVisitor(final FieldVisitor fieldVisitor) {
        this.fv = fieldVisitor;
    }

    /**
     * Visits the end of the field. This method, which is the last one to be called, is used to inform
     * the visitor that all the annotations and attributes of the field have been visited.
     */
    public void visitEnd() {
        if (fv != null) {
            fv.visitEnd();
        }
    }

}

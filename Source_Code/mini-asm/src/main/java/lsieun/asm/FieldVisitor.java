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
     * Visits an annotation of the field.
     *
     * @param descriptor the class descriptor of the annotation class.
     * @param visible {@literal true} if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or {@literal null} if this visitor is not
     *     interested in visiting this annotation.
     */
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if (fv != null) {
            return fv.visitAnnotation(descriptor, visible);
        }
        return null;
    }

    /**
     * Visits a non standard attribute of the field.
     *
     * @param attribute an attribute.
     */
    public void visitAttribute(final Attribute attribute) {
        if (fv != null) {
            fv.visitAttribute(attribute);
        }
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

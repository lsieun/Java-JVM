package lsieun.asm;

/**
 * A visitor to visit a Java method.<br/>
 * The methods of this class must be called in the following
 * order: ( {@code visitParameter} )* [ {@code visitAnnotationDefault} ] ( {@code visitAnnotation} |
 * {@code visitAnnotableParameterCount} | {@code visitParameterAnnotation} {@code
 * visitTypeAnnotation} | {@code visitAttribute} )* [ {@code visitCode} ( {@code visitFrame} |
 * {@code visit<i>X</i>Insn} | {@code visitLabel} | {@code visitInsnAnnotation} | {@code
 * visitTryCatchBlock} | {@code visitTryCatchAnnotation} | {@code visitLocalVariable} | {@code
 * visitLocalVariableAnnotation} | {@code visitLineNumber} )* {@code visitMaxs} ] {@code visitEnd}.
 * In addition, the {@code visit<i>X</i>Insn} and {@code visitLabel} methods must be called in the
 * sequential order of the bytecode instructions of the visited code, {@code visitInsnAnnotation}
 * must be called <i>after</i> the annotated instruction, {@code visitTryCatchBlock} must be called
 * <i>before</i> the labels passed as arguments have been visited, {@code
 * visitTryCatchBlockAnnotation} must be called <i>after</i> the corresponding try catch block has
 * been visited, and the {@code visitLocalVariable}, {@code visitLocalVariableAnnotation} and {@code
 * visitLineNumber} methods must be called <i>after</i> the labels passed as arguments have been
 * visited.
 *
 */
public abstract class MethodVisitor {
    /**
     * The method visitor to which this visitor must delegate method calls. May be {@literal null}.
     */
    protected MethodVisitor mv;

    /**
     * Constructs a new {@link MethodVisitor}.
     *
     * @param methodVisitor the method visitor to which this visitor must delegate method calls. May
     *     be null.
     */
    public MethodVisitor(final MethodVisitor methodVisitor) {
        this.mv = methodVisitor;
    }

    // -----------------------------------------------------------------------------------------------
    // Parameters, annotations and non standard attributes
    // -----------------------------------------------------------------------------------------------

    /**
     * Visits the default value of this annotation interface method.
     *
     * @return a visitor to the visit the actual default value of this annotation interface method, or
     *     {@literal null} if this visitor is not interested in visiting this default value. The
     *     'name' parameters passed to the methods of this annotation visitor are ignored. Moreover,
     *     exacly one visit method must be called on this annotation visitor, followed by visitEnd.
     */
    public AnnotationVisitor visitAnnotationDefault() {
        if (mv != null) {
            return mv.visitAnnotationDefault();
        }
        return null;
    }

    /**
     * Visits an annotation of this method.
     *
     * @param descriptor the class descriptor of the annotation class.
     * @param visible {@literal true} if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or {@literal null} if this visitor is not
     *     interested in visiting this annotation.
     */
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if (mv != null) {
            return mv.visitAnnotation(descriptor, visible);
        }
        return null;
    }

    /**
     * Visits the number of method parameters that can have annotations. By default (i.e. when this
     * method is not called), all the method parameters defined by the method descriptor can have
     * annotations.
     *
     * @param parameterCount the number of method parameters than can have annotations. This number
     *     must be less or equal than the number of parameter types in the method descriptor. It can
     *     be strictly less when a method has synthetic parameters and when these parameters are
     *     ignored when computing parameter indices for the purpose of parameter annotations (see
     *     https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.18).
     * @param visible {@literal true} to define the number of method parameters that can have
     *     annotations visible at runtime, {@literal false} to define the number of method parameters
     *     that can have annotations invisible at runtime.
     */
    public void visitAnnotableParameterCount(final int parameterCount, final boolean visible) {
        if (mv != null) {
            mv.visitAnnotableParameterCount(parameterCount, visible);
        }
    }

    /**
     * Visits an annotation of a parameter this method.
     *
     * @param parameter the parameter index. This index must be strictly smaller than the number of
     *     parameters in the method descriptor, and strictly smaller than the parameter count
     *     specified in {@link #visitAnnotableParameterCount}. Important note: <i>a parameter index i
     *     is not required to correspond to the i'th parameter descriptor in the method
     *     descriptor</i>, in particular in case of synthetic parameters (see
     *     https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.18).
     * @param descriptor the class descriptor of the annotation class.
     * @param visible {@literal true} if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or {@literal null} if this visitor is not
     *     interested in visiting this annotation.
     */
    public AnnotationVisitor visitParameterAnnotation(
            final int parameter, final String descriptor, final boolean visible) {
        if (mv != null) {
            return mv.visitParameterAnnotation(parameter, descriptor, visible);
        }
        return null;
    }

    /**
     * Visits a non standard attribute of this method.
     *
     * @param attribute an attribute.
     */
    public void visitAttribute(final Attribute attribute) {
        if (mv != null) {
            mv.visitAttribute(attribute);
        }
    }

    /** Starts the visit of the method's code, if any (i.e. non abstract method). */
    public void visitCode() {
        if (mv != null) {
            mv.visitCode();
        }
    }

    /**
     * Visits the maximum stack size and the maximum number of local variables of the method.
     *
     * @param maxStack maximum stack size of the method.
     * @param maxLocals maximum number of local variables for the method.
     */
    public void visitMaxs(final int maxStack, final int maxLocals) {
        if (mv != null) {
            mv.visitMaxs(maxStack, maxLocals);
        }
    }

    /**
     * Visits the end of the method. This method, which is the last one to be called, is used to
     * inform the visitor that all the annotations and attributes of the method have been visited.
     */
    public void visitEnd() {
        if (mv != null) {
            mv.visitEnd();
        }
    }
}

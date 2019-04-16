package lsieun.asm;

/**
 * A visitor to visit a Java class.<br/>
 * The methods of this class must be called in the following order:
 * {@code visit} [ {@code visitSource} ] [ {@code visitModule} ][ {@code visitNestHost} ][ {@code
 * visitOuterClass} ] ( {@code visitAnnotation} | {@code visitTypeAnnotation} | {@code
 * visitAttribute} )* ( {@code visitNestMember} | {@code visitInnerClass} | {@code visitField} |
 * {@code visitMethod} )* {@code visitEnd}.
 *
 */
public abstract class ClassVisitor {
    /** The class visitor to which this visitor must delegate method calls. May be {@literal null}. */
    protected ClassVisitor cv;

    /**
     * Constructs a new {@link ClassVisitor}.
     *
     * @param classVisitor the class visitor to which this visitor must delegate method calls. May be null.
     */
    public ClassVisitor(final ClassVisitor classVisitor) {
        this.cv = classVisitor;
    }

    /**
     * Visits the header of the class.
     *
     * @param version the class version. The minor version is stored in the 16 most significant bits,
     *     and the major version in the 16 least significant bits.
     * @param access the class's access flags (see {@link Opcodes}). This parameter also indicates if
     *     the class is deprecated.
     * @param name the internal name of the class (see {@link Type#getInternalName()}).
     * @param signature the signature of this class. May be {@literal null} if the class is not a
     *     generic one, and does not extend or implement generic classes or interfaces.
     * @param superName the internal of name of the super class (see {@link Type#getInternalName()}).
     *     For interfaces, the super class is {@link Object}. May be {@literal null}, but only for the
     *     {@link Object} class.
     * @param interfaces the internal names of the class's interfaces (see {@link
     *     Type#getInternalName()}). May be {@literal null}.
     */
    public void visit(
            final int version,
            final int access,
            final String name,
            final String signature,
            final String superName,
            final String[] interfaces) {
        if (cv != null) {
            cv.visit(version, access, name, signature, superName, interfaces);
        }
    }

    /**
     * Visits the source of the class.
     *
     * @param source the name of the source file from which the class was compiled. May be {@literal
     *     null}.
     * @param debug additional debug information to compute the correspondence between source and
     *     compiled elements of the class. May be {@literal null}.
     */
    public void visitSource(final String source, final String debug) {
        if (cv != null) {
            cv.visitSource(source, debug);
        }
    }

    /**
     * Visits an annotation of the class.
     *
     * @param descriptor the class descriptor of the annotation class.
     * @param visible {@literal true} if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or {@literal null} if this visitor is not
     *     interested in visiting this annotation.
     */
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if (cv != null) {
            return cv.visitAnnotation(descriptor, visible);
        }
        return null;
    }

    /**
     * Visits a non standard attribute of the class.
     *
     * @param attribute an attribute.
     */
    public void visitAttribute(final Attribute attribute) {
        if (cv != null) {
            cv.visitAttribute(attribute);
        }
    }

    /**
     * Visits a field of the class.
     *
     * @param access the field's access flags (see {@link Opcodes}). This parameter also indicates if
     *     the field is synthetic and/or deprecated.
     * @param name the field's name.
     * @param descriptor the field's descriptor (see {@link Type}).
     * @param signature the field's signature. May be {@literal null} if the field's type does not use
     *     generic types.
     * @param value the field's initial value. This parameter, which may be {@literal null} if the
     *     field does not have an initial value, must be an {@link Integer}, a {@link Float}, a {@link
     *     Long}, a {@link Double} or a {@link String} (for {@code int}, {@code float}, {@code long}
     *     or {@code String} fields respectively). <i>This parameter is only used for static
     *     fields</i>. Its value is ignored for non static fields, which must be initialized through
     *     bytecode instructions in constructors or methods.
     * @return a visitor to visit field annotations and attributes, or {@literal null} if this class
     *     visitor is not interested in visiting these annotations and attributes.
     */
    public FieldVisitor visitField(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final Object value) {
        if (cv != null) {
            return cv.visitField(access, name, descriptor, signature, value);
        }
        return null;
    }

    /**
     * Visits a method of the class. This method <i>must</i> return a new {@link MethodVisitor}
     * instance (or {@literal null}) each time it is called, i.e., it should not return a previously
     * returned visitor.
     *
     * @param access the method's access flags (see {@link Opcodes}). This parameter also indicates if
     *     the method is synthetic and/or deprecated.
     * @param name the method's name.
     * @param descriptor the method's descriptor (see {@link Type}).
     * @param signature the method's signature. May be {@literal null} if the method parameters,
     *     return type and exceptions do not use generic types.
     * @param exceptions the internal names of the method's exception classes (see {@link
     *     Type#getInternalName()}). May be {@literal null}.
     * @return an object to visit the byte code of the method, or {@literal null} if this class
     *     visitor is not interested in visiting the code of this method.
     */
    public MethodVisitor visitMethod(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions) {
        if (cv != null) {
            return cv.visitMethod(access, name, descriptor, signature, exceptions);
        }
        return null;
    }

    /**
     * Visits the end of the class. This method, which is the last one to be called, is used to inform
     * the visitor that all the fields and methods of the class have been visited.
     */
    public void visitEnd() {
        if (cv != null) {
            cv.visitEnd();
        }
    }
}

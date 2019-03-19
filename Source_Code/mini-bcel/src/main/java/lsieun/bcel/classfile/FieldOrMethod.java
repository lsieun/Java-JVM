package lsieun.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.attributes.AnnotationEntry;
import lsieun.bcel.classfile.attributes.Attribute;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;
import lsieun.bcel.exceptions.ClassFormatException;

/**
 * Abstract super class for fields and methods.
 *
 */
public abstract class FieldOrMethod implements Node {
    private int access_flags;
    private int name_index;
    private int signature_index;
    private ConstantPool constant_pool;

    private String signatureAttributeString = null;

    private int attributes_count;
    private Attribute[] attributes;

    private AnnotationEntry[] annotationEntries;

    FieldOrMethod() {
    }

    /**
     * @param access_flags Access rights of method
     * @param name_index Points to field name in constant pool
     * @param signature_index Points to encoded signature
     * @param attributes Collection of attributes
     * @param constant_pool Array of constants
     */
    protected FieldOrMethod(final int access_flags, final int name_index, final int signature_index,
                            final Attribute[] attributes, final ConstantPool constant_pool) {
        this.name_index = name_index;
        this.signature_index = signature_index;
        this.constant_pool = constant_pool;
        setAttributes(attributes);
    }

    /**
     * Construct object from file stream.
     * @param file Input stream
     * @throws IOException
     * @throws ClassFormatException
     */
    protected FieldOrMethod(final DataInput file, final ConstantPool constant_pool) throws IOException, ClassFormatException {
        this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), null, constant_pool);
        final int attributes_count = file.readUnsignedShort();
        attributes = new Attribute[attributes_count];
        for (int i = 0; i < attributes_count; i++) {
            attributes[i] = Attribute.readAttribute(file, constant_pool);
        }
        this.attributes_count = attributes_count;
    }

    public int getAccessFlags() {
        return access_flags;
    }

    /**
     * @return Index in constant pool of object's name.
     */
    public final int getNameIndex() {
        return name_index;
    }

    /**
     * @return Index in constant pool of field signature.
     */
    public final int getSignatureIndex() {
        return signature_index;
    }

    /**
     * @return Constant pool used by this object.
     */
    public final ConstantPool getConstantPool() {
        return constant_pool;
    }

    /**
     * @return Name of object, i.e., method name or field name
     */
    public final String getName() {
        ConstantUtf8 c;
        c = (ConstantUtf8) constant_pool.getConstant(name_index, CPConst.CONSTANT_Utf8);
        return c.getBytes();
    }

    /**
     * @return String representation of object's type signature (java style)
     */
    public final String getSignature() {
        ConstantUtf8 c;
        c = (ConstantUtf8) constant_pool.getConstant(signature_index, CPConst.CONSTANT_Utf8);
        return c.getBytes();
    }

    /**
     * @return Collection of object attributes.
     */
    public final Attribute[] getAttributes() {
        return attributes;
    }

    /**
     * @param attributes Collection of object attributes.
     */
    public final void setAttributes(final Attribute[] attributes) {
        this.attributes = attributes;
        this.attributes_count = attributes != null ? attributes.length : 0; // init deprecated field
    }

    /**
     * @return Annotations on the field or method
     * @since 6.0
     */
    public AnnotationEntry[] getAnnotationEntries() {
        if (annotationEntries == null) {
            annotationEntries = AnnotationEntry.createAnnotationEntries(getAttributes());
        }

        return annotationEntries;
    }
}

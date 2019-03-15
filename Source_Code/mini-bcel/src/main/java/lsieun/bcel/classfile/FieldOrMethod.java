package lsieun.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.attributes.Attribute;
import lsieun.bcel.exceptions.ClassFormatException;

/**
 * Abstract super class for fields and methods.
 *
 */
public abstract class FieldOrMethod implements Node {
    private int name_index;
    private int signature_index;
    private ConstantPool constant_pool;

    private String signatureAttributeString = null;

    private int attributes_count;
    private Attribute[] attributes;

    // FIXME: 这个，我还不知道存储在哪里呢
    //private AnnotationEntry[] annotationEntries;

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
}

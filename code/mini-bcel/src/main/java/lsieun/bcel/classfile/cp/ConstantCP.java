package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

/**
 * Abstract super class for Fieldref, Methodref, InterfaceMethodref,
 * Dynamic and InvokeDynamic constants.
 *
 *
 * @see     ConstantFieldref
 * @see     ConstantMethodref
 * @see     ConstantInterfaceMethodref
 * @see     ConstantDynamic
 * @see     ConstantInvokeDynamic
 */
public abstract class ConstantCP extends Constant {
    /** References to the constants containing the class and the field signature
     */
    // Note that this field is used to store the
    // bootstrap_method_attr_index of a ConstantInvokeDynamic.
    private int class_index;

    // This field has the same meaning for all subclasses.
    protected int name_and_type_index;

    /**
     * @param class_index Reference to the class containing the field
     * @param name_and_type_index and the field signature
     */
    protected ConstantCP(final byte tag, final int class_index, final int name_and_type_index) {
        super(tag);
        this.class_index = class_index;
        this.name_and_type_index = name_and_type_index;
    }

    /**
     * Initialize instance from file data.
     *
     * @param tag  Constant type tag
     * @param file Input stream
     * @throws IOException
     */
    ConstantCP(final byte tag, final DataInput file) throws IOException {
        this(tag, file.readUnsignedShort(), file.readUnsignedShort());
    }

    /**
     * @return Reference (index) to class this constant refers to.
     */
    public final int getClassIndex() {
        return class_index;
    }

    /**
     * @return Reference (index) to signature of the field.
     */
    public final int getNameAndTypeIndex() {
        return name_and_type_index;
    }

    /**
     * @return String representation.
     *
     * not final as ConstantInvokeDynamic needs to modify
     */
    @Override
    public String toString() {
        return super.toString() + "(class_index = " + class_index + ", name_and_type_index = "
                + name_and_type_index + ")";
    }
}

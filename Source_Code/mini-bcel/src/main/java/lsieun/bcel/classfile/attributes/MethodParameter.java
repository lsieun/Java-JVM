package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.AccessFlag;
import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;

/**
 * Entry of the parameters table.
 *
 * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.24">
 * The class File Format : The MethodParameters Attribute</a>
 *
 */
public class MethodParameter {

    /** Index of the CONSTANT_Utf8_info structure in the constant_pool table representing the name of the parameter */
    private int name_index;

    /** The access flags */
    private int access_flags;

    public MethodParameter() {
    }

    /**
     * Construct object from input stream.
     *
     * @param input Input stream
     * @throws java.io.IOException
     */
    MethodParameter(final DataInput input) throws IOException {
        name_index = input.readUnsignedShort();
        access_flags = input.readUnsignedShort();
    }

    public int getNameIndex() {
        return name_index;
    }

    /**
     * Returns the name of the parameter.
     */
    public String getParameterName(final ConstantPool constant_pool) {
        if (name_index == 0) {
            return null;
        }
        return ((ConstantUtf8) constant_pool.getConstant(name_index, CPConst.CONSTANT_Utf8)).getBytes();
    }

    public int getAccessFlags() {
        return access_flags;
    }

    public boolean isFinal() {
        return (access_flags & AccessFlag.FINAL) != 0;
    }

    public boolean isSynthetic() {
        return (access_flags & AccessFlag.SYNTHETIC) != 0;
    }

    public boolean isMandated() {
        return (access_flags & AccessFlag.MANDATED) != 0;
    }
}

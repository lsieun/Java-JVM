package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * This class represents a BootstrapMethods attribute.
 *
 * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.23">
 * The class File Format : The BootstrapMethods Attribute</a>
 *
 */
public class BootstrapMethods extends Attribute {
    private BootstrapMethod[] bootstrap_methods;

    /**
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param bootstrap_methods array of bootstrap methods
     * @param constant_pool Array of constants
     */
    public BootstrapMethods(final int name_index, final int length, final BootstrapMethod[] bootstrap_methods, final ConstantPool constant_pool) {
        super(AttrConst.ATTR_BOOTSTRAP_METHODS, name_index, length, constant_pool);
        this.bootstrap_methods = bootstrap_methods;
    }

    /**
     * Construct object from Input stream.
     *
     * @param name_index Index in constant pool to CONSTANT_Utf8
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     * @throws IOException
     */
    BootstrapMethods(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool) throws IOException {
        this(name_index, length, (BootstrapMethod[]) null, constant_pool);

        final int num_bootstrap_methods = input.readUnsignedShort();
        bootstrap_methods = new BootstrapMethod[num_bootstrap_methods];
        for (int i = 0; i < num_bootstrap_methods; i++) {
            bootstrap_methods[i] = new BootstrapMethod(input);
        }
    }

    /**
     * @return array of bootstrap method "records"
     */
    public final BootstrapMethod[] getBootstrapMethods() {
        return bootstrap_methods;
    }

    /**
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitBootstrapMethods(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("BootstrapMethods(");
        buf.append(bootstrap_methods.length);
        buf.append("):\n");
        for (int i = 0; i < bootstrap_methods.length; i++) {
            buf.append("  ").append(i).append(": ");
            buf.append(bootstrap_methods[i].toString(super.getConstantPool())).append("\n");
        }
        return buf.toString();
    }
}

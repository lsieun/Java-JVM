package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.Arrays;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Utility;
import lsieun.bcel.classfile.consts.CPConst;

/**
 * This class represents a bootstrap method attribute, i.e., the bootstrap
 * method ref, the number of bootstrap arguments and an array of the
 * bootstrap arguments.
 *
 * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.23">
 * The class File Format : The BootstrapMethods Attribute</a>
 */
public class BootstrapMethod {
    /** Index of the CONSTANT_MethodHandle_info structure in the constant_pool table */
    private int bootstrap_method_ref;

    /** Array of references to the constant_pool table */
    private int[] bootstrap_arguments;

    /**
     * Construct object from input stream.
     *
     * @param input Input stream
     * @throws IOException
     */
    BootstrapMethod(final DataInput input) throws IOException {
        this(input.readUnsignedShort(), input.readUnsignedShort());

        for (int i = 0; i < bootstrap_arguments.length; i++) {
            bootstrap_arguments[i] = input.readUnsignedShort();
        }
    }

    // helper method
    private BootstrapMethod(final int bootstrap_method_ref, final int num_bootstrap_arguments) {
        this(bootstrap_method_ref, new int[num_bootstrap_arguments]);
    }

    /**
     * @param bootstrap_method_ref int index into constant_pool of CONSTANT_MethodHandle
     * @param bootstrap_arguments int[] indices into constant_pool of CONSTANT_<type>_info
     */
    public BootstrapMethod(final int bootstrap_method_ref, final int[] bootstrap_arguments) {
        this.bootstrap_method_ref = bootstrap_method_ref;
        this.bootstrap_arguments = bootstrap_arguments;
    }

    /**
     * @return index into constant_pool of bootstrap_method
     */
    public int getBootstrapMethodRef() {
        return bootstrap_method_ref;
    }

    /**
     * @return int[] of bootstrap_method indices into constant_pool of CONSTANT_<type>_info
     */
    public int[] getBootstrapArguments() {
        return bootstrap_arguments;
    }

    /**
     * @return String representation.
     */
    @Override
    public final String toString() {
        return "BootstrapMethod(" + bootstrap_method_ref + ", " + bootstrap_arguments.length + ", "
                + Arrays.toString(bootstrap_arguments) + ")";
    }

    /**
     * @return Resolved string representation
     */
    public final String toString( final ConstantPool constant_pool ) {
        final StringBuilder buf = new StringBuilder();
        String bootstrap_method_name;
        bootstrap_method_name = constant_pool.constantToString(bootstrap_method_ref,
                CPConst.CONSTANT_MethodHandle);
        buf.append(Utility.compactClassName(bootstrap_method_name));
        final int num_bootstrap_arguments = bootstrap_arguments.length;
        if (num_bootstrap_arguments > 0) {
            buf.append("\n     Method Arguments:");
            for (int i = 0; i < num_bootstrap_arguments; i++) {
                buf.append("\n     ").append(i).append(": ");
                buf.append(constant_pool.constantToString(constant_pool.getConstant(bootstrap_arguments[i])));
            }
        }
        return buf.toString();
    }
}

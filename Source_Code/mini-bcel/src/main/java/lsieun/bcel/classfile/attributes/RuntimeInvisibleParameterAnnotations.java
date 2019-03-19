package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * Represents a parameter annotation that is represented in the class file
 * but is not provided to the JVM.
 *
 * @version $Id: RuntimeInvisibleParameterAnnotations
 * TODO: 这里应该改成@see之类的注释
 */
public class RuntimeInvisibleParameterAnnotations extends ParameterAnnotations {
    /**
     * @param name_index Index pointing to the name <em>Code</em>
     * @param length Content length in bytes
     * @param input Input stream
     * @param constant_pool Array of constants
     */
    public RuntimeInvisibleParameterAnnotations(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool)
            throws IOException {
        super(AttrConst.ATTR_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS, name_index, length, input, constant_pool);
    }
}

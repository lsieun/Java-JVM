package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * represents an annotation that is represented in the class file and is
 * provided to the JVM.
 *
 * @version $Id: RuntimeVisibleAnnotations
 */
public final class RuntimeVisibleAnnotations extends Annotations {
    /**
     * @param name_index    Index pointing to the name <em>Code</em>
     * @param length        Content length in bytes
     * @param input         Input stream
     * @param constant_pool Array of constants
     */
    public RuntimeVisibleAnnotations(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool) throws IOException {
        super(AttrConst.ATTR_RUNTIME_VISIBLE_ANNOTATIONS, name_index, length, input, constant_pool, true);
    }
}

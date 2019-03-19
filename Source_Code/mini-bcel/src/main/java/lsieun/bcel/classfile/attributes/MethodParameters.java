package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;

/**
 * This class represents a MethodParameters attribute.
 *
 * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.24">
 * The class File Format : The MethodParameters Attribute</a>
 */
public class MethodParameters extends Attribute {
    private MethodParameter[] parameters = new MethodParameter[0];

    MethodParameters(final int name_index, final int length, final DataInput input, final ConstantPool constant_pool) throws IOException {
        super(AttrConst.ATTR_METHOD_PARAMETERS, name_index, length, constant_pool);

        final int parameters_count = input.readUnsignedByte();
        parameters = new MethodParameter[parameters_count];
        for (int i = 0; i < parameters_count; i++) {
            parameters[i] = new MethodParameter(input);
        }
    }

    public MethodParameter[] getParameters() {
        return parameters;
    }

    @Override
    public void accept(final Visitor v) {
        v.visitMethodParameters(this);
    }
}

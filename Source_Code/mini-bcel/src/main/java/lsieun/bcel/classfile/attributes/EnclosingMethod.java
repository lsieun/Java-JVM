package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantClass;
import lsieun.bcel.classfile.cp.ConstantNameAndType;

/**
 * This attribute exists for local or
 * anonymous classes and ... there can be only one.
 *
 */
public class EnclosingMethod extends Attribute {
    // Pointer to the CONSTANT_Class_info structure representing the
    // innermost class that encloses the declaration of the current class.
    private int classIndex;

    // If the current class is not immediately enclosed by a method or
    // constructor, then the value of the method_index item must be zero.
    // Otherwise, the value of the  method_index item must point to a
    // CONSTANT_NameAndType_info structure representing the name and the
    // type of a method in the class referenced by the class we point
    // to in the class_index.  *It is the compiler responsibility* to
    // ensure that the method identified by this index is the closest
    // lexically enclosing method that includes the local/anonymous class.
    private int methodIndex;

    // Constructors - and code to read an attribute in.
    EnclosingMethod(final int nameIndex, final int len, final DataInput input, final ConstantPool cpool) throws IOException {
        this(nameIndex, len, input.readUnsignedShort(), input.readUnsignedShort(), cpool);
    }

    private EnclosingMethod(final int nameIndex, final int len, final int classIdx,final int methodIdx, final ConstantPool cpool) {
        super(AttrConst.ATTR_ENCLOSING_METHOD, nameIndex, len, cpool);
        classIndex  = classIdx;
        methodIndex = methodIdx;
    }

    public final int getEnclosingClassIndex() {
        return classIndex;
    }

    public final int getEnclosingMethodIndex() {
        return methodIndex;
    }

    public final ConstantClass getEnclosingClass() {
        final ConstantClass c = (ConstantClass)super.getConstantPool().getConstant(classIndex, CPConst.CONSTANT_Class);
        return c;
    }

    public final ConstantNameAndType getEnclosingMethod() {
        if (methodIndex == 0) {
            return null;
        }
        final ConstantNameAndType nat = (ConstantNameAndType)super.getConstantPool().getConstant(methodIndex, CPConst.CONSTANT_NameAndType);
        return nat;
    }

    @Override
    public void accept(final Visitor v) {
        v.visitEnclosingMethod(this);
    }
}

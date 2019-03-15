package lsieun.bcel.classfile.cp;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.Node;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.exceptions.ClassFormatException;

public abstract class Constant implements Node {
    private final byte tag;

    Constant(final byte tag) {
        this.tag = tag;
    }

    /**
     * @return Tag of constant, i.e., its type. No setTag() method to avoid
     * confusion.
     */
    public final byte getTag() {
        return tag;
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public abstract void accept(Visitor v);

    /**
     * Read one constant from the given input, the type depends on a tag byte.
     *
     * @param dataInput Input stream
     * @return Constant object
     * @throws IOException if an I/O error occurs reading from the given {@code dataInput}.
     * @throws ClassFormatException if the next byte is not recognized
     * @since 6.0 made public
     */
    public static Constant readConstant(final DataInput dataInput) throws IOException, ClassFormatException {
        final byte b = dataInput.readByte(); // Read tag byte
        switch (b) {
            case CPConst.CONSTANT_Utf8:
                return new ConstantUtf8(dataInput);
            case CPConst.CONSTANT_Integer:
                return new ConstantInteger(dataInput);
            case CPConst.CONSTANT_Float:
                return new ConstantFloat(dataInput);
            case CPConst.CONSTANT_Long:
                return new ConstantLong(dataInput);
            case CPConst.CONSTANT_Double:
                return new ConstantDouble(dataInput);
            case CPConst.CONSTANT_Class:
                return new ConstantClass(dataInput);
            case CPConst.CONSTANT_String:
                return new ConstantString(dataInput);
            case CPConst.CONSTANT_Fieldref:
                return new ConstantFieldref(dataInput);
            case CPConst.CONSTANT_Methodref:
                return new ConstantMethodref(dataInput);
            case CPConst.CONSTANT_InterfaceMethodref:
                return new ConstantInterfaceMethodref(dataInput);
            case CPConst.CONSTANT_NameAndType:
                return new ConstantNameAndType(dataInput);
            case CPConst.CONSTANT_MethodHandle:
                return new ConstantMethodHandle(dataInput);
            case CPConst.CONSTANT_MethodType:
                return new ConstantMethodType(dataInput);
            case CPConst.CONSTANT_Dynamic:
                return new ConstantDynamic(dataInput);
            case CPConst.CONSTANT_InvokeDynamic:
                return new ConstantInvokeDynamic(dataInput);
            case CPConst.CONSTANT_Module:
                return new ConstantModule(dataInput);
            case CPConst.CONSTANT_Package:
                return new ConstantPackage(dataInput);
            default:
                throw new ClassFormatException("Invalid byte tag in constant pool: " + b);
        }
    }

    /**
     * @return String representation.
     */
    @Override
    public String toString() {
        return CPConst.getConstantName(tag) + "[" + tag + "]";
    }

}

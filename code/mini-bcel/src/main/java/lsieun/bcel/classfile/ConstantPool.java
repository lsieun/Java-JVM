package lsieun.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.consts.MethodHandleConst;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.Constant;
import lsieun.bcel.classfile.cp.ConstantCP;
import lsieun.bcel.classfile.cp.ConstantClass;
import lsieun.bcel.classfile.cp.ConstantDouble;
import lsieun.bcel.classfile.cp.ConstantFloat;
import lsieun.bcel.classfile.cp.ConstantInteger;
import lsieun.bcel.classfile.cp.ConstantInvokeDynamic;
import lsieun.bcel.classfile.cp.ConstantLong;
import lsieun.bcel.classfile.cp.ConstantMethodHandle;
import lsieun.bcel.classfile.cp.ConstantMethodType;
import lsieun.bcel.classfile.cp.ConstantNameAndType;
import lsieun.bcel.classfile.cp.ConstantString;
import lsieun.bcel.classfile.cp.ConstantUtf8;
import lsieun.bcel.exceptions.ClassFormatException;

/**
 * This class represents the constant pool, i.e., a table of constants, of
 * a parsed classfile. It may contain null references, due to the JVM
 * specification that skips an entry after an 8-byte constant (double,
 * long) entry.
 *
 * @see     Constant
 */
public class ConstantPool implements Node {

    private Constant[] constant_pool;

    /**
     * Read constants from given input stream.
     *
     * @param input Input stream
     * @throws IOException
     * @throws ClassFormatException
     */
    public ConstantPool(final DataInput input) throws IOException, ClassFormatException {
        byte tag;
        final int constant_pool_count = input.readUnsignedShort();
        constant_pool = new Constant[constant_pool_count];
        /* constant_pool[0] is unused by the compiler and may be used freely
         * by the implementation.
         */
        for (int i = 1; i < constant_pool_count; i++) {
            constant_pool[i] = Constant.readConstant(input);
            /* Quote from the JVM specification:
             * "All eight byte constants take up two spots in the constant pool.
             * If this is the n'th byte in the constant pool, then the next item
             * will be numbered n+2"
             *
             * Thus we have to increment the index counter.
             */
            tag = constant_pool[i].getTag();
            if ((tag == CPConst.CONSTANT_Double) || (tag == CPConst.CONSTANT_Long)) {
                i++;
            }
        }
    }

    /**
     * @return Array of constants.
     * @see    Constant
     */
    public Constant[] getConstantPool() {
        return constant_pool;
    }

    /**
     * @return Length of constant pool.
     */
    public int getLength() {
        return constant_pool == null ? 0 : constant_pool.length;
    }

    /**
     * Resolve constant to a string representation.
     *
     * @param  c Constant to be printed
     * @return String representation
     */
    public String constantToString( Constant c ) throws ClassFormatException {
        String str;
        int i;
        final byte tag = c.getTag();
        switch (tag) {
            case CPConst.CONSTANT_Utf8:
                str = ((ConstantUtf8) c).getBytes();
                break;
            case CPConst.CONSTANT_Integer:
                str = String.valueOf(((ConstantInteger) c).getBytes());
                break;
            case CPConst.CONSTANT_Float:
                str = String.valueOf(((ConstantFloat) c).getBytes());
                break;
            case CPConst.CONSTANT_Long:
                str = String.valueOf(((ConstantLong) c).getBytes());
                break;
            case CPConst.CONSTANT_Double:
                str = String.valueOf(((ConstantDouble) c).getBytes());
                break;
            case CPConst.CONSTANT_Class:
                i = ((ConstantClass) c).getNameIndex();
                c = getConstant(i, CPConst.CONSTANT_Utf8);
                str = Utility.compactClassName(((ConstantUtf8) c).getBytes(), false);
                break;
            case CPConst.CONSTANT_String:
                i = ((ConstantString) c).getStringIndex();
                c = getConstant(i, CPConst.CONSTANT_Utf8);
                str = "\"" + escape(((ConstantUtf8) c).getBytes()) + "\"";
                break;
            case CPConst.CONSTANT_Fieldref:
            case CPConst.CONSTANT_Methodref:
            case CPConst.CONSTANT_InterfaceMethodref:
                str = constantToString(((ConstantCP) c).getClassIndex(), CPConst.CONSTANT_Class)
                        + "." + constantToString(((ConstantCP) c).getNameAndTypeIndex(),
                        CPConst.CONSTANT_NameAndType);
                break;
            case CPConst.CONSTANT_NameAndType:
                str = constantToString(((ConstantNameAndType) c).getNameIndex(), CPConst.CONSTANT_Utf8)
                        + " " + constantToString(((ConstantNameAndType) c).getSignatureIndex(), CPConst.CONSTANT_Utf8);
                break;
            case CPConst.CONSTANT_MethodHandle:
                // Note that the ReferenceIndex may point to a Fieldref, Methodref or
                // InterfaceMethodref - so we need to peek ahead to get the actual type.
                final ConstantMethodHandle cmh = (ConstantMethodHandle) c;
                str = MethodHandleConst.getMethodHandleName(cmh.getReferenceKind())
                        + " " + constantToString(cmh.getReferenceIndex(), getConstant(cmh.getReferenceIndex()).getTag());
                break;
            case CPConst.CONSTANT_MethodType:
                final ConstantMethodType cmt = (ConstantMethodType) c;
                str = constantToString(cmt.getDescriptorIndex(), CPConst.CONSTANT_Utf8);
                break;
            case CPConst.CONSTANT_InvokeDynamic:
                final ConstantInvokeDynamic cid = (ConstantInvokeDynamic) c;
                str = cid.getBootstrapMethodAttrIndex()
                        + ":" + constantToString(cid.getNameAndTypeIndex(), CPConst.CONSTANT_NameAndType);
                break;
            default: // Never reached
                throw new RuntimeException("Unknown constant type " + tag);
        }
        return str;
    }

    /**
     * Retrieve constant at `index' from constant pool and resolve it to
     * a string representation.
     *
     * @param  index of constant in constant pool
     * @param  tag expected type
     * @return String representation
     */
    public String constantToString(final int index, final byte tag) throws ClassFormatException {
        final Constant c = getConstant(index, tag);
        return constantToString(c);
    }

    /**
     * Get constant from constant pool and check whether it has the
     * expected type.
     *
     * @param  index Index in constant pool
     * @param  tag Tag of expected constant, i.e., its type
     * @return Constant value
     * @see    Constant
     * @throws  ClassFormatException
     */
    public Constant getConstant(final int index, final byte tag) throws ClassFormatException {
        Constant c = getConstant(index);

        if (c == null) {
            throw new ClassFormatException("Constant pool at index " + index + " is null.");
        }
        if (c.getTag() != tag) {
            throw new ClassFormatException("Expected class `" + CPConst.getConstantName(tag)
                    + "' at index " + index + " and got " + c);
        }
        return c;
    }

    /**
     * Get constant from constant pool.
     *
     * @param  index Index in constant pool
     * @return Constant value
     * @see    Constant
     */
    public Constant getConstant(final int index) {
        if (index >= constant_pool.length || index < 0) {
            throw new ClassFormatException("Invalid constant pool reference: " + index
                    + ". Constant pool size is: " + constant_pool.length);
        }
        return constant_pool[index];
    }

    private static String escape(final String str) {
        final int len = str.length();
        final StringBuilder buf = new StringBuilder(len + 5);
        final char[] ch = str.toCharArray();
        for (int i = 0; i < len; i++) {
            switch (ch[i]) {
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\t':
                    buf.append("\\t");
                    break;
                case '\b':
                    buf.append("\\b");
                    break;
                case '"':
                    buf.append("\\\"");
                    break;
                default:
                    buf.append(ch[i]);
            }
        }
        return buf.toString();
    }

    /**
     * Get string from constant pool and bypass the indirection of
     * `ConstantClass' and `ConstantString' objects. I.e. these classes have
     * an index field that points to another entry of the constant pool of
     * type `ConstantUtf8' which contains the real data.
     *
     * @param  index Index in constant pool
     * @param  tag Tag of expected constant, either ConstantClass or ConstantString
     * @return Contents of string reference
     * @see    ConstantClass
     * @see    ConstantString
     * @throws  ClassFormatException
     */
    public String getConstantString( final int index, final byte tag ) throws ClassFormatException {
        Constant c;
        int i;
        c = getConstant(index, tag);
        /* This switch() is not that elegant, since the two classes have the
         * same contents, they just differ in the name of the index
         * field variable.
         * But we want to stick to the JVM naming conventions closely though
         * we could have solved these more elegantly by using the same
         * variable name or by subclassing.
         */
        switch (tag) {
            case CPConst.CONSTANT_Class:
                i = ((ConstantClass) c).getNameIndex();
                break;
            case CPConst.CONSTANT_String:
                i = ((ConstantString) c).getStringIndex();
                break;
            default:
                throw new RuntimeException("getConstantString called with illegal tag " + tag);
        }
        // Finally get the string from the constant pool
        c = getConstant(i, CPConst.CONSTANT_Utf8);
        return ((ConstantUtf8) c).getBytes();
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitConstantPool(this);
    }

    /**
     * @return String representation.
     */
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        for (int i = 1; i < constant_pool.length; i++) {
            buf.append(i).append(")").append(constant_pool[i]).append("\n");
        }
        return buf.toString();
    }
}

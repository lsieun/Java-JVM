package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Node;
import lsieun.bcel.classfile.Utility;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;

/**
 * This class represents a local variable within a method. It contains its
 * scope, name, signature and index on the method's frame.
 *
 * @see     LocalVariableTable
 */
public final class LocalVariable implements Node {
    private int start_pc; // Range in which the variable is valid
    private int length;
    private int name_index; // Index in constant pool of variable name
    private int signature_index; // Index of variable signature
    private int index; /* Variable is `index'th local variable on
     * this method's frame.
     */
    private ConstantPool constant_pool;
    private int orig_index; // never changes; used to match up with LocalVariableTypeTable entries

    /**
     * Construct object from file stream.
     * @param file Input stream
     * @throws IOException
     */
    LocalVariable(final DataInput file, final ConstantPool constant_pool) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file
                .readUnsignedShort(), file.readUnsignedShort(), constant_pool);
    }

    /**
     * @param start_pc Range in which the variable
     * @param length ... is valid
     * @param name_index Index in constant pool of variable name
     * @param signature_index Index of variable's signature
     * @param index Variable is `index'th local variable on the method's frame
     * @param constant_pool Array of constants
     */
    public LocalVariable(final int start_pc, final int length, final int name_index, final int signature_index, final int index,
                         final ConstantPool constant_pool) {
        this.start_pc = start_pc;
        this.length = length;
        this.name_index = name_index;
        this.signature_index = signature_index;
        this.index = index;
        this.constant_pool = constant_pool;
        this.orig_index = index;
    }

    /**
     * @return Start of range where he variable is valid
     */
    public final int getStartPC() {
        return start_pc;
    }

    /**
     * @return Variable is valid within getStartPC() .. getStartPC()+getLength()
     */
    public final int getLength() {
        return length;
    }

    /**
     * @return Index in constant pool of variable name.
     */
    public final int getNameIndex() {
        return name_index;
    }

    /**
     * @return Index in constant pool of variable signature.
     */
    public final int getSignatureIndex() {
        return signature_index;
    }

    /**
     * @return index of register where variable is stored
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @return index of register where variable was originally stored
     */
    public final int getOrigIndex() {
        return orig_index;
    }

    /**
     * @return Constant pool used by this object.
     */
    public final ConstantPool getConstantPool() {
        return constant_pool;
    }

    /**
     * @return Variable name.
     */
    public final String getName() {
        ConstantUtf8 c;
        c = (ConstantUtf8) constant_pool.getConstant(name_index, CPConst.CONSTANT_Utf8);
        return c.getBytes();
    }

    /**
     * @return Signature.
     */
    public final String getSignature() {
        ConstantUtf8 c;
        c = (ConstantUtf8) constant_pool.getConstant(signature_index, CPConst.CONSTANT_Utf8);
        return c.getBytes();
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
        v.visitLocalVariable(this);
    }

    /**
     * @return string representation.
     */
    @Override
    public final String toString() {
        return toStringShared(false);
    }

    /*
     * Helper method shared with LocalVariableTypeTable
     */
    final String toStringShared(final boolean typeTable) {
        final String name = getName();
        final String signature = Utility.signatureToString(getSignature(), false);
        final String label = "LocalVariable" + (typeTable ? "Types" : "" );
        return label + "(start_pc = " + start_pc + ", length = " + length + ", index = "
                + index + ":" + signature + " " + name + ")";
    }
}

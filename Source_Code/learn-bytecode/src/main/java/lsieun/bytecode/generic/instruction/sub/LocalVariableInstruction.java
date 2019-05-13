package lsieun.bytecode.generic.instruction.sub;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.JVMConst;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;
import lsieun.bytecode.generic.instruction.facet.IndexedInstruction;
import lsieun.bytecode.generic.type.Type;

/**
 * Abstract super class for instructions dealing with local variables.
 */
public abstract class LocalVariableInstruction extends Instruction
        implements TypedInstruction, IndexedInstruction {
    private int n = -1; // index of referenced variable

    private short c_tag = -1; // compact version, such as ILOAD_0
    private short canon_tag = -1; // canonical tag such as ILOAD


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     * tag and length are defined in readInstruction and readFully, respectively.
     */
    public LocalVariableInstruction(final short canon_tag, final short c_tag) {
        this.canon_tag = canon_tag;
        this.c_tag = c_tag;
    }


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Also used by IINC()!
     */
    public LocalVariableInstruction() {
    }

    /**
     * @param opcode Instruction opcode
     * @param c_tag  Instruction number for compact version, ALOAD_0, e.g.
     * @param n      local variable index (unsigned short)
     */
    protected LocalVariableInstruction(final short opcode, final short c_tag, final int n) {
        super(opcode, (short) 2);
        this.c_tag = c_tag;
        this.canon_tag = opcode;
        setIndex(n);
    }

    /**
     * @return local variable index (n) referred by this instruction.
     */
    @Override
    public final int getIndex() {
        return n;
    }

    /**
     * Set the local variable index.
     * also updates opcode and length
     * TODO Why?
     *
     * @see #setIndexOnly(int)
     */
    @Override
    public void setIndex(final int n) { // TODO could be package-protected?
        if ((n < 0) || (n > JVMConst.MAX_SHORT)) {
            throw new ClassGenException("Illegal value: " + n);
        }
        this.n = n;
        // Cannot be < 0 as this is checked above
        if (n <= 3) { // Use more compact instruction xLOAD_n
            super.setOpcode((short) (c_tag + n));
            super.setLength(1);
        } else {
            super.setOpcode(canon_tag);
            if (wide()) {
                super.setLength(4);
            } else {
                super.setLength(2);
            }
        }
    }

    /**
     * Sets the index of the referenced variable (n) only
     * @see #setIndex(int)
     */
    public void setIndexOnly(final int n) {
        this.n = n;
    }

    /**
     * @return canonical tag for instruction, e.g., ALOAD for ALOAD_0
     */
    public short getCanonicalTag() {
        return canon_tag;
    }


    private boolean wide() {
        return n > JVMConst.MAX_BYTE;
    }

    /**
     * Returns the type associated with the instruction -
     * in case of ALOAD or ASTORE Type.OBJECT is returned.
     * This is just a bit incorrect, because ALOAD and ASTORE
     * may work on every ReferenceType (including Type.NULL) and
     * ASTORE may even work on a ReturnaddressType .
     *
     * @return type associated with the instruction
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        switch (canon_tag) {
            case OpcodeConst.ILOAD:
            case OpcodeConst.ISTORE:
                return Type.INT;
            case OpcodeConst.LLOAD:
            case OpcodeConst.LSTORE:
                return Type.LONG;
            case OpcodeConst.DLOAD:
            case OpcodeConst.DSTORE:
                return Type.DOUBLE;
            case OpcodeConst.FLOAD:
            case OpcodeConst.FSTORE:
                return Type.FLOAT;
            case OpcodeConst.ALOAD:
            case OpcodeConst.ASTORE:
                return Type.OBJECT;
            default:
                throw new ClassGenException("Oops: unknown case in switch" + canon_tag);
        }
    }
}

package lsieun.bytecode.generic.instruction.sub.load;

import lsieun.bytecode.generic.instruction.facet.PopInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.instruction.sub.LocalVariableInstruction;

/**
 * Denotes an unparameterized instruction to store a value into a local variable,
 * e.g. ISTORE.
 */
public abstract class StoreInstruction extends LocalVariableInstruction implements PopInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     * tag and length are defined in readInstruction and readFully, respectively.
     */
    public StoreInstruction(final short canon_tag, final short c_tag) {
        super(canon_tag, c_tag);
    }


    /**
     * @param opcode Instruction opcode
     * @param c_tag  Instruction number for compact version, ASTORE_0, e.g.
     * @param n      local variable index (unsigned short)
     */
    protected StoreInstruction(final short opcode, final short c_tag, final int n) {
        super(opcode, c_tag, n);
    }


    /**
     * Call corresponding visitor method(s). The order is:
     * Call visitor methods of implemented interfaces first, then
     * call methods according to the class hierarchy in descending order,
     * i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitStackConsumer(this);
        v.visitPopInstruction(this);
        v.visitTypedInstruction(this);
        v.visitLocalVariableInstruction(this);
        v.visitStoreInstruction(this);
    }
}

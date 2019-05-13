package lsieun.bytecode.generic.opcode.cst;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.sub.ConstantPushInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.Type;

/**
 * ICONST - Push value between -1, ..., 5, other values cause an exception
 *
 * <PRE>Stack: ... -&gt; ..., </PRE>
 *
 */
public class ICONST extends ConstantPushInstruction {
    private int value;

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public ICONST() {
    }



    public ICONST(final int i) {
        super(OpcodeConst.ICONST_0, (short) 1);
        if ((i >= -1) && (i <= 5)) {
            super.setOpcode((short) (OpcodeConst.ICONST_0 + i)); // Even works for i == -1
        } else {
            throw new ClassGenException("ICONST can be used only for value between -1 and 5: " + i);
        }
        value = i;
    }

    @Override
    public Number getValue() {
        return Integer.valueOf(value);
    }

    /** @return Type.INT
     */
    @Override
    public Type getType(final ConstantPoolGen cp ) {
        return Type.INT;
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
    public void accept(Visitor v) {
        v.visitPushInstruction(this);
        v.visitStackProducer(this);
        v.visitTypedInstruction(this);
        v.visitConstantPushInstruction(this);
        v.visitICONST(this);
    }

}

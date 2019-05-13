package lsieun.bytecode.generic.opcode.cst;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.sub.ConstantPushInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.Type;

/**
 * DCONST - Push 0.0 or 1.0, other values cause an exception
 *
 * <PRE>Stack: ... -&gt; ..., </PRE>
 *
 */
public class DCONST extends ConstantPushInstruction {

    private double value;


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    DCONST() {
    }


    public DCONST(final double f) {
        super(OpcodeConst.DCONST_0, (short) 1);
        if (f == 0.0) {
            super.setOpcode(OpcodeConst.DCONST_0);
        } else if (f == 1.0) {
            super.setOpcode(OpcodeConst.DCONST_1);
        } else {
            throw new ClassGenException("DCONST can be used only for 0.0 and 1.0: " + f);
        }
        value = f;
    }


    @Override
    public Number getValue() {
        return new Double(value);
    }


    /**
     * @return Type.DOUBLE
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return Type.DOUBLE;
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
        v.visitPushInstruction(this);
        v.visitStackProducer(this);
        v.visitTypedInstruction(this);
        v.visitConstantPushInstruction(this);
        v.visitDCONST(this);
    }
}

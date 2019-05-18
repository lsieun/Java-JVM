package lsieun.bytecode.generic.opcode.cst;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.sub.ConstantPushInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.Type;

/**
 * FCONST - Push 0.0, 1.0 or 2.0, other values cause an exception
 *
 * <PRE>Stack: ... -&gt; ..., </PRE>
 */
public class FCONST extends ConstantPushInstruction {

    private float value;


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    FCONST() {
    }


    public FCONST(final float f) {
        super(OpcodeConst.FCONST_0, (short) 1);
        if (f == 0.0) {
            super.setOpcode(OpcodeConst.FCONST_0);
        } else if (f == 1.0) {
            super.setOpcode(OpcodeConst.FCONST_1);
        } else if (f == 2.0) {
            super.setOpcode(OpcodeConst.FCONST_2);
        } else {
            throw new ClassGenException("FCONST can be used only for 0.0, 1.0 and 2.0: " + f);
        }
        value = f;
    }


    @Override
    public Number getValue() {
        return new Float(value);
    }


    /**
     * @return Type.FLOAT
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return Type.FLOAT;
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
        v.visitFCONST(this);
    }
}

package lsieun.bytecode.generic.opcode.compare;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.fairydust.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.instruction.sub.CompareInstruction;
import lsieun.bytecode.generic.type.Type;

/**
 * FCMPL - Compare floats: value1 &lt; value2
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 */
public class FCMPL extends CompareInstruction {

    public FCMPL() {
        super(OpcodeConst.FCMPL, (short) 1);
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
        v.visitCompareInstruction(this);
        v.visitTypedInstruction(this);
        v.visitStackProducer(this);
        v.visitStackConsumer(this);
        v.visitFCMPL(this);
    }
}

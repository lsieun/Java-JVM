package lsieun.bytecode.generic.opcode.compare;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.facet.StackProducer;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.Type;

/**
 * FCMPG - Compare floats: value1 &gt; value2
 * <PRE>Stack: ..., value1, value2 -&gt; ..., result</PRE>
 *
 * @version $Id$
 */
public class FCMPG extends Instruction implements TypedInstruction, StackProducer, StackConsumer {

    public FCMPG() {
        super(OpcodeConst.FCMPG, (short) 1);
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
        v.visitTypedInstruction(this);
        v.visitStackProducer(this);
        v.visitStackConsumer(this);
        v.visitFCMPG(this);
    }
}

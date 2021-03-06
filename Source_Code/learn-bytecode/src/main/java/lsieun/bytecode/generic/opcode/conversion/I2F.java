package lsieun.bytecode.generic.opcode.conversion;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.ConversionInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * I2F - Convert int to float
 * <PRE>Stack: ..., value -&gt; ..., result</PRE>
 *
 * @version $Id$
 */
public class I2F extends ConversionInstruction {

    /**
     * Convert int to float
     */
    public I2F() {
        super(OpcodeConst.I2F);
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
        v.visitConversionInstruction(this);
        v.visitI2F(this);
    }
}

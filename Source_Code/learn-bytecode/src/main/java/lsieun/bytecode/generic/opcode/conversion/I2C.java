package lsieun.bytecode.generic.opcode.conversion;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.sub.ConversionInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * I2C - Convert int to char
 * <PRE>Stack: ..., value -&gt; ..., result</PRE>
 *
 * @version $Id$
 */
public class I2C extends ConversionInstruction {

    /**
     * Convert int to char
     */
    public I2C() {
        super(OpcodeConst.I2C);
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
        v.visitI2C(this);
    }
}

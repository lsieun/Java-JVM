package lsieun.bytecode.generic.opcode.branh;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.sub.branch.JsrInstruction;
import lsieun.bytecode.generic.instruction.facet.VariableLengthInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * JSR - Jump to subroutine
 *
 * @version $Id$
 */
public class JSR extends JsrInstruction implements VariableLengthInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public JSR() {
    }


    public JSR(final InstructionHandle target) {
        super(OpcodeConst.JSR, target);
    }


    @Override
    public int updatePosition(final int offset, final int max_offset) {
        final int i = getTargetOffset(); // Depending on old position value
        setPosition(getPosition() + offset); // Position may be shifted by preceding expansions
        if (Math.abs(i) >= (Short.MAX_VALUE - max_offset)) { // to large for short (estimate)
            super.setOpcode(OpcodeConst.JSR_W);
            final short old_length = (short) super.getLength();
            super.setLength(5);
            return super.getLength() - old_length;
        }
        return 0;
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
        v.visitStackProducer(this);
        v.visitVariableLengthInstruction(this);
        v.visitBranchInstruction(this);
        v.visitJsrInstruction(this);
        v.visitJSR(this);
    }
}

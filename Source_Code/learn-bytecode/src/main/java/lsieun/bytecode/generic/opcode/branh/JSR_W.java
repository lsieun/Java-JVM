package lsieun.bytecode.generic.opcode.branh;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.sub.branch.JsrInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * JSR_W - Jump to subroutine
 *
 * @version $Id$
 */
public class JSR_W extends JsrInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public JSR_W() {
    }


    public JSR_W(final InstructionHandle target) {
        super(OpcodeConst.JSR_W, target);
        super.setLength(5);
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
        v.visitBranchInstruction(this);
        v.visitJsrInstruction(this);
        v.visitJSR_W(this);
    }
}

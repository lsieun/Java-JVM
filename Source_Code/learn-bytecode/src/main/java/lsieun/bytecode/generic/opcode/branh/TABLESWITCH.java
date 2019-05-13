package lsieun.bytecode.generic.opcode.branh;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.sub.branch.SelectInstruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * TABLESWITCH - Switch within given range of values, i.e., low..high
 *
 * @version $Id$
 * @see SWITCH
 */
public class TABLESWITCH extends SelectInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public TABLESWITCH() {
    }


    /**
     * @param match         sorted array of match values, match[0] must be low value,
     *                      match[match_length - 1] high value
     * @param targets       where to branch for matched values
     * @param defaultTarget default branch
     */
    public TABLESWITCH(final int[] match, final InstructionHandle[] targets, final InstructionHandle defaultTarget) {
        super(OpcodeConst.TABLESWITCH, match, targets, defaultTarget);
        /* Alignment remainder assumed 0 here, until dump time */
        final short _length = (short) (13 + getMatch_length() * 4);
        super.setLength(_length);
        setFixed_length(_length);
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
        v.visitVariableLengthInstruction(this);
        v.visitStackConsumer(this);
        v.visitBranchInstruction(this);
        v.visitSelect(this);
        v.visitTABLESWITCH(this);
    }
}

package lsieun.bytecode.generic.opcode.branh;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.sub.branch.SelectInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.utils.ByteDashboard;

/**
 * LOOKUPSWITCH - Switch with unordered set of values
 *
 * @version $Id$
 * @see SWITCH
 */
public class LOOKUPSWITCH extends SelectInstruction {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public LOOKUPSWITCH() {
    }


    public LOOKUPSWITCH(final int[] match, final InstructionHandle[] targets, final InstructionHandle defaultTarget) {
        super(OpcodeConst.LOOKUPSWITCH, match, targets, defaultTarget);
        /* alignment remainder assumed 0 here, until dump time. */
        final short _length = (short) (9 + getMatch_length() * 8);
        super.setLength(_length);
        setFixed_length(_length);
    }

    @Override
    protected void readFully(ByteDashboard byteDashboard, boolean wide) {
        super.readFully(byteDashboard, wide); // reads padding
        final int _match_length = byteDashboard.readInt();
        setMatch_length(_match_length);
        final short _fixed_length = (short) (9 + _match_length * 8);
        setFixed_length(_fixed_length);
        final short _length = (short) (_fixed_length + super.getPadding());
        super.setLength(_length);
        super.setMatches(new int[_match_length]);
        super.setIndices(new int[_match_length]);
        super.setTargets(new InstructionHandle[_match_length]);
        for (int i = 0; i < _match_length; i++) {
            super.setMatch(i, byteDashboard.readInt());
            super.setIndices(i, byteDashboard.readInt());
        }
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
        v.visitLOOKUPSWITCH(this);
    }
}

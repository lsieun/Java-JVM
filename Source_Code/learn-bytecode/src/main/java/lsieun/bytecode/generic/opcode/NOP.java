package lsieun.bytecode.generic.opcode;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.Visitor;

/**
 * NOP - Do nothing
 *
 */
public class NOP extends Instruction {
    public NOP() {
        super(OpcodeConst.NOP, (short) 1);
    }

    @Override
    public void accept(Visitor v) {
        v.visitNOP(this);
    }
}

package lsieun.bytecode.generic.opcode;

import lsieun.bytecode.generic.cnst.OpcodeConst;

/**
 * NOP - Do nothing
 *
 */
public class NOP extends Instruction {
    public NOP() {
        super(OpcodeConst.NOP, (short) 1);
    }

    @Override
    public void accept(OpcodeVisitor v) {
        v.visitNOP(this);
    }
}

package lsieun.bytecode.generic.instruction.sub;

import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.PushInstruction;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;
import lsieun.bytecode.generic.opcode.cst.ICONST;
import lsieun.bytecode.generic.opcode.cst.SIPUSH;

/**
 * Denotes a push instruction that produces a literal on the stack
 * such as  SIPUSH, BIPUSH, ICONST, etc.
 *
 * @version $Id$

 * @see ICONST
 * @see SIPUSH
 */
public abstract class ConstantPushInstruction extends Instruction implements PushInstruction, TypedInstruction {
    public ConstantPushInstruction() {
    }

    public ConstantPushInstruction(short opcode, short length) {
        super(opcode, length);
    }

    public abstract Number getValue();
}

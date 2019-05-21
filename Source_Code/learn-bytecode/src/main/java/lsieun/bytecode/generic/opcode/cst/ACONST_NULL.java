package lsieun.bytecode.generic.opcode.cst;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.fairydust.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.instruction.facet.PushInstruction;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;
import lsieun.bytecode.generic.type.Type;

public class ACONST_NULL extends Instruction implements PushInstruction, TypedInstruction {

    /**
     * Push null reference
     */
    public ACONST_NULL() {
        super(OpcodeConst.ACONST_NULL, (short) 1);
    }

    /**
     * @return Type.NULL
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return Type.NULL;
    }

    @Override
    public void accept(Visitor v) {
        v.visitStackProducer(this);
        v.visitPushInstruction(this);
        v.visitTypedInstruction(this);
        v.visitACONST_NULL(this);
    }

}

package lsieun.bytecode.generic.opcode;

public class ACONST_NULL extends Instruction {
    @Override
    public void accept(OpcodeVisitor v) {
        v.visitStackProducer(this);
        v.visitPushInstruction(this);
        v.visitTypedInstruction(this);
        v.visitACONST_NULL(this);
    }
}

package lsieun.bytecode.generic.instruction.sub;

import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.facet.StackProducer;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;

public abstract class CompareInstruction extends Instruction implements TypedInstruction, StackProducer, StackConsumer {
    public CompareInstruction() {
    }

    public CompareInstruction(short opcode, short length) {
        super(opcode, length);
    }
}

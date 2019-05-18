package lsieun.bytecode.generic.opcode.allocate;

import lsieun.bytecode.generic.LoadClass;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.facet.AllocationInstruction;
import lsieun.bytecode.generic.instruction.sub.CPInstruction;
import lsieun.bytecode.generic.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.facet.StackProducer;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.ObjectType;

/**
 * NEW - Create new object
 * <PRE>Stack: ... -&gt; ..., objectref</PRE>
 */
public class NEW extends CPInstruction
        implements LoadClass, AllocationInstruction, StackProducer {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public NEW() {
    }


    public NEW(final int index) {
        super(OpcodeConst.NEW, index);
    }

    @Override
    public ObjectType getLoadClassType(final ConstantPoolGen cpg) {
        return (ObjectType) getType(cpg);
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
        v.visitLoadClass(this);
        v.visitAllocationInstruction(this);
        v.visitStackProducer(this);
        v.visitTypedInstruction(this);
        v.visitCPInstruction(this);
        v.visitNEW(this);
    }
}

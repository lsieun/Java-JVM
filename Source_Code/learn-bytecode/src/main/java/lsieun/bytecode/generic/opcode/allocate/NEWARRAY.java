package lsieun.bytecode.generic.opcode.allocate;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.facet.AllocationInstruction;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.StackProducer;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.ArrayType;
import lsieun.bytecode.generic.type.BasicType;
import lsieun.bytecode.generic.type.Type;

/**
 * NEWARRAY -  Create new array of basic type (int, short, ...)
 * <PRE>Stack: ..., count -&gt; ..., arrayref</PRE>
 * type must be one of T_INT, T_SHORT, ...
 */
public class NEWARRAY extends Instruction
        implements AllocationInstruction, StackProducer {
    private byte type;

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public NEWARRAY() {
    }


    public NEWARRAY(final byte type) {
        super(OpcodeConst.NEWARRAY, (short) 2);
        this.type = type;
    }


    public NEWARRAY(final BasicType type) {
        this(type.getType());
    }

    /**
     * @return numeric code for basic element type
     */
    public final byte getTypecode() {
        return type;
    }


    /**
     * @return type of constructed array
     */
    public final Type getType() {
        return new ArrayType(BasicType.getType(type), 1);
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
        v.visitAllocationInstruction(this);
        v.visitStackProducer(this);
        v.visitNEWARRAY(this);
    }
}

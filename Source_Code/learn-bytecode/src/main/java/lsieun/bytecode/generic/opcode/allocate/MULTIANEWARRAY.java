package lsieun.bytecode.generic.opcode.allocate;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.LoadClass;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.facet.AllocationInstruction;
import lsieun.bytecode.generic.instruction.sub.CPInstruction;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.ArrayType;
import lsieun.bytecode.generic.type.ObjectType;
import lsieun.bytecode.generic.type.Type;

/**
 * MULTIANEWARRAY - Create new mutidimensional array of references
 * <PRE>Stack: ..., count1, [count2, ...] -&gt; ..., arrayref</PRE>
 */
public class MULTIANEWARRAY extends CPInstruction
        implements LoadClass, AllocationInstruction {
    private short dimensions;

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public MULTIANEWARRAY() {
    }

    public MULTIANEWARRAY(final int index, final short dimensions) {
        super(OpcodeConst.MULTIANEWARRAY, index);
        if (dimensions < 1) {
            throw new ClassGenException("Invalid dimensions value: " + dimensions);
        }
        this.dimensions = dimensions;
        super.setLength(4);
    }

    /**
     * @return number of dimensions to be created
     */
    public final short getDimensions() {
        return dimensions;
    }

    /**
     * Also works for instructions whose stack effect depends on the
     * constant pool entry they reference.
     *
     * @return Number of words consumed from stack by this instruction
     */
    @Override
    public int consumeStack(final ConstantPoolGen cpg) {
        return dimensions;
    }

    @Override
    public ObjectType getLoadClassType(final ConstantPoolGen cpg) {
        Type t = getType(cpg);
        if (t instanceof ArrayType) {
            t = ((ArrayType) t).getBasicType();
        }
        return (t instanceof ObjectType) ? (ObjectType) t : null;
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
        v.visitTypedInstruction(this);
        v.visitCPInstruction(this);
        v.visitMULTIANEWARRAY(this);
    }
}

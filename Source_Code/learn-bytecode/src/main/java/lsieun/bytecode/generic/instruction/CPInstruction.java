package lsieun.bytecode.generic.instruction;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.generic.type.Type;

/**
 * Abstract super class for instructions that use an index into the
 * constant pool such as LDC, INVOKEVIRTUAL, etc.
 *
 * @see ConstantPoolGen
 * @see LDC
 * @see INVOKEVIRTUAL
 */
public abstract class CPInstruction extends Instruction
        implements TypedInstruction, IndexedInstruction {
    private int index; // index to constant pool

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public CPInstruction() {
    }


    /**
     * @param index to constant pool
     */
    protected CPInstruction(final short opcode, final int index) {
        super(opcode, (short) 3);
        setIndex(index);
    }


    /**
     * @return index in constant pool referred by this instruction.
     */
    @Override
    public final int getIndex() {
        return index;
    }


    /**
     * Set the index to constant pool.
     * @param index in  constant pool.
     */
    @Override
    public void setIndex( final int index ) { // TODO could be package-protected?
        if (index < 0) {
            throw new ClassGenException("Negative index value: " + index);
        }
        this.index = index;
    }


    /** @return type related with this instruction.
     */
    @Override
    public Type getType(final ConstantPoolGen cpg ) {
        final ConstantPool cp = cpg.getConstantPool();
        String name = cp.getConstantString(index, CPConst.CONSTANT_Class);
        if (!name.startsWith("[")) {
            name = "L" + name + ";";
        }
        return Type.getType(name);
    }
}

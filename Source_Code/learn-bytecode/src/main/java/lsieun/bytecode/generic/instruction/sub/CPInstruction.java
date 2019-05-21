package lsieun.bytecode.generic.instruction.sub;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.fairydust.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.facet.IndexedInstruction;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;
import lsieun.bytecode.generic.type.Type;
import lsieun.bytecode.utils.ByteDashboard;

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
     *
     * @param index in  constant pool.
     */
    @Override
    public void setIndex(final int index) { // TODO could be package-protected?
        if (index < 0) {
            throw new ClassGenException("Negative index value: " + index);
        }
        this.index = index;
    }

    @Override
    protected void readFully(ByteDashboard byteDashboard, boolean wide) {
        int cpIndex = byteDashboard.nextShort();
        setIndex(cpIndex);
        super.setLength(3);
    }

    /**
     * @return type related with this instruction.
     */
    @Override
    public Type getType(final ConstantPoolGen cpg) {
        final ConstantPool cp = cpg.getConstantPool();
        String name = cp.getConstantString(index, CPConst.CONSTANT_Class);
        if (!name.startsWith("[")) {
            name = "L" + name + ";";
        }
        return Type.getType(name);
    }
}

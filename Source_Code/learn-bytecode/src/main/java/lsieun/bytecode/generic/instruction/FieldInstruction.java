package lsieun.bytecode.generic.instruction;

import lsieun.bytecode.generic.FieldOrMethod;
import lsieun.bytecode.generic.type.Type;

/**
 * Super class for the GET/PUTxxx family of instructions.
 */
public abstract class FieldInstruction extends FieldOrMethod {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public FieldInstruction() {
    }

    /**
     * @param index to constant pool
     */
    protected FieldInstruction(final short opcode, final int index) {
        super(opcode, index);
    }

    /**
     * @return size of field (1 or 2)
     */
    protected int getFieldSize(final ConstantPoolGen cpg) {
        return Type.size(Type.getTypeSizeAndCharNum(getSignature(cpg)));
    }

    /**
     * @return return type of referenced field
     */
    @Override
    public Type getType(final ConstantPoolGen cpg) {
        return getFieldType(cpg);
    }

    /**
     * @return type of field
     */
    public Type getFieldType(final ConstantPoolGen cpg) {
        return Type.getType(getSignature(cpg));
    }

    /**
     * @return name of referenced field.
     */
    public String getFieldName(final ConstantPoolGen cpg) {
        return getName(cpg);
    }
}

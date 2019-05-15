package lsieun.bytecode.generic.instruction.sub.cp;

import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.facet.StackProducer;
import lsieun.bytecode.generic.type.Type;

/**
 * Super class for the INVOKExxx family of instructions.
 */
public abstract class InvokeInstruction extends FieldOrMethod
        implements StackConsumer, StackProducer {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public InvokeInstruction() {
    }


    /**
     * @param index to constant pool
     */
    protected InvokeInstruction(final short opcode, final int index) {
        super(opcode, index);
    }

    /**
     * Also works for instructions whose stack effect depends on the
     * constant pool entry they reference.
     *
     * @return Number of words consumed from stack by this instruction
     */
    @Override
    public int consumeStack(final ConstantPoolGen cpg) {
        int sum;
        if ((super.getOpcode() == OpcodeConst.INVOKESTATIC) || (super.getOpcode() == OpcodeConst.INVOKEDYNAMIC)) {
            sum = 0;
        } else {
            sum = 1; // this reference
        }

        final String signature = getSignature(cpg);
        sum += Type.getArgumentTypesSize(signature);
        return sum;
    }

    /**
     * Also works for instructions whose stack effect depends on the
     * constant pool entry they reference.
     *
     * @return Number of words produced onto stack by this instruction
     */
    @Override
    public int produceStack(final ConstantPoolGen cpg) {
        final String signature = getSignature(cpg);
        return Type.getReturnTypeSize(signature);
    }

    /**
     * @return return type of referenced method.
     */
    @Override
    public Type getType(final ConstantPoolGen cpg) {
        return getReturnType(cpg);
    }

    /**
     * @return name of referenced method.
     */
    public String getMethodName(final ConstantPoolGen cpg) {
        return getName(cpg);
    }

    /**
     * @return return type of referenced method.
     */
    public Type getReturnType(final ConstantPoolGen cpg) {
        return Type.getReturnType(getSignature(cpg));
    }

    /**
     * @return argument types of referenced method.
     */
    public Type[] getArgumentTypes(final ConstantPoolGen cpg) {
        return Type.getArgumentTypes(getSignature(cpg));
    }
}

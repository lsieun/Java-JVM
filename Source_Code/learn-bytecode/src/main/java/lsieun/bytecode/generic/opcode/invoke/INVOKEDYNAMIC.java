package lsieun.bytecode.generic.opcode.invoke;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.cp.ConstantInvokeDynamic;
import lsieun.bytecode.classfile.cp.ConstantNameAndType;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.InvokeInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.ObjectType;
import lsieun.bytecode.generic.type.ReferenceType;

/**
 * Class for INVOKEDYNAMIC. Not an instance of InvokeInstruction, since that class
 * expects to be able to get the class of the method. Ignores the bootstrap
 * mechanism entirely.
 */
public class INVOKEDYNAMIC extends InvokeInstruction {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public INVOKEDYNAMIC() {
    }


    public INVOKEDYNAMIC(final int index) {
        super(OpcodeConst.INVOKEDYNAMIC, index);
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
        v.visitTypedInstruction(this);
        v.visitStackConsumer(this);
        v.visitStackProducer(this);
        v.visitLoadClass(this);
        v.visitCPInstruction(this);
        v.visitFieldOrMethod(this);
        v.visitInvokeInstruction(this);
        v.visitINVOKEDYNAMIC(this);
    }

//    /**
//     * Override the parent method because our classname is held elsewhere.
//     */
//    @Override
//    public String getClassName(final ConstantPoolGen cpg) {
//        final ConstantPool cp = cpg.getConstantPool();
//        final ConstantInvokeDynamic cid = (ConstantInvokeDynamic) cp.getConstant(super.getIndex(), CPConst.CONSTANT_InvokeDynamic);
//        return ((ConstantNameAndType) cp.getConstant(cid.getNameAndTypeIndex())).getName(cp);
//    }

    /**
     * Since InvokeDynamic doesn't refer to a reference type, just return java.lang.Object,
     * as that is the only type we can say for sure the reference will be.
     *
     * @param cpg
     *            the ConstantPoolGen used to create the instruction
     * @return an ObjectType for java.lang.Object
     */
    @Override
    public ReferenceType getReferenceType(final ConstantPoolGen cpg) {
        return new ObjectType(Object.class.getName());
    }
}

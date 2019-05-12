package lsieun.bytecode.generic.opcode;

import lsieun.bytecode.generic.LoadClass;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.CPInstruction;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.StackConsumer;
import lsieun.bytecode.generic.instruction.StackProducer;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.ArrayType;
import lsieun.bytecode.generic.type.ObjectType;
import lsieun.bytecode.generic.type.Type;

public class INSTANCEOF extends CPInstruction
        implements LoadClass, StackProducer, StackConsumer {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public INSTANCEOF() {
    }


    public INSTANCEOF(final int index) {
        super(OpcodeConst.INSTANCEOF, index);
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
        v.visitStackProducer(this);
        v.visitStackConsumer(this);
        v.visitTypedInstruction(this);
        v.visitCPInstruction(this);
        v.visitINSTANCEOF(this);
    }
}

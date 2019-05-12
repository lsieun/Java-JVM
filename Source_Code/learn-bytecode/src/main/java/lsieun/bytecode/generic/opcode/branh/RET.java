package lsieun.bytecode.generic.opcode.branh;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.JVMConst;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.IndexedInstruction;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.TypedInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.type.ReturnaddressType;
import lsieun.bytecode.generic.type.Type;

/**
 * RET - Return from subroutine
 *
 * <PRE>Stack: ... -&gt; ...</PRE>
 */
public class RET extends Instruction implements IndexedInstruction, TypedInstruction {
    private boolean wide;
    private int index; // index to local variable containg the return address


    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public RET() {
    }


    public RET(final int index) {
        super(OpcodeConst.RET, (short) 2);
        setIndex(index); // May set wide as side effect
    }

    private void setWide() {
        wide = index > JVMConst.MAX_BYTE;
        if (wide) {
            super.setLength(4); // Including the wide byte
        } else {
            super.setLength(2);
        }
    }

    /**
     * @return index of local variable containg the return address
     */
    @Override
    public final int getIndex() {
        return index;
    }


    /**
     * Set index of local variable containg the return address
     */
    @Override
    public final void setIndex(final int n) {
        if (n < 0) {
            throw new ClassGenException("Negative index value: " + n);
        }
        index = n;
        setWide();
    }


    /**
     * @return return address type
     */
    @Override
    public Type getType(final ConstantPoolGen cp) {
        return ReturnaddressType.NO_TARGET;
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
        v.visitRET(this);
    }
}

package lsieun.bytecode.generic.opcode.cst;

import java.io.IOException;

import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.utils.ByteDashboard;

/**
 * LDC_W - Push item from constant pool (wide index)
 *
 * <PRE>Stack: ... -&gt; ..., item.word1, item.word2</PRE>
 *
 */
public class LDC_W extends LDC {

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public LDC_W() {
    }


    public LDC_W(final int index) {
        super(index);
    }

    @Override
    protected void readFully(ByteDashboard byteDashboard, boolean wide) {
        int cpIndex = byteDashboard.nextShort();
        setIndex(cpIndex);
        // Override just in case it has been changed
        super.setOpcode(OpcodeConst.LDC_W);
        super.setLength(3);
    }
}

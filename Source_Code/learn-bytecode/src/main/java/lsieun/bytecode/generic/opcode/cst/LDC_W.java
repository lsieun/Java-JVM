package lsieun.bytecode.generic.opcode.cst;

import java.io.IOException;

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

}

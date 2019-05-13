package lsieun.bytecode.generic.instruction.facet;

import lsieun.bytecode.generic.instruction.ConstantPoolGen;

/**
 * Denote an instruction that may consume a value from the stack.
 */
public interface StackConsumer {

    /**
     * @return how many words are consumed from stack
     */
    int consumeStack(ConstantPoolGen cpg);
}

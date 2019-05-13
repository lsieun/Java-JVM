package lsieun.bytecode.generic.instruction.facet;

import lsieun.bytecode.generic.instruction.ConstantPoolGen;

/**
 * Denote an instruction that may produce a value on top of the stack
 * (this excludes DUP_X1, e.g.)
 *
 */
public interface StackProducer {
    /** @return how many words are produced on stack
     */
    int produceStack(ConstantPoolGen cpg);
}

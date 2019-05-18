package lsieun.bytecode.generic.instruction.facet;

import lsieun.bytecode.generic.ConstantPoolGen;
import lsieun.bytecode.generic.type.Type;

/**
 * Get the type associated with an instruction, int for ILOAD, or the type
 * of the field of a PUTFIELD instruction, e.g..
 *
 */
public interface TypedInstruction {
    Type getType(ConstantPoolGen cpg);
}

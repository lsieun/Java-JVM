package lsieun.bytecode.generic.opcode;

import lsieun.bytecode.generic.type.Type;

/**
 * Get the type associated with an instruction, int for ILOAD, or the type
 * of the field of a PUTFIELD instruction, e.g..
 */
public interface TypedInstruction {

    Type getType(ConstantPoolGen cpg);
}

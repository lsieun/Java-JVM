package lsieun.bytecode.generic.instruction;

import lsieun.bytecode.exceptions.ClassGenException;

/**
 * Denote that a class targets InstructionHandles within an InstructionList. Namely
 * the following implementers:
 *
 * @see BranchHandle
 * @see LocalVariableGen
 * @see CodeExceptionGen
 */
public interface InstructionTargeter {

    /**
     * Checks whether this targeter targets the specified instruction handle.
     */
    boolean containsTarget(InstructionHandle ih);

    /**
     * Replaces the target of this targeter from this old handle to the new handle.
     *
     * @param old_ih the old handle
     * @param new_ih the new handle
     * @throws ClassGenException if old_ih is not targeted by this object
     */
    void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) throws ClassGenException;
}

package lsieun.bytecode.generic.instruction.sub;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.InstructionList;
import lsieun.bytecode.generic.instruction.facet.InstructionTargeter;

/**
 * Abstract super class for branching instructions like GOTO, IFEQ, etc..
 * Branch instructions may have a variable length, namely GOTO, JSR,
 * LOOKUPSWITCH and TABLESWITCH.
 *
 * @see InstructionList
 */
public abstract class BranchInstruction extends Instruction implements InstructionTargeter {
    private int index; // Branch target relative to this instruction
    private InstructionHandle target; // Target object in instruction list
    private int position; // Byte code offset

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public BranchInstruction() {
    }


    /**
     * Common super constructor
     *
     * @param opcode Instruction opcode
     * @param target instruction to branch to
     */
    protected BranchInstruction(final short opcode, final InstructionHandle target) {
        super(opcode, (short) 3);
        setTarget(target);
    }

    /**
     * @return target offset in byte code
     */
    public final int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    protected void setIndex(final int index) {
        this.index = index;
    }

    /**
     * @return target of branch instruction
     */
    public InstructionHandle getTarget() {
        return target;
    }

    /**
     * Set branch target
     *
     * @param target branch target
     */
    public void setTarget(final InstructionHandle target) {
        notifyTarget(this.target, target, this);
        this.target = target;
    }

    /**
     * Used by BranchInstruction, LocalVariableGen, CodeExceptionGen, LineNumberGen
     */
    public static void notifyTarget(final InstructionHandle old_ih, final InstructionHandle new_ih,
                             final InstructionTargeter t) {
        if (old_ih != null) {
            old_ih.removeTargeter(t);
        }
        if (new_ih != null) {
            new_ih.addTargeter(t);
        }
    }

    /**
     * @param old_ih old target
     * @param new_ih new target
     */
    @Override
    public void updateTarget(final InstructionHandle old_ih, final InstructionHandle new_ih) {
        if (target == old_ih) {
            setTarget(new_ih);
        } else {
            throw new ClassGenException("Not targeting " + old_ih + ", but " + target);
        }
    }

    /**
     * @return true, if ih is target of this instruction
     */
    @Override
    public boolean containsTarget(final InstructionHandle ih) {
        return target == ih;
    }

    /**
     * @param _target branch target
     * @return the offset to  `target' relative to this instruction
     */
    public int getTargetOffset(final InstructionHandle _target) {
        if (_target == null) {
            throw new ClassGenException("Target of " + super.toString(true)
                    + " is invalid null handle");
        }
        final int t = _target.getPosition();
        if (t < 0) {
            throw new ClassGenException("Invalid branch target position offset for "
                    + super.toString(true) + ":" + t + ":" + _target);
        }
        return t - position;
    }

    /**
     * @return the offset to this instruction's target
     */
    public int getTargetOffset() {
        return getTargetOffset(target);
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(final int position) {
        this.position = position;
    }

    /**
     * Called by InstructionList.setPositions when setting the position for every
     * instruction. In the presence of variable length instructions `setPositions'
     * performs multiple passes over the instruction list to calculate the
     * correct (byte) positions and offsets by calling this function.
     *
     * @param offset     additional offset caused by preceding (variable length) instructions
     * @param max_offset the maximum offset that may be caused by these instructions
     * @return additional offset caused by possible change of this instruction's length
     */
    public int updatePosition(final int offset, final int max_offset) {
        position += offset;
        return 0;
    }

    /**
     * Inform target that it's not targeted anymore.
     */
    @Override
    public void dispose() {
        setTarget(null);
        index = -1;
        position = -1;
    }
}

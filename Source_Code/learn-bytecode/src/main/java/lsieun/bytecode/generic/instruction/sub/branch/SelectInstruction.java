package lsieun.bytecode.generic.instruction.sub.branch;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.InstructionList;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.facet.StackProducer;
import lsieun.bytecode.generic.instruction.facet.VariableLengthInstruction;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;
import lsieun.bytecode.utils.ByteDashboard;

/**
 * SelectInstruction - Abstract super class for LOOKUPSWITCH and TABLESWITCH instructions.
 *
 * <p>We use our super's <code>target</code> property as the default target.
 *
 * @version $Id$
 * @see LOOKUPSWITCH
 * @see TABLESWITCH
 * @see InstructionList
 */
public abstract class SelectInstruction extends BranchInstruction
        implements VariableLengthInstruction, StackConsumer, StackProducer {
    private int[] match; // matches, i.e., case 1: ...
    private int[] indices; // target offsets

    private InstructionHandle[] targets; // target objects in instruction list
    private int fixed_length; // fixed length defined by subclasses
    private int match_length; // number of cases
    protected int padding = 0; // number of pad bytes for alignment

    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public SelectInstruction() {
    }

    /**
     * (Match, target) pairs for switch.
     * `Match' and `targets' must have the same length of course.
     *
     * @param match array of matching values
     * @param targets instruction targets
     * @param defaultTarget default instruction target
     */
    public SelectInstruction(final short opcode, final int[] match, final InstructionHandle[] targets, final InstructionHandle defaultTarget) {
        // don't set default target before instuction is built
        super(opcode, null);
        this.match = match;
        this.targets = targets;
        // now it's safe to set default target
        setTarget(defaultTarget);
        for (final InstructionHandle target2 : targets) {
            notifyTarget(null, target2, this);
        }
        if ((match_length = match.length) != targets.length) {
            throw new ClassGenException("Match and target array have not the same length: Match length: " +
                    match.length + " Target length: " + targets.length);
        }
        indices = new int[match_length];
    }

    /**
     * @return array of match indices
     */
    public int[] getMatchs() {
        return match;
    }

    /**
     * @return match entry
     */
    final int getMatch(final int index) {
        return match[index];
    }

    /**
     * @return array of match target offsets
     */
    public int[] getIndices() {
        return indices;
    }

    /**
     * @return index entry from indices
     */
    public final int getIndices(final int index) {
        return indices[index];
    }


    public final int setIndices(final int i, final int value) {
        indices[i] = value;
        return value;  // Allow use in nested calls
    }

    /**
     * Since this is a variable length instruction, it may shift the following
     * instructions which then need to update their position.
     *
     * Called by InstructionList.setPositions when setting the position for every
     * instruction. In the presence of variable length instructions `setPositions'
     * performs multiple passes over the instruction list to calculate the
     * correct (byte) positions and offsets by calling this function.
     *
     * @param offset additional offset caused by preceding (variable length) instructions
     * @param max_offset the maximum offset that may be caused by these instructions
     * @return additional offset caused by possible change of this instruction's length
     */
    @Override
    public int updatePosition( final int offset, final int max_offset ) {
        setPosition(getPosition() + offset); // Additional offset caused by preceding SWITCHs, GOTOs, etc.
        final short old_length = (short) super.getLength();
        /* Alignment on 4-byte-boundary, + 1, because of tag byte.
         */
        padding = (4 - ((getPosition() + 1) % 4)) % 4;
        super.setLength((short) (fixed_length + padding)); // Update length
        return super.getLength() - old_length;
    }

    /**
     * Set branch target for `i'th case
     */
    public void setTarget( final int i, final InstructionHandle target ) { // TODO could be package-protected?
        notifyTarget(targets[i], target, this);
        targets[i] = target;
    }


    /**
     * @param old_ih old target
     * @param new_ih new target
     */
    @Override
    public void updateTarget( final InstructionHandle old_ih, final InstructionHandle new_ih ) {
        boolean targeted = false;
        if (super.getTarget() == old_ih) {
            targeted = true;
            setTarget(new_ih);
        }
        for (int i = 0; i < targets.length; i++) {
            if (targets[i] == old_ih) {
                targeted = true;
                setTarget(i, new_ih);
            }
        }
        if (!targeted) {
            throw new ClassGenException("Not targeting " + old_ih);
        }
    }

    /**
     * @return true, if ih is target of this instruction
     */
    @Override
    public boolean containsTarget( final InstructionHandle ih ) {
        if (super.getTarget() == ih) {
            return true;
        }
        for (final InstructionHandle target2 : targets) {
            if (target2 == ih) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return array of match targets
     */
    public InstructionHandle[] getTargets() {
        return targets;
    }

    /**
     * @return target entry
     */
    final InstructionHandle getTarget(final int index) {
        return targets[index];
    }

    /**
     * @return the fixed_length
     */
    final int getFixed_length() {
        return fixed_length;
    }

    /**
     * @param fixed_length the fixed_length to set
     */
    public final void setFixed_length(final int fixed_length) {
        this.fixed_length = fixed_length;
    }

    /**
     * @return the match_length
     */
    public final int getMatch_length() {
        return match_length;
    }


    /**
     * @param match_length the match_length to set
     */
    public final int setMatch_length(final int match_length) {
        this.match_length = match_length;
        return match_length;
    }

    /**
     *
     * @param index
     * @param value
     */
    public final void setMatch(final int index, final int value) {
        match[index] = value;
    }

    /**
     *
     * @param array
     */
    public final void setIndices(final int[] array) {
        indices = array;
    }

    /**
     *
     * @param array
     */
    public final void setMatches(final int[] array) {
        match = array;
    }

    /**
     *
     * @param array
     */
    public final void setTargets(final InstructionHandle[] array) {
        targets = array;
    }

    /**
     *
     * @return the padding
     */
    public final int getPadding() {
        return padding;
    }

    @Override
    protected void readFully(ByteDashboard byteDashboard, boolean wide) {
        padding = (4 - (byteDashboard.getIndex() % 4)) % 4; // Compute number of pad bytes
        for (int i = 0; i < padding; i++) {
            byteDashboard.readByte();
        }
        // Default branch target clazz for both cases (TABLESWITCH, LOOKUPSWITCH)
        super.setIndex(byteDashboard.readInt());
    }

    /**
     * Inform targets that they're not targeted anymore.
     */
    @Override
    public void dispose() {
        super.dispose();
        for (final InstructionHandle target2 : targets) {
            target2.removeTargeter(this);
        }
    }
}

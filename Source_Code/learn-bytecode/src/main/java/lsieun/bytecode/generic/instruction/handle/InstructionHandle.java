package lsieun.bytecode.generic.instruction.handle;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.InstructionList;
import lsieun.bytecode.generic.instruction.facet.InstructionTargeter;
import lsieun.bytecode.generic.instruction.Visitor;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;

/**
 * Instances of this class give users a handle to the instructions contained in
 * an InstructionList. Instruction objects may be used more than once within a
 * list, this is useful because it saves memory and may be much faster.
 * <p>
 * Within an InstructionList an InstructionHandle object is wrapped
 * around all instructions, i.e., it implements a cell in a
 * doubly-linked list. From the outside only the next and the
 * previous instruction (handle) are accessible. One
 * can traverse the list via an Enumeration returned by
 * InstructionList.elements().
 *
 * @see Instruction
 * @see BranchHandle
 * @see InstructionList
 */
public class InstructionHandle {
    private InstructionHandle next;
    private InstructionHandle prev;
    private Instruction instruction;

    protected int i_position = -1; // byte code offset of instruction

    private Set<InstructionTargeter> targeters;
    private Map<Object, Object> attributes;

    protected InstructionHandle(final Instruction i) {
        setInstruction(i);
    }

    public final InstructionHandle getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public final InstructionHandle setNext(final InstructionHandle next) {
        this.next = next;
        return next;
    }

    public final InstructionHandle getPrev() {
        return prev;
    }

    /**
     * @param prev the prev to set
     */
    public final InstructionHandle setPrev(final InstructionHandle prev) {
        this.prev = prev;
        return prev;
    }


    public final Instruction getInstruction() {
        return instruction;
    }

    /**
     * Replace current instruction contained in this handle.
     * Old instruction is disposed using Instruction.dispose().
     */
    public void setInstruction(final Instruction i) { // Overridden in BranchHandle TODO could be package-protected?
        if (i == null) {
            throw new ClassGenException("Assigning null to handle");
        }
        if ((this.getClass() != BranchHandle.class) && (i instanceof BranchInstruction)) {
            throw new ClassGenException("Assigning branch instruction " + i + " to plain handle");
        }
        if (instruction != null) {
            instruction.dispose();
        }
        instruction = i;
    }

    /**
     * Called by InstructionList.setPositions when setting the position for every
     * instruction. In the presence of variable length instructions `setPositions()'
     * performs multiple passes over the instruction list to calculate the
     * correct (byte) positions and offsets by calling this function.
     *
     * @param offset     additional offset caused by preceding (variable length) instructions
     * @param max_offset the maximum offset that may be caused by these instructions
     * @return additional offset caused by possible change of this instruction's length
     */
    public int updatePosition(final int offset, final int max_offset) {
        i_position += offset;
        return 0;
    }

    /**
     * @return the position, i.e., the byte code offset of the contained
     * instruction. This is accurate only after
     * InstructionList.setPositions() has been called.
     */
    public int getPosition() {
        return i_position;
    }

    /**
     * Set the position, i.e., the byte code offset of the contained
     * instruction.
     */
    public void setPosition(final int pos) {
        i_position = pos;
    }

    /**
     * Denote this handle is being referenced by t.
     */
    public void addTargeter(final InstructionTargeter t) {
        if (targeters == null) {
            targeters = new HashSet<>();
        }
        //if(!targeters.contains(t))
        targeters.add(t);
    }

    /**
     * Denote this handle isn't referenced anymore by t.
     */
    public void removeTargeter(final InstructionTargeter t) {
        if (targeters != null) {
            targeters.remove(t);
        }
    }

    public boolean hasTargeters() {
        return (targeters != null) && (targeters.size() > 0);
    }

    /**
     * @return null, if there are no targeters
     */
    public InstructionTargeter[] getTargeters() {
        if (!hasTargeters()) {
            return new InstructionTargeter[0];
        }
        final InstructionTargeter[] t = new InstructionTargeter[targeters.size()];
        targeters.toArray(t);
        return t;
    }

    /**
     * Remove all targeters, if any.
     */
    public void removeAllTargeters() {
        if (targeters != null) {
            targeters.clear();
        }
    }

    /**
     * Add an attribute to an instruction handle.
     *
     * @param key  the key object to store/retrieve the attribute
     * @param attr the attribute to associate with this handle
     */
    public void addAttribute(final Object key, final Object attr) {
        if (attributes == null) {
            attributes = new HashMap<>(3);
        }
        attributes.put(key, attr);
    }

    /**
     * Delete an attribute of an instruction handle.
     *
     * @param key the key object to retrieve the attribute
     */
    public void removeAttribute(final Object key) {
        if (attributes != null) {
            attributes.remove(key);
        }
    }

    /**
     * Get attribute of an instruction handle.
     *
     * @param key the key object to store/retrieve the attribute
     */
    public Object getAttribute(final Object key) {
        if (attributes != null) {
            return attributes.get(key);
        }
        return null;
    }


    /**
     * @return all attributes associated with this handle
     */
    public Collection<Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>(3);
        }
        return attributes.values();
    }

    /**
     * Delete contents, i.e., remove user access.
     */
    public void dispose() {
        next = prev = null;
        instruction.dispose();
        instruction = null;
        i_position = -1;
        attributes = null;
        removeAllTargeters();
    }


    /**
     * Factory method.
     */
    public static InstructionHandle getInstructionHandle(final Instruction i) {
        return new InstructionHandle(i);
    }

    /**
     * Convenience method, simply calls accept() on the contained instruction.
     *
     * @param v Visitor object
     */
    public void accept(final Visitor v) {
        instruction.accept(v);
    }
}

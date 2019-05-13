package lsieun.bytecode.generic.instruction;

import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.instruction.handle.BranchHandle;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.SelectInstruction;
import lsieun.bytecode.utils.ByteDashboard;

/**
 * This class is a container for a list of <a href="Instruction.html">Instruction</a> objects. Instructions can be appended, inserted, moved, deleted, etc..
 * Instructions are being wrapped into <a href="InstructionHandle.html">InstructionHandles</a> objects that are returned upon append/insert operations. They
 * give the user (read only) access to the list structure, such that it can be traversed and manipulated in a controlled way.
 *
 * A list is finally dumped to a byte code array with <a href="#getByteCode()">getByteCode</a>.
 *
 * @see Instruction
 * @see InstructionHandle
 * @see BranchHandle
 */
public class InstructionList /*implements Iterable<InstructionHandle>*/ {
    private InstructionHandle start = null;
    private InstructionHandle end = null;
    private int length = 0; // number of elements in list
    private int[] byte_positions; // byte code offsets corresponding to instructions

    /**
     * Create (empty) instruction list.
     */
    public InstructionList() {
    }


    /**
     * Initialize instruction list from byte array.
     *
     * @param code
     *            byte array containing the instructions
     */
    public InstructionList(final byte[] code) {
        int count = 0; // Contains actual length
        int[] pos;
        InstructionHandle[] ihs;
        ByteDashboard byteDashboard = new ByteDashboard("code", code);

        try{
            ihs = new InstructionHandle[code.length];
            pos = new int[code.length]; // Can't be more than that
            /*
             * Pass 1: Create an object for each byte code and append them to the list.
             */
            while (byteDashboard.hasNext()) {
                // Remember byte offset and associate it with the instruction
                final int off = byteDashboard.getIndex();
                pos[count] = off;
                /*
                 * Read one instruction from the byte stream, the byte position is set accordingly.
                 */
                final Instruction i = Instruction.readInstruction(byteDashboard);
                InstructionHandle ih;
                if (i instanceof BranchInstruction) {
                    ih = append((BranchInstruction) i);
                } else {
                    ih = append(i);
                }
                ih.setPosition(off);
                ihs[count] = ih;
                count++;
            }
        } catch (Exception e) {
            throw new ClassGenException(e.toString(), e);
        }
        byte_positions = new int[count]; // Trim to proper size
        System.arraycopy(pos, 0, byte_positions, 0, count);
        /*
         * Pass 2: Look for BranchInstruction and update their targets, i.e., convert offsets to instruction handles.
         */
        for (int i = 0; i < count; i++) {
            if (ihs[i] instanceof BranchHandle) {
                final BranchInstruction bi = (BranchInstruction) ihs[i].getInstruction();
                int target = bi.getPosition() + bi.getIndex(); /*
                 * Byte code position: relative -> absolute.
                 */
                // Search for target position
                InstructionHandle ih = findHandle(ihs, pos, count, target);
                if (ih == null) {
                    throw new ClassGenException("Couldn't find target for branch: " + bi);
                }
                bi.setTarget(ih); // Update target
                // If it is a SelectInstruction instruction, update all branch targets
                if (bi instanceof SelectInstruction) { // Either LOOKUPSWITCH or TABLESWITCH
                    final SelectInstruction s = (SelectInstruction) bi;
                    final int[] indices = s.getIndices();
                    for (int j = 0; j < indices.length; j++) {
                        target = bi.getPosition() + indices[j];
                        ih = findHandle(ihs, pos, count, target);
                        if (ih == null) {
                            throw new ClassGenException("Couldn't find target for switch: " + bi);
                        }
                        s.setTarget(j, ih); // Update target
                    }
                }
            }
        }
    }

    /**
     * Append a branch instruction to the end of this list.
     *
     * @param i
     *            branch instruction to append
     * @return branch instruction handle of the appended instruction
     */
    public BranchHandle append(final BranchInstruction i) {
        final BranchHandle ih = BranchHandle.getBranchHandle(i);
        append(ih);
        return ih;
    }

    /**
     * Append an instruction to the end of this list.
     *
     * @param i
     *            instruction to append
     * @return instruction handle of the appended instruction
     */
    public InstructionHandle append(final Instruction i) {
        final InstructionHandle ih = InstructionHandle.getInstructionHandle(i);
        append(ih);
        return ih;
    }

    /**
     * Append an instruction to the end of this list.
     *
     * @param ih
     *            instruction to append
     */
    private void append(final InstructionHandle ih) {
        if (isEmpty()) {
            ih.setPrev(null);
            ih.setNext(null);
            start = end = ih;
        } else {
            end.setNext(ih);

            ih.setPrev(end);
            ih.setNext(null);

            end = ih;
        }
        length++; // Update length
    }

    /**
     * Test for empty list.
     */
    public boolean isEmpty() {
        return start == null;
    } // && end == null

    /**
     * @return start of list
     */
    public InstructionHandle getStart() {
        return start;
    }

    /**
     * @return end of list
     */
    public InstructionHandle getEnd() {
        return end;
    }

    /**
     * @return length of list (Number of instructions, not bytes)
     */
    public int getLength() {
        return length;
    }

    /**
     * @return length of list (Number of instructions, not bytes)
     */
    public int size() {
        return length;
    }

    /**
     * Get instruction handle for instruction at byte code position pos. This only works properly, if the list is freshly initialized from a byte array or
     * setPositions() has been called before this method.
     *
     * @param pos
     *            byte code position to search for
     * @return target position's instruction handle if available
     */
    public InstructionHandle findHandle(final int pos) {
        final int[] positions = byte_positions;
        InstructionHandle ih = start;
        for (int i = 0; i < length; i++) {
            if (positions[i] == pos) {
                return ih;
            }
            ih = ih.getNext();
        }
        return null;
    }

    /**
     * GOOD_CODE: binary search
     *
     * Find the target instruction (handle) that corresponds to the given target position (byte code offset).
     *
     * @param ihs
     *            array of instruction handles, i.e. il.getInstructionHandles()
     * @param pos
     *            array of positions corresponding to ihs, i.e. il.getInstructionPositions()
     * @param count
     *            length of arrays
     * @param target
     *            target position to search for
     * @return target position's instruction handle if available
     */
    public static InstructionHandle findHandle(final InstructionHandle[] ihs, final int[] pos, final int count, final int target) {
        int left = 0;
        int right = count - 1;
        /*
         * Do a binary search since the pos array is orderd.
         */
        do {
            final int i = (left + right) / 2;
            final int j = pos[i];
            if (j == target) {
                return ihs[i];
            } else if (target < j) {
                right = i - 1;
            } else {
                left = i + 1;
            }
        } while (left <= right);
        return null;
    }
}

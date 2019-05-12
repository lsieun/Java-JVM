package lsieun.bytecode.generic.instruction;

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
     * Create instruction list containing one instruction.
     *
     * @param i
     *            initial instruction
     */
    public InstructionList(final Instruction i) {
//        append(i);
    }

    /**
     * Create instruction list containing one instruction.
     *
     * @param i
     *            initial instruction
     */
    public InstructionList(final BranchInstruction i) {
//        append(i);
    }

    /**
     * Test for empty list.
     */
    public boolean isEmpty() {
        return start == null;
    } // && end == null

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

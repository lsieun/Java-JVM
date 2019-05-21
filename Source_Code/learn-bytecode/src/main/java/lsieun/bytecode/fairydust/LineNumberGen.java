package lsieun.bytecode.fairydust;

import lsieun.bytecode.classfile.attrs.code.LineNumber;
import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.instruction.facet.InstructionTargeter;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;

/**
 * This class represents a line number within a method, i.e., give an instruction
 * a line number corresponding to the source code line.
 *
 * @version $Id$
 * @see MethodGen
 */
public class LineNumberGen implements InstructionTargeter {
    private InstructionHandle ih;
    private int src_line;

    /**
     * Create a line number.
     *
     * @param ih instruction handle to reference
     */
    public LineNumberGen(final InstructionHandle ih, final int src_line) {
        setInstruction(ih);
        setSourceLine(src_line);
    }

    // region getter and setter
    public InstructionHandle getInstruction() {
        return ih;
    }

    public void setInstruction(final InstructionHandle ih) { // TODO could be package-protected?
        if (ih == null) {
            throw new NullPointerException("InstructionHandle may not be null");
        }
        BranchInstruction.notifyTarget(this.ih, ih, this);
        this.ih = ih;
    }

    public void setSourceLine(final int src_line) { // TODO could be package-protected?
        this.src_line = src_line;
    }


    public int getSourceLine() {
        return src_line;
    }
    // endregion

    /**
     * Get LineNumber attribute .
     * <p>
     * This relies on that the instruction list has already been dumped to byte code or
     * or that the `setPositions' methods has been called for the instruction list.
     */
    public LineNumber getLineNumber() {
        return new LineNumber(ih.getPosition(), src_line);
    }

    /**
     * @return true, if ih is target of this line number
     */
    @Override
    public boolean containsTarget(final InstructionHandle ih) {
        return this.ih == ih;
    }

    /**
     * @param old_ih old target
     * @param new_ih new target
     */
    @Override
    public void updateTarget(final InstructionHandle old_ih, final InstructionHandle new_ih) {
        if (old_ih != ih) {
            throw new ClassGenException("Not targeting " + old_ih + ", but " + ih + "}");
        }
        setInstruction(new_ih);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new Error("Clone Not Supported"); // never happens
        }
    }
}

package lsieun.bytecode.verifier.structurals;

/**
 * This class represents a JVM execution frame; that means,
 * a local variable array and an operand stack.
 */
public class Frame {
    /**
     * For instance initialization methods, it is important to remember
     * which instance it is that is not initialized yet. It will be
     * initialized invoking another constructor later.
     * NULL means the instance already *is* initialized.
     */
    private static UninitializedObjectType _this;

    private final LocalVariables locals;
    private final OperandStack stack;

    public Frame(final int maxLocals, final int maxStack) {
        locals = new LocalVariables(maxLocals);
        stack = new OperandStack(maxStack);
    }

    public Frame(final LocalVariables locals, final OperandStack stack) {
        this.locals = locals;
        this.stack = stack;
    }

    public LocalVariables getLocals() {
        return locals;
    }

    public OperandStack getStack() {
        return stack;
    }

    // region clone
    @Override
    protected Object clone() {
        final Frame f = new Frame(locals.getClone(), stack.getClone());
        return f;
    }

    public Frame getClone() {
        return (Frame) clone();
    }
    // endregion

    // region hashcode and equals
    @Override
    public int hashCode() {
        return stack.hashCode() ^ locals.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Frame)) {
            return false; // implies "null" is non-equal.
        }
        final Frame f = (Frame) o;
        return this.stack.equals(f.stack) && this.locals.equals(f.locals);
    }
    // endregion

    @Override
    public String toString() {
        String s="Local Variables:\n";
        s += locals;
        s += "OperandStack:\n";
        s += stack;
        return s;
    }

    public static UninitializedObjectType getThis() {
        return _this;
    }

    public static void setThis(final UninitializedObjectType _this) {
        Frame._this = _this;
    }
}

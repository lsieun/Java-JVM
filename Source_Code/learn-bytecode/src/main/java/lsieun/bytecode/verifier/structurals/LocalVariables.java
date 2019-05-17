package lsieun.bytecode.verifier.structurals;

import lsieun.bytecode.exceptions.AssertionViolatedException;
import lsieun.bytecode.generic.type.Type;

/**
 * This class implements an array of local variables used for symbolic JVM
 * simulation.
 */
public class LocalVariables implements Cloneable {
    /** The Type[] containing the local variable slots. */
    private final Type[] locals;

    /**
     * Creates a new LocalVariables object.
     */
    public LocalVariables(final int maxLocals) {
        locals = new Type[maxLocals];
        for (int i=0; i<maxLocals; i++) {
            locals[i] = Type.UNKNOWN;
        }
    }

    /**
     * Returns the number of local variable slots this
     * LocalVariables instance has.
     */
    public int maxLocals() {
        return locals.length;
    }

    /**
     * Returns the type of the local variable slot i.
     */
    public Type get(final int i) {
        return locals[i];
    }

    /**
     * Sets a new Type for the given local variable slot.
     */
    public void set(final int i, final Type type) {
        if (type == Type.BYTE || type == Type.SHORT || type == Type.BOOLEAN || type == Type.CHAR) {
            throw new AssertionViolatedException("LocalVariables do not know about '"+type+"'. Use Type.INT instead.");
        }
        locals[i] = type;
    }

    /**
     * Replaces all occurences of u in this local variables set
     * with an "initialized" ObjectType.
     */
    public void initializeObject(final UninitializedObjectType u) {
        for (int i=0; i<locals.length; i++) {
            if (locals[i] == u) {
                locals[i] = u.getInitialized();
            }
        }
    }

    // region clone
    /**
     * Returns a deep copy of this object; i.e. the clone
     * operates on a new local variable array.
     * However, the Type objects in the array are shared.
     */
    @Override
    public Object clone() {
        final LocalVariables lvs = new LocalVariables(locals.length);
        for (int i=0; i<locals.length; i++) {
            lvs.locals[i] = this.locals[i];
        }
        return lvs;
    }

    /**
     * Returns a (correctly typed) clone of this object.
     * This is equivalent to ((LocalVariables) this.clone()).
     */
    public LocalVariables getClone() {
        return (LocalVariables) this.clone();
    }
    // endregion

    // region hashcode and equals
    /** @return a hash code value for the object.
     */
    @Override
    public int hashCode() { return locals.length; }

    /*
     * Fulfills the general contract of Object.equals().
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof LocalVariables)) {
            return false;
        }
        final LocalVariables lv = (LocalVariables) o;
        if (this.locals.length != lv.locals.length) {
            return false;
        }
        for (int i=0; i<this.locals.length; i++) {
            if (!this.locals[i].equals(lv.locals[i])) {
                //System.out.println(this.locals[i]+" is not "+lv.locals[i]);
                return false;
            }
        }
        return true;
    }
    // endregion

    /**
     * Returns a String representation of this object.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i=0; i<locals.length; i++) {
            sb.append(Integer.toString(i));
            sb.append(": ");
            sb.append(locals[i]);
            sb.append("\n");
        }
        return sb.toString();
    }
}

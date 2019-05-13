package lsieun.bytecode.generic.instruction.facet;

/**
 * Denotes an unparameterized instruction to pop a value on top from the stack,
 * such as ISTORE, POP, PUTSTATIC.
 *
 * @see ISTORE
 * @see POP
 */
public interface PopInstruction extends StackConsumer {
}

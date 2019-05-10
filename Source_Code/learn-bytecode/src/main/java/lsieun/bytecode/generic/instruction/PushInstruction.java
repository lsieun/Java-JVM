package lsieun.bytecode.generic.instruction;

/**
 * Denotes an unparameterized instruction to produce a value on top of the stack,
 * such as ILOAD, LDC, SIPUSH, DUP, ICONST, etc.
 *
 * @see ILOAD
 * @see ICONST
 * @see LDC
 * @see DUP
 * @see SIPUSH
 * @see GETSTATIC
 */
public interface PushInstruction extends StackProducer {
}

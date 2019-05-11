package lsieun.bytecode.generic.instruction;

/**
 * Denote entity that refers to an index, e.g. local variable instructions,
 * RET, CPInstruction, etc.
 *
 */
public interface IndexedInstruction {

    int getIndex();


    void setIndex( int index );
}

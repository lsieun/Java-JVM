package lsieun.bcel.classfile;

/**
 * Denote class to have an accept method();
 *
 */
public interface Node {
    void accept(Visitor obj);
}

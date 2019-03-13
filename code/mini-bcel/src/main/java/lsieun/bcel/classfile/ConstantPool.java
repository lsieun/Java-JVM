package lsieun.bcel.classfile;

/**
 * This class represents the constant pool, i.e., a table of constants, of
 * a parsed classfile. It may contain null references, due to the JVM
 * specification that skips an entry after an 8-byte constant (double,
 * long) entry.
 *
 * @see     Constant
 */
public class ConstantPool implements Node {
    @Override
    public void accept(Visitor obj) {
        //FIXME: 还没有实现呢
    }
}

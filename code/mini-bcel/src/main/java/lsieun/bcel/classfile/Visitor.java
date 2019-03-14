package lsieun.bcel.classfile;

import lsieun.bcel.classfile.attributes.CodeException;
import lsieun.bcel.classfile.attributes.ConstantValue;
import lsieun.bcel.classfile.attributes.SourceFile;
import lsieun.bcel.classfile.cp.ConstantClass;
import lsieun.bcel.classfile.cp.ConstantDouble;
import lsieun.bcel.classfile.cp.ConstantDynamic;
import lsieun.bcel.classfile.cp.ConstantFieldref;
import lsieun.bcel.classfile.cp.ConstantFloat;
import lsieun.bcel.classfile.cp.ConstantInteger;
import lsieun.bcel.classfile.cp.ConstantInterfaceMethodref;
import lsieun.bcel.classfile.cp.ConstantInvokeDynamic;
import lsieun.bcel.classfile.cp.ConstantLong;
import lsieun.bcel.classfile.cp.ConstantMethodHandle;
import lsieun.bcel.classfile.cp.ConstantMethodType;
import lsieun.bcel.classfile.cp.ConstantMethodref;
import lsieun.bcel.classfile.cp.ConstantModule;
import lsieun.bcel.classfile.cp.ConstantNameAndType;
import lsieun.bcel.classfile.cp.ConstantPackage;
import lsieun.bcel.classfile.cp.ConstantString;
import lsieun.bcel.classfile.cp.ConstantUtf8;

/**
 * Interface to make use of the Visitor pattern programming style. I.e. a class
 * that implements this interface can traverse the contents of a Java class just
 * by calling the `accept' method which all classes have.
 *
 */
public interface Visitor {

    void visitJavaClass(JavaClass obj);

    void visitConstantPool(ConstantPool obj);

    // region constants
    void visitConstantUtf8(ConstantUtf8 obj);

    void visitConstantInteger(ConstantInteger obj);

    void visitConstantFloat(ConstantFloat obj);

    void visitConstantLong(ConstantLong obj);

    void visitConstantDouble(ConstantDouble obj);

    void visitConstantClass(ConstantClass obj);

    void visitConstantString(ConstantString obj);

    void visitConstantFieldref(ConstantFieldref obj);

    void visitConstantMethodref(ConstantMethodref obj);

    void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj);

    void visitConstantNameAndType(ConstantNameAndType obj);

    void visitConstantMethodHandle(ConstantMethodHandle obj);

    void visitConstantMethodType(ConstantMethodType obj);

    default void visitConstantDynamic(ConstantDynamic constantDynamic) {
        // empty
        // TODO：整理知识“接口的default”
    }

    void visitConstantInvokeDynamic(ConstantInvokeDynamic obj);

    void visitConstantModule(ConstantModule constantModule);

    void visitConstantPackage(ConstantPackage constantPackage);
    // endregion

    // region attributes
    void visitConstantValue(ConstantValue obj);

    void visitSourceFile(SourceFile obj);

    void visitCodeException(CodeException obj);
    // endregion
}

package lsieun.bcel.classfile;

import lsieun.bcel.classfile.attributes.AnnotationDefault;
import lsieun.bcel.classfile.attributes.AnnotationEntry;
import lsieun.bcel.classfile.attributes.Annotations;
import lsieun.bcel.classfile.attributes.BootstrapMethods;
import lsieun.bcel.classfile.attributes.Code;
import lsieun.bcel.classfile.attributes.CodeException;
import lsieun.bcel.classfile.attributes.ConstantValue;
import lsieun.bcel.classfile.attributes.Deprecated;
import lsieun.bcel.classfile.attributes.EnclosingMethod;
import lsieun.bcel.classfile.attributes.ExceptionTable;
import lsieun.bcel.classfile.attributes.InnerClass;
import lsieun.bcel.classfile.attributes.InnerClasses;
import lsieun.bcel.classfile.attributes.LineNumber;
import lsieun.bcel.classfile.attributes.LineNumberTable;
import lsieun.bcel.classfile.attributes.LocalVariable;
import lsieun.bcel.classfile.attributes.LocalVariableTable;
import lsieun.bcel.classfile.attributes.LocalVariableTypeTable;
import lsieun.bcel.classfile.attributes.MethodParameters;
import lsieun.bcel.classfile.attributes.ParameterAnnotationEntry;
import lsieun.bcel.classfile.attributes.ParameterAnnotations;
import lsieun.bcel.classfile.attributes.Signature;
import lsieun.bcel.classfile.attributes.SourceFile;
import lsieun.bcel.classfile.attributes.StackMap;
import lsieun.bcel.classfile.attributes.StackMapEntry;
import lsieun.bcel.classfile.attributes.Synthetic;
import lsieun.bcel.classfile.attributes.Unknown;
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

    void visitField(Field obj);

    void visitMethod(Method obj);

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

    // region ClassFile
    void visitBootstrapMethods(BootstrapMethods obj);

    void visitEnclosingMethod(EnclosingMethod obj);

    void visitInnerClass(InnerClass obj);

    void visitInnerClasses(InnerClasses obj);

    void visitSourceFile(SourceFile obj);
    // endregion

    // region field_info
    void visitConstantValue(ConstantValue obj);
    // endregion

    // region method_info
    void visitAnnotationDefault(AnnotationDefault obj);

    void visitCode(Code obj);

    void visitExceptionTable(ExceptionTable obj);

    void visitMethodParameters(MethodParameters obj);

    void visitParameterAnnotation(ParameterAnnotations obj);

    void visitParameterAnnotationEntry(ParameterAnnotationEntry obj);
    // endregion

    // region Code
    void visitCodeException(CodeException obj);

    void visitLineNumber(LineNumber obj);

    void visitLineNumberTable(LineNumberTable obj);

    void visitLocalVariable(LocalVariable obj);

    void visitLocalVariableTable(LocalVariableTable obj);

    void visitLocalVariableTypeTable(LocalVariableTypeTable obj);

    void visitStackMap(StackMap obj);

    void visitStackMapEntry(StackMapEntry obj);
    // endregion

    void visitAnnotation(Annotations obj);

    void visitAnnotationEntry(AnnotationEntry obj);

    void visitDeprecated(Deprecated obj);

    void visitSignature(Signature obj);

    void visitSynthetic(Synthetic obj);

    void visitUnknown(Unknown obj);
    // endregion
}

package lsieun.bcel.classfile;

import java.io.DataInput;
import java.io.IOException;

import lsieun.bcel.classfile.attributes.Attribute;
import lsieun.bcel.classfile.attributes.Code;
import lsieun.bcel.classfile.attributes.ExceptionTable;
import lsieun.bcel.classfile.attributes.LineNumberTable;
import lsieun.bcel.classfile.attributes.LocalVariableTable;
import lsieun.bcel.classfile.attributes.ParameterAnnotationEntry;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;
import lsieun.bcel.exceptions.ClassFormatException;

/**
 * This class represents the method info structure, i.e., the representation
 * for a method in the class. See JVM specification for details.
 * A method has access flags, a name, a signature and a number of attributes.
 *
 */
public final class Method extends FieldOrMethod {
    // annotations defined on the parameters of a method
    private ParameterAnnotationEntry[] parameterAnnotationEntries;

    /**
     * Construct object from file stream.
     * @param file Input stream
     * @throws IOException
     * @throws ClassFormatException
     */
    public Method(final DataInput file, final ConstantPool constant_pool) throws IOException,
            ClassFormatException {
        super(file, constant_pool);
    }


    /**
     * @param access_flags Access rights of method
     * @param name_index Points to field name in constant pool
     * @param signature_index Points to encoded signature
     * @param attributes Collection of attributes
     * @param constant_pool Array of constants
     */
    public Method(final int access_flags, final int name_index, final int signature_index, final Attribute[] attributes,
                  final ConstantPool constant_pool) {
        super(access_flags, name_index, signature_index, attributes, constant_pool);
    }

    /**
     * @return Code attribute of method, if any
     */
    public final Code getCode() {
        for (final Attribute attribute : super.getAttributes()) {
            if (attribute instanceof Code) {
                return (Code) attribute;
            }
        }
        return null;
    }

    /**
     * @return ExceptionTable attribute of method, if any, i.e., list all
     * exceptions the method may throw not exception handlers!
     */
    public final ExceptionTable getExceptionTable() {
        for (final Attribute attribute : super.getAttributes()) {
            if (attribute instanceof ExceptionTable) {
                return (ExceptionTable) attribute;
            }
        }
        return null;
    }

    /** @return LocalVariableTable of code attribute if any, i.e. the call is forwarded
     * to the Code atribute.
     */
    public final LocalVariableTable getLocalVariableTable() {
        final Code code = getCode();
        if (code == null) {
            return null;
        }
        return code.getLocalVariableTable();
    }


    /** @return LineNumberTable of code attribute if any, i.e. the call is forwarded
     * to the Code atribute.
     */
    public final LineNumberTable getLineNumberTable() {
        final Code code = getCode();
        if (code == null) {
            return null;
        }
        return code.getLineNumberTable();
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public void accept(final Visitor v) {
        v.visitMethod(this);
    }

    /**
     * Return string representation close to declaration format,
     * `public static void main(String[] args) throws IOException', e.g.
     *
     * @return String representation of the method.
     */
    @Override
    public final String toString() {
        final String access = Utility.accessToString(super.getAccessFlags());
        // Get name and signature from constant pool
        ConstantUtf8 c = (ConstantUtf8) super.getConstantPool().getConstant(super.getSignatureIndex(), CPConst.CONSTANT_Utf8);
        String signature = c.getBytes();
        c = (ConstantUtf8) super.getConstantPool().getConstant(super.getNameIndex(), CPConst.CONSTANT_Utf8);
        final String name = c.getBytes();
        signature = Utility.methodSignatureToString(signature, name, access, true,
                getLocalVariableTable());
        final StringBuilder buf = new StringBuilder(signature);
        for (final Attribute attribute : super.getAttributes()) {
            if (!((attribute instanceof Code) || (attribute instanceof ExceptionTable))) {
                buf.append(" [").append(attribute).append("]");
            }
        }
        final ExceptionTable e = getExceptionTable();
        if (e != null) {
            final String str = e.toString();
            if (!str.isEmpty()) {
                buf.append("\n\t\tthrows ").append(str);
            }
        }
        return buf.toString();
    }
}

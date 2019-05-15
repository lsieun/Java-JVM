package lsieun.bytecode.generic.instruction.sub.cp;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.cp.ConstantNameAndType;
import lsieun.bytecode.classfile.cp.ConstantRef;
import lsieun.bytecode.classfile.cp.ConstantUtf8;
import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.LoadClass;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.generic.instruction.sub.CPInstruction;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.type.ArrayType;
import lsieun.bytecode.generic.type.ObjectType;
import lsieun.bytecode.generic.type.ReferenceType;
import lsieun.bytecode.generic.type.Type;

/**
 * Super class for InvokeInstruction and FieldInstruction, since they have
 * some methods in clazz!
 */
public abstract class FieldOrMethod extends CPInstruction implements LoadClass {
    /**
     * Empty constructor needed for Instruction.readInstruction.
     * Not to be used otherwise.
     */
    public FieldOrMethod() {
    }

    /**
     * @param index to constant pool
     */
    protected FieldOrMethod(final short opcode, final int index) {
        super(opcode, index);
    }

    /**
     * @return signature of referenced method/field.
     */
    public String getSignature(final ConstantPoolGen cpg) {
        final ConstantPool cp = cpg.getConstantPool();
        final ConstantRef cmr = (ConstantRef) cp.getConstant(super.getIndex());
        final ConstantNameAndType cnat = (ConstantNameAndType) cp.getConstant(cmr.getNameAndTypeIndex());
        return ((ConstantUtf8) cp.getConstant(cnat.getDescriptorIndex())).getUtf8Value();
    }

    /** @return name of referenced method/field.
     */
    public String getName( final ConstantPoolGen cpg ) {
        final ConstantPool cp = cpg.getConstantPool();
        final ConstantRef cmr = (ConstantRef) cp.getConstant(super.getIndex());
        final ConstantNameAndType cnat = (ConstantNameAndType) cp.getConstant(cmr.getNameAndTypeIndex());
        return ((ConstantUtf8) cp.getConstant(cnat.getNameIndex())).getUtf8Value();
    }

    /**
     * Return the reference type representing the class, interface,
     * or array class referenced by the instruction.
     * @param cpg the ConstantPoolGen used to create the instruction
     * @return an ObjectType (if the referenced class type is a class
     *   or interface), or an ArrayType (if the referenced class
     *   type is an array class)
     */
    public ReferenceType getReferenceType(final ConstantPoolGen cpg ) {
        final ConstantPool cp = cpg.getConstantPool();
        final ConstantRef cmr = (ConstantRef) cp.getConstant(super.getIndex());
        String className = cp.getConstantString(cmr.getClassIndex(), CPConst.CONSTANT_Class);
        if (className.startsWith("[")) {
            return (ArrayType) Type.getType(className);
        }
        className = className.replace('/', '.');
        return ObjectType.getInstance(className);
    }

    /**
     * Get the ObjectType of the method return or field.
     *
     * @return type of the referenced class/interface
     * @throws ClassGenException when the field is (or method returns) an array,
     */
    @Override
    public ObjectType getLoadClassType(final ConstantPoolGen cpg ) {
        final ReferenceType rt = getReferenceType(cpg);
        if(rt instanceof ObjectType) {
            return (ObjectType)rt;
        }
        throw new ClassGenException(rt.getSignature() + " does not represent an ObjectType");
    }
}

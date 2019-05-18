package lsieun.bytecode.generic;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.generic.cst.TypeConst;
import lsieun.bytecode.generic.type.Type;

/**
 * Super class for FieldGen and MethodGen objects, since they have
 * some methods in common!
 */
public abstract class FieldGenOrMethodGen implements Cloneable {
    private int access_flags;
    private String name;
    private Type type;
    private ConstantPoolGen cp;
    private final List<AttributeInfo> attribute_vec = new ArrayList();

    protected FieldGenOrMethodGen() {
    }

    protected FieldGenOrMethodGen(final int access_flags) {
        this.access_flags = access_flags;
    }

    public int getAccessFlags() {
        return access_flags;
    }

    public void setAccessFlags(int access_flags) {
        this.access_flags = access_flags;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        if (type.getType() == TypeConst.T_ADDRESS) {
            throw new IllegalArgumentException("Type can not be " + type);
        }
        this.type = type;
    }

    public ConstantPoolGen getConstantPool() {
        return cp;
    }


    public void setConstantPool(final ConstantPoolGen cp) {
        this.cp = cp;
    }

    public void addAttribute( final AttributeInfo a ) {
        attribute_vec.add(a);
    }

    public void removeAttribute( final AttributeInfo a ) {
        attribute_vec.remove(a);
    }

    public void removeAttributes() {
        attribute_vec.clear();
    }

    public AttributeInfo[] getAttributes() {
        final AttributeInfo[] attributes = new AttributeInfo[attribute_vec.size()];
        attribute_vec.toArray(attributes);
        return attributes;
    }

    public abstract String getSignature();

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new Error("Clone Not Supported"); // never happens
        }
    }
}

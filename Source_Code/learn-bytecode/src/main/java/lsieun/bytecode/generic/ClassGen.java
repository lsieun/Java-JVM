package lsieun.bytecode.generic;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.FieldInfo;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.attrs.classfile.SourceFile;
import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.generic.cst.JDKConst;
import lsieun.bytecode.generic.instruction.InstructionConst;
import lsieun.bytecode.generic.instruction.InstructionList;
import lsieun.bytecode.generic.opcode.invoke.INVOKESPECIAL;
import lsieun.bytecode.generic.type.Type;

/**
 * Template class for building up a java class. May be initialized with an
 * existing java class (file).
 */
public class ClassGen {
    /* Corresponds to the fields found in a JavaClass object.
     */
    private int access_flags;
    private String class_name;
    private String super_class_name;
    private final String file_name;
    private int class_name_index = -1;
    private int superclass_name_index = -1;
    private int major = JDKConst.JAVA_8;
    private int minor = 0;
    private ConstantPoolGen cp; // Template for building up constant pool
    // ArrayLists instead of arrays to gather fields, methods, etc.
    private final List<FieldInfo> field_vec = new ArrayList();
    private final List<MethodInfo> method_vec = new ArrayList();
    private final List<AttributeInfo> attribute_vec = new ArrayList();
    private final List<String> interface_vec = new ArrayList();

    /**
     * Convenience constructor to set up some important values initially.
     *
     * @param class_name       fully qualified class name
     * @param super_class_name fully qualified superclass name
     * @param file_name        source file name
     * @param access_flags     access qualifiers
     * @param interfaces       implemented interfaces
     * @param cp               constant pool to use
     */
    public ClassGen(final String class_name, final String super_class_name, final String file_name, final int access_flags,
                    final String[] interfaces, final ConstantPoolGen cp) {
        this.access_flags = access_flags;
        this.class_name = class_name;
        this.super_class_name = super_class_name;
        this.file_name = file_name;
        this.cp = cp;
        // Put everything needed by default into the constant pool and the vectors
        if (file_name != null) {
            addAttribute(new SourceFile(cp.addUtf8("SourceFile"), 2, cp.addUtf8(file_name), cp
                    .getConstantPool()));
        }
        class_name_index = cp.addClass(class_name);
        superclass_name_index = cp.addClass(super_class_name);
        if (interfaces != null) {
            for (final String interface1 : interfaces) {
                addInterface(interface1);
            }
        }
    }

    /**
     * Convenience constructor to set up some important values initially.
     *
     * @param class_name       fully qualified class name
     * @param super_class_name fully qualified superclass name
     * @param file_name        source file name
     * @param access_flags     access qualifiers
     * @param interfaces       implemented interfaces
     */
    public ClassGen(final String class_name, final String super_class_name,
                    final String file_name,
                    final int access_flags,
                    final String[] interfaces) {
        this(class_name, super_class_name, file_name, access_flags, interfaces, new ConstantPoolGen());
    }

    /**
     * Add an interface to this class, i.e., this class has to implement it.
     *
     * @param name interface to implement (fully qualified class name)
     */
    public void addInterface(final String name) {
        interface_vec.add(name);
    }

    /**
     * Remove an interface from this class.
     *
     * @param name interface to remove (fully qualified name)
     */
    public void removeInterface(final String name) {
        interface_vec.remove(name);
    }

    /**
     * @return major version number of class file
     */
    public int getMajor() {
        return major;
    }


    /**
     * Set major version number of class file, default value is 45 (JDK 1.1)
     *
     * @param major major version number
     */
    public void setMajor(final int major) { // TODO could be package-protected - only called by test code
        this.major = major;
    }


    /**
     * Set minor version number of class file, default value is 3 (JDK 1.1)
     *
     * @param minor minor version number
     */
    public void setMinor(final int minor) {  // TODO could be package-protected - only called by test code
        this.minor = minor;
    }

    /**
     * @return minor version number of class file
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Add an attribute to this class.
     *
     * @param attr attribute to add
     */
    public void addAttribute(final AttributeInfo attr) {
        attribute_vec.add(attr);
    }

    /**
     * Add a method to this class.
     *
     * @param m method to add
     */
    public void addMethod(final MethodInfo m) {
        method_vec.add(m);
    }

    /**
     * Convenience method.
     *
     * Add an empty constructor to this class that does nothing but calling super().
     * @param access_flags rights for constructor
     */
    public void addEmptyConstructor( final int access_flags ) {
        final InstructionList il = new InstructionList();
        il.append(InstructionConst.THIS); // Push `this'
        il.append(new INVOKESPECIAL(cp.addMethodref(super_class_name, "<init>", "()V")));
        il.append(InstructionConst.RETURN);
        final MethodGen mg = new MethodGen(access_flags, Type.VOID, Type.NO_ARGS, null, "<init>",
                class_name, il, cp);
        mg.setMaxStack(1);
        addMethod(mg.getMethod());
    }

    /**
     * Add a field to this class.
     * @param f field to add
     */
    public void addField( final FieldInfo f ) {
        field_vec.add(f);
    }


    public boolean containsField( final FieldInfo f ) {
        return field_vec.contains(f);
    }

    /** @return field object with given name, or null
     */
    public FieldInfo containsField( final String name ) {
        for (final FieldInfo f : field_vec) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    /** @return method object with given name and signature, or null
     */
    public MethodInfo containsMethod( final String name, final String signature ) {
        for (final MethodInfo m : method_vec) {
            if (m.getName().equals(name) && m.getSignature().equals(signature)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Remove an attribute from this class.
     * @param a attribute to remove
     */
    public void removeAttribute( final AttributeInfo a ) {
        attribute_vec.remove(a);
    }


    /**
     * Remove a method from this class.
     * @param m method to remove
     */
    public void removeMethod( final MethodInfo m ) {
        method_vec.remove(m);
    }

    /** Replace given method with new one. If the old one does not exist
     * add the new_ method to the class anyway.
     */
    public void replaceMethod( final MethodInfo old, final MethodInfo new_ ) {
        if (new_ == null) {
            throw new ClassGenException("Replacement method must not be null");
        }
        final int i = method_vec.indexOf(old);
        if (i < 0) {
            method_vec.add(new_);
        } else {
            method_vec.set(i, new_);
        }
    }


    /** Replace given field with new one. If the old one does not exist
     * add the new_ field to the class anyway.
     */
    public void replaceField( final FieldInfo old, final FieldInfo new_ ) {
        if (new_ == null) {
            throw new ClassGenException("Replacement method must not be null");
        }
        final int i = field_vec.indexOf(old);
        if (i < 0) {
            field_vec.add(new_);
        } else {
            field_vec.set(i, new_);
        }
    }

    /**
     * Remove a field to this class.
     * @param f field to remove
     */
    public void removeField( final FieldInfo f ) {
        field_vec.remove(f);
    }


    public String getClassName() {
        return class_name;
    }


    public String getSuperclassName() {
        return super_class_name;
    }


    public String getFileName() {
        return file_name;
    }


    public void setClassName( final String name ) {
        class_name = name.replace('/', '.');
        class_name_index = cp.addClass(name);
    }


    public void setSuperclassName( final String name ) {
        super_class_name = name.replace('/', '.');
        superclass_name_index = cp.addClass(name);
    }

    public MethodInfo[] getMethods() {
        return method_vec.toArray(new MethodInfo[method_vec.size()]);
    }


    public void setMethods( final MethodInfo[] methods ) {
        method_vec.clear();
        for (final MethodInfo method : methods) {
            addMethod(method);
        }
    }

    public void setMethodAt( final MethodInfo method, final int pos ) {
        method_vec.set(pos, method);
    }


    public MethodInfo getMethodAt( final int pos ) {
        return method_vec.get(pos);
    }

    public String[] getInterfaceNames() {
        final int size = interface_vec.size();
        final String[] interfaces = new String[size];
        interface_vec.toArray(interfaces);
        return interfaces;
    }


    public int[] getInterfaces() {
        final int size = interface_vec.size();
        final int[] interfaces = new int[size];
        for (int i = 0; i < size; i++) {
            interfaces[i] = cp.addClass(interface_vec.get(i));
        }
        return interfaces;
    }

    public FieldInfo[] getFields() {
        return field_vec.toArray(new FieldInfo[field_vec.size()]);
    }


    public AttributeInfo[] getAttributes() {
        return attribute_vec.toArray(new AttributeInfo[attribute_vec.size()]);
    }

    public ConstantPoolGen getConstantPool() {
        return cp;
    }


    public void setConstantPool( final ConstantPoolGen constant_pool ) {
        cp = constant_pool;
    }


    public void setClassNameIndex( final int class_name_index ) {
        this.class_name_index = class_name_index;
        class_name = cp.getConstantPool().getConstantString(class_name_index,
                CPConst.CONSTANT_Class).replace('/', '.');
    }


    public void setSuperclassNameIndex( final int superclass_name_index ) {
        this.superclass_name_index = superclass_name_index;
        super_class_name = cp.getConstantPool().getConstantString(superclass_name_index,
                CPConst.CONSTANT_Class).replace('/', '.');
    }


    public int getSuperclassNameIndex() {
        return superclass_name_index;
    }


    public int getClassNameIndex() {
        return class_name_index;
    }

}

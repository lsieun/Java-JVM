package lsieun.bcel.classfile;

import java.util.StringTokenizer;

import lsieun.bcel.classfile.attributes.AnnotationEntry;
import lsieun.bcel.classfile.attributes.Attribute;
import lsieun.bcel.classfile.attributes.InnerClass;
import lsieun.bcel.classfile.attributes.InnerClasses;
import lsieun.bcel.classfile.attributes.SourceFile;
import lsieun.bcel.classfile.consts.CPConst;

public class JavaClass implements Node {
    private static final boolean debug = Boolean.getBoolean("JavaClass.debug"); // Debugging on/off
    public static final byte HEAP = 1;
    public static final byte FILE = 2;
    public static final byte ZIP = 3;

    /* Print debug information depending on `JavaClass.debug'
     */
    static void Debug( final String str ) {
        if (debug) {
            System.out.println(str);
        }
    }

    private String file_name;
    private byte source = HEAP; // Generated in memory


    private int major;
    private int minor; // Compiler version

    private ConstantPool constant_pool; // Constant pool

    private int access_flags;
    private int class_name_index;
    private int superclass_name_index;
    private String class_name;
    private String superclass_name;

    private int[] interfaces; // implemented interfaces
    private String[] interface_names;

    private Field[] fields; // Fields, i.e., variables of class
    private Method[] methods; // methods defined in the class
    private Attribute[] attributes; // attributes defined in the class

    private AnnotationEntry[] annotations;   // annotations defined on the class
    private String package_name;
    private String source_file_name = "<Unknown>";
    private boolean isAnonymous = false;
    private boolean isNested = false;
    // FIXME: 不知道是做什么用的，如果没有用，那么就删除掉
    private boolean computedNestedTypeStatus = false;

    /**
     * Constructor gets all contents as arguments.
     *
     * @param class_name_index Index into constant pool referencing a
     * ConstantClass that represents this class.
     * @param superclass_name_index Index into constant pool referencing a
     * ConstantClass that represents this class's superclass.
     * @param file_name File name
     * @param major Major compiler version
     * @param minor Minor compiler version
     * @param access_flags Access rights defined by bit flags
     * @param constant_pool Array of constants
     * @param interfaces Implemented interfaces
     * @param fields Class fields
     * @param methods Class methods
     * @param attributes Class attributes
     * @param source Read from file or generated in memory?
     */
    public JavaClass(final int class_name_index, final int superclass_name_index, final String file_name, final int major,
                     final int minor, final int access_flags, final ConstantPool constant_pool, int[] interfaces,
                     Field[] fields, Method[] methods, Attribute[] attributes, final byte source) {
        this.access_flags = access_flags;
        if (interfaces == null) {
            interfaces = new int[0];
        }
        if (attributes == null) {
            attributes = new Attribute[0];
        }
        if (fields == null) {
            fields = new Field[0];
        }
        if (methods == null) {
            methods = new Method[0];
        }
        this.class_name_index = class_name_index;
        this.superclass_name_index = superclass_name_index;
        this.file_name = file_name;
        this.major = major;
        this.minor = minor;
        this.constant_pool = constant_pool;
        this.interfaces = interfaces;
        this.fields = fields;
        this.methods = methods;
        this.attributes = attributes;
        this.source = source;
        // Get source file name if available
        for (final Attribute attribute : attributes) {
            if (attribute instanceof SourceFile) {
                source_file_name = ((SourceFile) attribute).getSourceFileName();
                break;
            }
        }
        /* According to the specification the following entries must be of type
         * `ConstantClass' but we check that anyway via the
         * `ConstPool.getConstant' method.
         */
        class_name = constant_pool.getConstantString(class_name_index, CPConst.CONSTANT_Class);
        class_name = Utility.compactClassName(class_name, false);
        final int index = class_name.lastIndexOf('.');
        if (index < 0) {
            package_name = "";
        } else {
            package_name = class_name.substring(0, index);
        }
        if (superclass_name_index > 0) {
            // May be zero -> class is java.lang.Object
            superclass_name = constant_pool.getConstantString(superclass_name_index, CPConst.CONSTANT_Class);
            superclass_name = Utility.compactClassName(superclass_name, false);
        } else {
            superclass_name = "java.lang.Object";
        }
        interface_names = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            final String str = constant_pool.getConstantString(interfaces[i], CPConst.CONSTANT_Class);
            interface_names[i] = Utility.compactClassName(str, false);
        }
    }

    /**
     * @return Major number of class file version.
     */
    public int getMajor() {
        return major;
    }

    /**
     * @return Minor number of class file version.
     */
    public int getMinor() {
        return minor;
    }

    /**
     * @return Constant pool.
     */
    public ConstantPool getConstantPool() {
        return constant_pool;
    }

    /**
     * @return Access flags of the object aka. "modifiers".
     */
    public final int getAccessFlags() {
        return access_flags;
    }

    /**
     * @return Class name index.
     */
    public int getClassNameIndex() {
        return class_name_index;
    }

    /**
     * @return Class name.
     */
    public String getClassName() {
        return class_name;
    }

    /**
     * @return Class name index.
     */
    public int getSuperclassNameIndex() {
        return superclass_name_index;
    }

    /**
     * returns the super class name of this class. In the case that this class is
     * java.lang.Object, it will return itself (java.lang.Object). This is probably incorrect
     * but isn't fixed at this time to not break existing clients.
     *
     * @return Superclass name.
     */
    public String getSuperclassName() {
        return superclass_name;
    }

    /**
     * @return Indices in constant pool of implemented interfaces.
     */
    public int[] getInterfaceIndices() {
        return interfaces;
    }

    /**
     * @return Names of implemented interfaces.
     */
    public String[] getInterfaceNames() {
        return interface_names;
    }

    /**
     * @return Fields, i.e., variables of the class. Like the JVM spec
     * mandates for the classfile format, these fields are those specific to
     * this class, and not those of the superclass or superinterfaces.
     */
    public Field[] getFields() {
        return fields;
    }

    /**
     * @return Methods of the class.
     */
    public Method[] getMethods() {
        return methods;
    }

    /**
     * @return Attributes of the class.
     */
    public Attribute[] getAttributes() {
        return attributes;
    }

    /**
     * @return Annotations on the class
     */
    public AnnotationEntry[] getAnnotationEntries() {
        if (annotations == null) {
            annotations = AnnotationEntry.createAnnotationEntries(getAttributes());
        }

        return annotations;
    }

    /**
     * @return File name of class, aka SourceFile attribute value
     */
    public String getFileName() {
        return file_name;
    }

    /**
     * @return sbsolute path to file where this class was read from
     */
    public String getSourceFileName() {
        return source_file_name;
    }

    /** @return returns either HEAP (generated), FILE, or ZIP
     */
    public final byte getSource() {
        return source;
    }

    /**
     * @return Package name.
     */
    public String getPackageName() {
        return package_name;
    }

    public final boolean isSuper() {
        return (this.getAccessFlags() & AccessFlag.SUPER) != 0;
    }

    public final boolean isClass() {
        return (this.getAccessFlags() & AccessFlag.INTERFACE) == 0;
    }

    public final boolean isAnonymous() {
        computeNestedTypeStatus();
        return this.isAnonymous;
    }

    /**
     * @since 6.0
     */
    public final boolean isNested() {
        computeNestedTypeStatus();
        return this.isNested;
    }

    private void computeNestedTypeStatus() {
        if (computedNestedTypeStatus) {
            return;
        }
        for (final Attribute attribute : this.attributes) {
            if (attribute instanceof InnerClasses) {
                final InnerClass[] innerClasses = ((InnerClasses) attribute).getInnerClasses();
                for (final InnerClass innerClasse : innerClasses) {
                    boolean innerClassAttributeRefersToMe = false;
                    String inner_class_name = constant_pool.getConstantString(innerClasse.getInnerClassIndex(),
                            CPConst.CONSTANT_Class);
                    inner_class_name = Utility.compactClassName(inner_class_name);
                    if (inner_class_name.equals(getClassName())) {
                        innerClassAttributeRefersToMe = true;
                    }
                    if (innerClassAttributeRefersToMe) {
                        this.isNested = true;
                        if (innerClasse.getInnerNameIndex() == 0) {
                            this.isAnonymous = true;
                        }
                    }
                }
            }
        }
        this.computedNestedTypeStatus = true;
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
        v.visitJavaClass(this);
    }

    /**
     * @return String representing class contents.
     */
    @Override
    public String toString() {
        String access = Utility.accessToString(this.getAccessFlags(), true);
        access = access.isEmpty() ? "" : (access + " ");
        final StringBuilder buf = new StringBuilder(128);
        buf.append(access).append(Utility.classOrInterface(this.getAccessFlags())).append(" ").append(
                class_name).append(" extends ").append(
                Utility.compactClassName(superclass_name, false)).append('\n');
        final int size = interfaces.length;
        if (size > 0) {
            buf.append("implements\t\t");
            for (int i = 0; i < size; i++) {
                buf.append(interface_names[i]);
                if (i < size - 1) {
                    buf.append(", ");
                }
            }
            buf.append('\n');
        }
        buf.append("filename\t\t").append(file_name).append('\n');
        buf.append("compiled from\t\t").append(source_file_name).append('\n');
        buf.append("compiler version\t").append(major).append(".").append(minor).append('\n');
        buf.append("access flags\t\t").append(this.getAccessFlags()).append('\n');
        buf.append("constant pool\t\t").append(constant_pool.getLength()).append(" entries\n");
        buf.append("ACC_SUPER flag\t\t").append(isSuper()).append("\n");
        if (attributes.length > 0) {
            buf.append("\nAttribute(s):\n");
            for (final Attribute attribute : attributes) {
                buf.append(indent(attribute));
            }
        }
        final AnnotationEntry[] annotations = getAnnotationEntries();
        if (annotations!=null && annotations.length>0) {
            buf.append("\nAnnotation(s):\n");
            for (final AnnotationEntry annotation : annotations) {
                buf.append(indent(annotation));
            }
        }
        if (fields.length > 0) {
            buf.append("\n").append(fields.length).append(" fields:\n");
            for (final Field field : fields) {
                buf.append("\t").append(field).append('\n');
            }
        }
        if (methods.length > 0) {
            buf.append("\n").append(methods.length).append(" methods:\n");
            for (final Method method : methods) {
                buf.append("\t").append(method).append('\n');
            }
        }
        return buf.toString();
    }

    private static String indent( final Object obj ) {
        final StringTokenizer tok = new StringTokenizer(obj.toString(), "\n");
        final StringBuilder buf = new StringBuilder();
        while (tok.hasMoreTokens()) {
            buf.append("\t").append(tok.nextToken()).append("\n");
        }
        return buf.toString();
    }
}

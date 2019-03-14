package lsieun.bcel.classfile.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Field;
import lsieun.bcel.classfile.Node;
import lsieun.bcel.classfile.Visitor;
import lsieun.bcel.classfile.consts.AttrConst;
import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.cp.ConstantUtf8;
import lsieun.bcel.exceptions.ClassFormatException;

public abstract class Attribute implements Node {
    private int name_index;
    private int length;
    private final byte tag;
    private ConstantPool constant_pool;

    private static final Map<String, Object> readers = new HashMap<>();

    protected Attribute(final byte tag, final int name_index, final int length, final ConstantPool constant_pool) {
        this.tag = tag;
        this.name_index = name_index;
        this.length = length;
        this.constant_pool = constant_pool;
    }

    /**
     * @return Name index in constant pool of attribute name.
     */
    public final int getNameIndex()
    {
        return name_index;
    }

    /**
     * @return Length of attribute field in bytes.
     */
    public final int getLength()
    {
        return length;
    }

    /**
     * @return Tag of attribute, i.e., its type. Value may not be altered, thus there is no setTag() method.
     */
    public final byte getTag()
    {
        return tag;
    }

    /**
     * @return Constant pool used by this object.
     * @see ConstantPool
     */
    public final ConstantPool getConstantPool()
    {
        return constant_pool;
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    @Override
    public abstract void accept(Visitor v);

    /**
     * Class method reads one attribute from the input data stream. This method
     * must not be accessible from the outside. It is called by the Field and
     * Method constructor methods.
     *
     * @see Field
     * @see Method
     *
     * @param file Input stream
     * @param constant_pool Array of constants
     * @return Attribute
     * @throws IOException
     * @throws ClassFormatException
     * @since 6.0
     */
    public static Attribute readAttribute(final DataInput file, final ConstantPool constant_pool) throws IOException, ClassFormatException
    {
        byte tag = AttrConst.ATTR_UNKNOWN; // Unknown attribute
        // Get class name from constant pool via `name_index' indirection
        final int name_index = file.readUnsignedShort();
        final ConstantUtf8 c = (ConstantUtf8) constant_pool.getConstant(name_index, CPConst.CONSTANT_Utf8);
        final String name = c.getBytes();

        // Length of data in bytes
        final int length = file.readInt();

        // Compare strings to find known attribute
        for (byte i = 0; i < AttrConst.KNOWN_ATTRIBUTES; i++)
        {
            if (name.equals(AttrConst.getAttributeName(i)))
            {
                tag = i; // found!
                break;
            }
        }

        // Call proper constructor, depending on `tag'
        switch (tag)
        {
            case AttrConst.ATTR_UNKNOWN:
                final Object r = readers.get(name);
                if (r instanceof UnknownAttributeReader)
                {
                    return ((UnknownAttributeReader) r).createAttribute(name_index, length, file, constant_pool);
                }
                return new Unknown(name_index, length, file, constant_pool);
            case AttrConst.ATTR_CONSTANT_VALUE:
                return new ConstantValue(name_index, length, file, constant_pool);
            case AttrConst.ATTR_SOURCE_FILE:
                return new SourceFile(name_index, length, file, constant_pool);
            case AttrConst.ATTR_CODE:
                return new Code(name_index, length, file, constant_pool);
            case AttrConst.ATTR_EXCEPTIONS:
                return new ExceptionTable(name_index, length, file, constant_pool);
            case AttrConst.ATTR_LINE_NUMBER_TABLE:
                return new LineNumberTable(name_index, length, file, constant_pool);
            case AttrConst.ATTR_LOCAL_VARIABLE_TABLE:
                return new LocalVariableTable(name_index, length, file, constant_pool);
            case AttrConst.ATTR_INNER_CLASSES:
                return new InnerClasses(name_index, length, file, constant_pool);
            case AttrConst.ATTR_SYNTHETIC:
                return new Synthetic(name_index, length, file, constant_pool);
            case AttrConst.ATTR_DEPRECATED:
                return new Deprecated(name_index, length, file, constant_pool);
            case AttrConst.ATTR_PMG:
                return new PMGClass(name_index, length, file, constant_pool);
            case AttrConst.ATTR_SIGNATURE:
                return new Signature(name_index, length, file, constant_pool);
            case AttrConst.ATTR_STACK_MAP:
                // old style stack map: unneeded for JDK5 and below;
                // illegal(?) for JDK6 and above.  So just delete with a warning.
                System.err.println("Warning: Obsolete StackMap attribute ignored.");
                return new Unknown(name_index, length, file, constant_pool);
            case AttrConst.ATTR_RUNTIME_VISIBLE_ANNOTATIONS:
                return new RuntimeVisibleAnnotations(name_index, length, file, constant_pool);
            case AttrConst.ATTR_RUNTIME_INVISIBLE_ANNOTATIONS:
                return new RuntimeInvisibleAnnotations(name_index, length, file, constant_pool);
            case AttrConst.ATTR_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
                return new RuntimeVisibleParameterAnnotations(name_index, length, file, constant_pool);
            case AttrConst.ATTR_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
                return new RuntimeInvisibleParameterAnnotations(name_index, length, file, constant_pool);
            case AttrConst.ATTR_ANNOTATION_DEFAULT:
                return new AnnotationDefault(name_index, length, file, constant_pool);
            case AttrConst.ATTR_LOCAL_VARIABLE_TYPE_TABLE:
                return new LocalVariableTypeTable(name_index, length, file, constant_pool);
            case AttrConst.ATTR_ENCLOSING_METHOD:
                return new EnclosingMethod(name_index, length, file, constant_pool);
            case AttrConst.ATTR_STACK_MAP_TABLE:
                // read new style stack map: StackMapTable.  The rest of the code
                // calls this a StackMap for historical reasons.
                return new StackMap(name_index, length, file, constant_pool);
            case AttrConst.ATTR_BOOTSTRAP_METHODS:
                return new BootstrapMethods(name_index, length, file, constant_pool);
            case AttrConst.ATTR_METHOD_PARAMETERS:
                return new MethodParameters(name_index, length, file, constant_pool);
            default:
                // Never reached
                throw new IllegalStateException("Unrecognized attribute type tag parsed: " + tag);
        }
    }
}

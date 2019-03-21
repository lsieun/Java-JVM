package lsieun.bcel;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lsieun.bcel.classfile.AccessFlag;
import lsieun.bcel.classfile.ConstantPool;
import lsieun.bcel.classfile.Field;
import lsieun.bcel.classfile.JavaClass;
import lsieun.bcel.classfile.Method;
import lsieun.bcel.classfile.attributes.Attribute;
import lsieun.bcel.classfile.consts.JVMConst;
import lsieun.bcel.exceptions.ClassFormatException;
import lsieun.bcel.utils.IOUtils;

public final class ClassParser {

    private static final int BUF_SIZE = 8192;


    private DataInputStream dataInputStream;
    private final boolean fileOwned;
    private final String file_name;

    private int major; // Compiler version
    private int minor; // Compiler version
    private int access_flags; // Access rights of parsed class
    private int class_name_index;
    private int superclass_name_index;
    private int[] interfaces; // Names of implemented interfaces
    private ConstantPool constant_pool; // collection of constants
    private Field[] fields; // class fields, i.e., its variables
    private Method[] methods; // methods defined in the class
    private Attribute[] attributes; // attributes defined in the class

    /**
     * Parse class from the given stream.
     *
     * @param inputStream Input stream
     * @param file_name File name
     */
    public ClassParser(final InputStream inputStream, final String file_name) {
        this.file_name = file_name;
        fileOwned = false;

        if (inputStream instanceof DataInputStream) {
            this.dataInputStream = (DataInputStream) inputStream;
        } else {
            this.dataInputStream = new DataInputStream(new BufferedInputStream(inputStream, BUF_SIZE));
        }
    }


    /** Parse class from given .class file.
     *
     * @param file_name file name
     */
    public ClassParser(final String file_name) {
        this.file_name = file_name;
        fileOwned = true;
    }

    /**
     * Parse the given Java class file and return an object that represents
     * the contained data, i.e., constants, methods, fields and commands.
     * A <em>ClassFormatException</em> is raised, if the file is not a valid
     * .class file. (This does not include verification of the byte code as it
     * is performed by the java interpreter).
     *
     * @return Class object representing the parsed class file
     * @throws  IOException
     * @throws  ClassFormatException
     */
    public JavaClass parse() throws IOException, ClassFormatException {
        try {
            if (fileOwned) {
                dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(
                        file_name), BUF_SIZE));
            }
            /****************** Read headers ********************************/
            // Check magic tag of class file
            readID();
            // Get compiler version
            readVersion();
            /****************** Read constant pool and related **************/
            // Read constant pool entries
            readConstantPool();
            // Get class information
            readClassInfo();
            // Get interface information, i.e., implemented interfaces
            readInterfaces();
            /****************** Read class fields and methods ***************/
            // Read class fields, i.e., the variables of the class
            readFields();
            // Read class methods, i.e., the functions in the class
            readMethods();
            // Read class attributes
            readAttributes();
        }
        finally {
            if (fileOwned) {
                IOUtils.closeQuitely(dataInputStream);
            }
        }

        // Return the information we have gathered in a new object
        return new JavaClass(class_name_index, superclass_name_index, file_name, major, minor,
                access_flags, constant_pool, interfaces, fields, methods, attributes, JavaClass.FILE);
    }

    // region Private utility methods
    /**
     * Check whether the header of the file is ok.
     * Of course, this has to be the first action on successive file reads.
     * @throws IOException
     * @throws  ClassFormatException
     */
    private void readID() throws IOException, ClassFormatException {
        if (dataInputStream.readInt() != JVMConst.JVM_CLASSFILE_MAGIC) {
            throw new ClassFormatException(file_name + " is not a Java .class file");
        }
    }

    /**
     * Read major and minor version of compiler which created the file.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private void readVersion() throws IOException, ClassFormatException {
        minor = dataInputStream.readUnsignedShort();
        major = dataInputStream.readUnsignedShort();
    }

    /**
     * Read constant pool entries.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private void readConstantPool() throws IOException, ClassFormatException {
        constant_pool = new ConstantPool(dataInputStream);
    }

    /**
     * Read information about the class and its super class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private void readClassInfo() throws IOException, ClassFormatException {
        access_flags = dataInputStream.readUnsignedShort();
        /* Interfaces are implicitely abstract, the flag should be set
         * according to the JVM specification.
         */
        if ((access_flags & AccessFlag.INTERFACE) != 0) {
            access_flags |= AccessFlag.ABSTRACT;
        }
        if (((access_flags & AccessFlag.ABSTRACT) != 0)
                && ((access_flags & AccessFlag.FINAL) != 0)) {
            throw new ClassFormatException("Class " + file_name + " can't be both final and abstract");
        }
        class_name_index = dataInputStream.readUnsignedShort();
        superclass_name_index = dataInputStream.readUnsignedShort();
    }

    /**
     * Read information about the interfaces implemented by this class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private void readInterfaces() throws IOException, ClassFormatException {
        final int interfaces_count = dataInputStream.readUnsignedShort();
        interfaces = new int[interfaces_count];
        for (int i = 0; i < interfaces_count; i++) {
            interfaces[i] = dataInputStream.readUnsignedShort();
        }
    }

    /**
     * Read information about the fields of the class, i.e., its variables.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private void readFields() throws IOException, ClassFormatException {
        final int fields_count = dataInputStream.readUnsignedShort();
        fields = new Field[fields_count];
        for (int i = 0; i < fields_count; i++) {
            fields[i] = new Field(dataInputStream, constant_pool);
        }
    }

    /**
     * Read information about the methods of the class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private void readMethods() throws IOException, ClassFormatException {
        final int methods_count = dataInputStream.readUnsignedShort();
        methods = new Method[methods_count];
        for (int i = 0; i < methods_count; i++) {
            methods[i] = new Method(dataInputStream, constant_pool);
        }
    }

    /**
     * Read information about the attributes of the class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private void readAttributes() throws IOException, ClassFormatException {
        final int attributes_count = dataInputStream.readUnsignedShort();
        attributes = new Attribute[attributes_count];
        for (int i = 0; i < attributes_count; i++) {
            attributes[i] = Attribute.readAttribute(dataInputStream, constant_pool);
        }
    }
    // endregion
}

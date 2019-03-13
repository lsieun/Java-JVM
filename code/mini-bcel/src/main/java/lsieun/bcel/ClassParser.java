package lsieun.bcel;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import lsieun.bcel.classfile.JavaClass;
import lsieun.bcel.classfile.JavaConst;
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
//    private Field[] fields; // class fields, i.e., its variables
//    private Method[] methods; // methods defined in the class
//    private Attribute[] attributes; // attributes defined in the class

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
            /****************** Read headers ********************************/
            // Check magic tag of class file
            readID();
            // Get compiler version
            readVersion();
            /****************** Read constant pool and related **************/
            // Read constant pool entries
            readConstantPool();
        }
        finally {
            if (fileOwned) {
                IOUtils.closeQuitely(dataInputStream);
            }
        }

        // Return the information we have gathered in a new object
        return new JavaClass(class_name_index, superclass_name_index, file_name, major, minor,
                access_flags, constant_pool, interfaces, fields, methods, attributes, is_zip
                ? JavaClass.ZIP
                : JavaClass.FILE);
    }

    // region Private utility methods
    /**
     * Check whether the header of the file is ok.
     * Of course, this has to be the first action on successive file reads.
     * @throws IOException
     * @throws  ClassFormatException
     */
    private void readID() throws IOException, ClassFormatException {
        if (dataInputStream.readInt() != JavaConst.JVM_CLASSFILE_MAGIC) {
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
    // endregion
}

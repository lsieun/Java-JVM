package lsieun.bcel.classfile;

import java.io.IOException;

import com.sun.org.apache.bcel.internal.util.ByteSequence;

import lsieun.bcel.classfile.consts.CPConst;
import lsieun.bcel.classfile.consts.OpcodeConst;
import lsieun.bcel.classfile.cp.Constant;
import lsieun.bcel.exceptions.ClassFormatException;

public class Utility {

    private static int unwrap( final ThreadLocal<Integer> tl ) {
        return tl.get().intValue();
    }


    private static void wrap( final ThreadLocal<Integer> tl, final int value ) {
        tl.set(Integer.valueOf(value));
    }

    private static ThreadLocal<Integer> consumed_chars = new ThreadLocal<Integer>() {

        @Override
        protected Integer initialValue() {
            return Integer.valueOf(0);
        }
    };/* How many chars have been consumed
     * during parsing in signatureToString().
     * Read by methodSignatureToString().
     * Set by side effect,but only internally.
     */
    private static boolean wide = false; /* The `WIDE' instruction is used in the
     * byte code to allow 16-bit wide indices
     * for local variables. This opcode
     * precedes an `ILOAD', e.g.. The opcode
     * immediately following takes an extra
     * byte which is combined with the
     * following byte to form a
     * 16-bit value.
     */

    /** Convert bytes into hexadecimal string
     *
     * @param bytes an array of bytes to convert to hexadecimal
     *
     * @return bytes as hexadecimal string, e.g. 00 fa 12 ...
     */
    public static String toHexString( final byte[] bytes ) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            final short b = byteToShort(bytes[i]);
            final String hex = Integer.toHexString(b);
            if (b < 0x10) {
                buf.append('0');
            }
            buf.append(hex);
            if (i < bytes.length - 1) {
                buf.append(' ');
            }
        }
        return buf.toString();
    }

    /**
     * Convert (signed) byte to (unsigned) short value, i.e., all negative
     * values become positive.
     */
    private static short byteToShort( final byte b ) {
        return (b < 0) ? (short) (256 + b) : (short) b;
    }

    /**
     * Convert bit field of flags into string such as `static final'.
     *
     * Special case: Classes compiled with new compilers and with the
     * `ACC_SUPER' flag would be said to be "synchronized". This is
     * because SUN used the same value for the flags `ACC_SUPER' and
     * `ACC_SYNCHRONIZED'.
     *
     * @param  access_flags Access flags
     * @param  for_class access flags are for class qualifiers ?
     * @return String representation of flags
     */
    public static String accessToString(final int access_flags, final boolean for_class) {
        final StringBuilder buf = new StringBuilder();
        int p = 0;
        for (int i = 0; p < AccessFlag.MAX_ACC_FLAG; i++) { // Loop through known flags
            p = pow2(i);
            if ((access_flags & p) != 0) {
                /* Special case: Classes compiled with new compilers and with the
                 * `ACC_SUPER' flag would be said to be "synchronized". This is
                 * because SUN used the same value for the flags `ACC_SUPER' and
                 * `ACC_SYNCHRONIZED'.
                 */
                if (for_class && ((p == AccessFlag.SUPER) || (p == AccessFlag.INTERFACE))) {
                    continue;
                }
                buf.append(AccessFlag.getAccessName(i)).append(" ");
            }
        }
        return buf.toString().trim();
    }

    /**
     *
     * @param access_flags Access flags
     * @param type 1-class,2-field,3-method
     * @return String representation of flags
     */
    public static String accessToString(final int access_flags, final int type) {
        StringBuilder buf = new StringBuilder();
        int p = 0;
        p = pow2(0);
        if ((access_flags & p) != 0) {
            buf.append("public").append(" ");
        }

        p = pow2(1);
        if ((access_flags & p) != 0) {
            buf.append("private").append(" ");
        }

        p = pow2(2);
        if ((access_flags & p) != 0) {
            buf.append("protected").append(" ");
        }

        p = pow2(3);
        if ((access_flags & p) != 0) {
            buf.append("static").append(" ");
        }

        p = pow2(4);
        if ((access_flags & p) != 0) {
            buf.append("final").append(" ");
        }

        p = pow2(5);
        if ((access_flags & p) != 0) {
            if(type == 1) {
                buf.append("super").append(" ");
            }
            else if(type == 3) {
                buf.append("synchronized").append(" ");
            }
        }

        p = pow2(6);
        if ((access_flags & p) != 0) {
            if(type == 2) {
                buf.append("volatile").append(" ");
            }
            else if(type == 3) {
                buf.append("bridge").append(" ");
            }
        }

        p = pow2(7);
        if ((access_flags & p) != 0) {
            if(type == 2) {
                buf.append("transient").append(" ");
            }
            else if(type == 3) {
                buf.append("varargs").append(" ");
            }
        }

        p = pow2(8);
        if ((access_flags & p) != 0) {
            buf.append("native").append(" ");
        }

        p = pow2(9);
        if ((access_flags & p) != 0) {
            buf.append("interface").append(" ");
        }

        p = pow2(10);
        if ((access_flags & p) != 0) {
            buf.append("abstract").append(" ");
        }

        p = pow2(11);
        if ((access_flags & p) != 0) {
            buf.append("strictfp").append(" ");
        }

        p = pow2(12);
        if ((access_flags & p) != 0) {
            buf.append("synthetic").append(" ");
        }

        p = pow2(13);
        if ((access_flags & p) != 0) {
            buf.append("annotation").append(" ");
        }

        p = pow2(14);
        if ((access_flags & p) != 0) {
            buf.append("enum").append(" ");
        }

        p = pow2(15);
        if ((access_flags & p) != 0) {
            buf.append(" ").append(" ");
        }
        return buf.toString().trim();
    }

    // Guess what this does
    private static int pow2(final int n) {
        return 1 << n;
    }

    /**
     * Shorten long class name <em>str</em>, i.e., chop off the <em>prefix</em>,
     * if the
     * class name starts with this string and the flag <em>chopit</em> is true.
     * Slashes <em>/</em> are converted to dots <em>.</em>.
     *
     * @param str The long class name
     * @param prefix The prefix the get rid off
     * @param chopit Flag that determines whether chopping is executed or not
     * @return Compacted class name
     */
    public static String compactClassName(String str, final String prefix, final boolean chopit) {
        final int len = prefix.length();
        str = str.replace('/', '.'); // Is `/' on all systems, even DOS
        if (chopit) {
            // If string starts with `prefix' and contains no further dots
            if (str.startsWith(prefix) && (str.substring(len).indexOf('.') == -1)) {
                str = str.substring(len);
            }
        }
        return str;
    }

    /**
     * Shorten long class names, <em>java/lang/String</em> becomes
     * <em>java.lang.String</em>,
     * e.g.. If <em>chopit</em> is <em>true</em> the prefix <em>java.lang</em>
     * is also removed.
     *
     * @param str The long class name
     * @param chopit Flag that determines whether chopping is executed or not
     * @return Compacted class name
     */
    public static String compactClassName(final String str, final boolean chopit) {
        return compactClassName(str, "java.lang.", chopit);
    }

    /**
     * Shorten long class names, <em>java/lang/String</em> becomes
     * <em>String</em>.
     *
     * @param str The long class name
     * @return Compacted class name
     */
    public static String compactClassName( final String str ) {
        return compactClassName(str, true);
    }

    /**
     * Escape all occurences of newline chars '\n', quotes \", etc.
     */
    public static String convertString( final String label ) {
        final char[] ch = label.toCharArray();
        final StringBuilder buf = new StringBuilder();
        for (final char element : ch) {
            switch (element) {
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\"':
                    buf.append("\\\"");
                    break;
                case '\'':
                    buf.append("\\'");
                    break;
                case '\\':
                    buf.append("\\\\");
                    break;
                default:
                    buf.append(element);
                    break;
            }
        }
        return buf.toString();
    }

    /**
     * The field signature represents the value of an argument to a function or
     * the value of a variable. It is a series of bytes generated by the
     * following grammar:
     *
     * <PRE>
     * &lt;field_signature&gt; ::= &lt;field_type&gt;
     * &lt;field_type&gt;      ::= &lt;base_type&gt;|&lt;object_type&gt;|&lt;array_type&gt;
     * &lt;base_type&gt;       ::= B|C|D|F|I|J|S|Z
     * &lt;object_type&gt;     ::= L&lt;fullclassname&gt;;
     * &lt;array_type&gt;      ::= [&lt;field_type&gt;
     *
     * The meaning of the base types is as follows:
     * B byte signed byte
     * C char character
     * D double double precision IEEE float
     * F float single precision IEEE float
     * I int integer
     * J long long integer
     * L&lt;fullclassname&gt;; ... an object of the given class
     * S short signed short
     * Z boolean true or false
     * [&lt;field sig&gt; ... array
     * </PRE>
     *
     * This method converts this string into a Java type declaration such as
     * `String[]' and throws a `ClassFormatException' when the parsed type is
     * invalid.
     *
     * @param  signature  Class signature
     * @param chopit Flag that determines whether chopping is executed or not
     * @return Java type declaration
     * @throws ClassFormatException
     */
    // TODO: signatureToString这个方法的细节，我还不了解，感觉应该很有用
    public static String signatureToString( final String signature, final boolean chopit ) {
        //corrected concurrent private static field acess
        wrap(consumed_chars, 1); // This is the default, read just one char like `B'
        try {
            switch (signature.charAt(0)) {
                case 'B':
                    return "byte";
                case 'C':
                    return "char";
                case 'D':
                    return "double";
                case 'F':
                    return "float";
                case 'I':
                    return "int";
                case 'J':
                    return "long";
                case 'T': { // TypeVariableSignature
                    final int index = signature.indexOf(';'); // Look for closing `;'
                    if (index < 0) {
                        throw new ClassFormatException("Invalid signature: " + signature);
                    }
                    //corrected concurrent private static field acess
                    wrap(consumed_chars, index + 1); // "Tblabla;" `T' and `;' are removed
                    return compactClassName(signature.substring(1, index), chopit);
                }
                case 'L': { // Full class name
                    // should this be a while loop? can there be more than
                    // one generic clause?  (markro)
                    int fromIndex = signature.indexOf('<'); // generic type?
                    if (fromIndex < 0) {
                        fromIndex = 0;
                    } else {
                        fromIndex = signature.indexOf('>', fromIndex);
                        if (fromIndex < 0) {
                            throw new ClassFormatException("Invalid signature: " + signature);
                        }
                    }
                    final int index = signature.indexOf(';', fromIndex); // Look for closing `;'
                    if (index < 0) {
                        throw new ClassFormatException("Invalid signature: " + signature);
                    }

                    // check to see if there are any TypeArguments
                    final int bracketIndex = signature.substring(0, index).indexOf('<');
                    if (bracketIndex < 0) {
                        // just a class identifier
                        wrap(consumed_chars, index + 1); // "Lblabla;" `L' and `;' are removed
                        return compactClassName(signature.substring(1, index), chopit);
                    }
                    // but make sure we are not looking past the end of the current item
                    fromIndex = signature.indexOf(';');
                    if (fromIndex < 0) {
                        throw new ClassFormatException("Invalid signature: " + signature);
                    }
                    if (fromIndex < bracketIndex) {
                        // just a class identifier
                        wrap(consumed_chars, fromIndex + 1); // "Lblabla;" `L' and `;' are removed
                        return compactClassName(signature.substring(1, fromIndex), chopit);
                    }

                    // we have TypeArguments; build up partial result
                    // as we recurse for each TypeArgument
                    final StringBuilder type = new StringBuilder(compactClassName(signature.substring(1, bracketIndex), chopit)).append("<");
                    int consumed_chars = bracketIndex + 1; // Shadows global var

                    // check for wildcards
                    if (signature.charAt(consumed_chars) == '+') {
                        type.append("? extends ");
                        consumed_chars++;
                    } else if (signature.charAt(consumed_chars) == '-') {
                        type.append("? super ");
                        consumed_chars++;
                    }

                    // get the first TypeArgument
                    if (signature.charAt(consumed_chars) == '*') {
                        type.append("?");
                        consumed_chars++;
                    } else {
                        type.append(signatureToString(signature.substring(consumed_chars), chopit));
                        // update our consumed count by the number of characters the for type argument
                        consumed_chars = unwrap(Utility.consumed_chars) + consumed_chars;
                        wrap(Utility.consumed_chars, consumed_chars);
                    }

                    // are there more TypeArguments?
                    while (signature.charAt(consumed_chars) != '>') {
                        type.append(", ");
                        // check for wildcards
                        if (signature.charAt(consumed_chars) == '+') {
                            type.append("? extends ");
                            consumed_chars++;
                        } else if (signature.charAt(consumed_chars) == '-') {
                            type.append("? super ");
                            consumed_chars++;
                        }
                        if (signature.charAt(consumed_chars) == '*') {
                            type.append("?");
                            consumed_chars++;
                        } else {
                            type.append(signatureToString(signature.substring(consumed_chars), chopit));
                            // update our consumed count by the number of characters the for type argument
                            consumed_chars = unwrap(Utility.consumed_chars) + consumed_chars;
                            wrap(Utility.consumed_chars, consumed_chars);
                        }
                    }

                    // process the closing ">"
                    consumed_chars++;
                    type.append(">");

                    if (signature.charAt(consumed_chars) == '.') {
                        // we have a ClassTypeSignatureSuffix
                        type.append(".");
                        // convert SimpleClassTypeSignature to fake ClassTypeSignature
                        // and then recurse to parse it
                        type.append(signatureToString("L" + signature.substring(consumed_chars+1), chopit));
                        // update our consumed count by the number of characters the for type argument
                        // note that this count includes the "L" we added, but that is ok
                        // as it accounts for the "." we didn't consume
                        consumed_chars = unwrap(Utility.consumed_chars) + consumed_chars;
                        wrap(Utility.consumed_chars, consumed_chars);
                        return type.toString();
                    }
                    if (signature.charAt(consumed_chars) != ';') {
                        throw new ClassFormatException("Invalid signature: " + signature);
                    }
                    wrap(Utility.consumed_chars, consumed_chars + 1); // remove final ";"
                    return type.toString();
                }
                case 'S':
                    return "short";
                case 'Z':
                    return "boolean";
                case '[': { // Array declaration
                    int n;
                    StringBuilder brackets;
                    String type;
                    int consumed_chars; // Shadows global var
                    brackets = new StringBuilder(); // Accumulate []'s
                    // Count opening brackets and look for optional size argument
                    for (n = 0; signature.charAt(n) == '['; n++) {
                        brackets.append("[]");
                    }
                    consumed_chars = n; // Remember value
                    // The rest of the string denotes a `<field_type>'
                    type = signatureToString(signature.substring(n), chopit);
                    //corrected concurrent private static field acess
                    //Utility.consumed_chars += consumed_chars; is replaced by:
                    final int _temp = unwrap(Utility.consumed_chars) + consumed_chars;
                    wrap(Utility.consumed_chars, _temp);
                    return type + brackets.toString();
                }
                case 'V':
                    return "void";
                default:
                    throw new ClassFormatException("Invalid signature: `" + signature + "'");
            }
        } catch (final StringIndexOutOfBoundsException e) { // Should never occur
            throw new ClassFormatException("Invalid signature: " + signature, e);
        }
    }

    /**
     * Disassemble a byte array of JVM byte codes starting from code line
     * `index' and return the disassembled string representation. Decode only
     * `num' opcodes (including their operands), use -1 if you want to
     * decompile everything.
     *
     * @param  code byte code array
     * @param  constant_pool Array of constants
     * @param  index offset in `code' array
     * <EM>(number of opcodes, not bytes!)</EM>
     * @param  length number of opcodes to decompile, -1 for all
     * @param  verbose be verbose, e.g. print constant pool index
     * @return String representation of byte codes
     */
    public static String codeToString( final byte[] code, final ConstantPool constant_pool, final int index,
                                       final int length, final boolean verbose ) {
        final StringBuilder buf = new StringBuilder(code.length * 20); // Should be sufficient // CHECKSTYLE IGNORE MagicNumber
        try (ByteSequence stream = new ByteSequence(code)) {
            for (int i = 0; i < index; i++) {
                codeToString(stream, constant_pool, verbose);
            }
            for (int i = 0; stream.available() > 0; i++) {
                if ((length < 0) || (i < length)) {
                    final String indices = fillup(stream.getIndex() + ":", 6, true, ' ');
                    buf.append(indices).append(codeToString(stream, constant_pool, verbose)).append('\n');
                }
            }
        } catch (final IOException e) {
            throw new ClassFormatException("Byte code error: " + buf.toString(), e);
        }
        return buf.toString();
    }


    /**
     * Disassemble a stream of byte codes and return the
     * string representation.
     *
     * @param  bytes stream of bytes
     * @param  constant_pool Array of constants
     * @param  verbose be verbose, e.g. print constant pool index
     * @return String representation of byte code
     *
     * @throws IOException if a failure from reading from the bytes argument occurs
     */
    public static String codeToString( final ByteSequence bytes, final ConstantPool constant_pool,
                                       final boolean verbose ) throws IOException {
        final short opcode = (short) bytes.readUnsignedByte();
        int default_offset = 0;
        int low;
        int high;
        int npairs;
        int index;
        int vindex;
        int constant;
        int[] match;
        int[] jump_table;
        int no_pad_bytes = 0;
        int offset;
        final StringBuilder buf = new StringBuilder(OpcodeConst.getOpcodeName(opcode));
        /* Special case: Skip (0-3) padding bytes, i.e., the
         * following bytes are 4-byte-aligned
         */
        if ((opcode == OpcodeConst.TABLESWITCH) || (opcode == OpcodeConst.LOOKUPSWITCH)) {
            final int remainder = bytes.getIndex() % 4;
            no_pad_bytes = (remainder == 0) ? 0 : 4 - remainder;
            for (int i = 0; i < no_pad_bytes; i++) {
                byte b;
                if ((b = bytes.readByte()) != 0) {
                    System.err.println("Warning: Padding byte != 0 in "
                            + OpcodeConst.getOpcodeName(opcode) + ":" + b);
                }
            }
            // Both cases have a field default_offset in common
            default_offset = bytes.readInt();
        }
        switch (opcode) {
            /* Table switch has variable length arguments.
             */
            case OpcodeConst.TABLESWITCH:
                low = bytes.readInt();
                high = bytes.readInt();
                offset = bytes.getIndex() - 12 - no_pad_bytes - 1;
                default_offset += offset;
                buf.append("\tdefault = ").append(default_offset).append(", low = ").append(low)
                        .append(", high = ").append(high).append("(");
                jump_table = new int[high - low + 1];
                for (int i = 0; i < jump_table.length; i++) {
                    jump_table[i] = offset + bytes.readInt();
                    buf.append(jump_table[i]);
                    if (i < jump_table.length - 1) {
                        buf.append(", ");
                    }
                }
                buf.append(")");
                break;
            /* Lookup switch has variable length arguments.
             */
            case OpcodeConst.LOOKUPSWITCH: {
                npairs = bytes.readInt();
                offset = bytes.getIndex() - 8 - no_pad_bytes - 1;
                match = new int[npairs];
                jump_table = new int[npairs];
                default_offset += offset;
                buf.append("\tdefault = ").append(default_offset).append(", npairs = ").append(
                        npairs).append(" (");
                for (int i = 0; i < npairs; i++) {
                    match[i] = bytes.readInt();
                    jump_table[i] = offset + bytes.readInt();
                    buf.append("(").append(match[i]).append(", ").append(jump_table[i]).append(")");
                    if (i < npairs - 1) {
                        buf.append(", ");
                    }
                }
                buf.append(")");
            }
            break;
            /* Two address bytes + offset from start of byte stream form the
             * jump target
             */
            case OpcodeConst.GOTO:
            case OpcodeConst.IFEQ:
            case OpcodeConst.IFGE:
            case OpcodeConst.IFGT:
            case OpcodeConst.IFLE:
            case OpcodeConst.IFLT:
            case OpcodeConst.JSR:
            case OpcodeConst.IFNE:
            case OpcodeConst.IFNONNULL:
            case OpcodeConst.IFNULL:
            case OpcodeConst.IF_ACMPEQ:
            case OpcodeConst.IF_ACMPNE:
            case OpcodeConst.IF_ICMPEQ:
            case OpcodeConst.IF_ICMPGE:
            case OpcodeConst.IF_ICMPGT:
            case OpcodeConst.IF_ICMPLE:
            case OpcodeConst.IF_ICMPLT:
            case OpcodeConst.IF_ICMPNE:
                buf.append("\t\t#").append((bytes.getIndex() - 1) + bytes.readShort());
                break;
            /* 32-bit wide jumps
             */
            case OpcodeConst.GOTO_W:
            case OpcodeConst.JSR_W:
                buf.append("\t\t#").append((bytes.getIndex() - 1) + bytes.readInt());
                break;
            /* Index byte references local variable (register)
             */
            case OpcodeConst.ALOAD:
            case OpcodeConst.ASTORE:
            case OpcodeConst.DLOAD:
            case OpcodeConst.DSTORE:
            case OpcodeConst.FLOAD:
            case OpcodeConst.FSTORE:
            case OpcodeConst.ILOAD:
            case OpcodeConst.ISTORE:
            case OpcodeConst.LLOAD:
            case OpcodeConst.LSTORE:
            case OpcodeConst.RET:
                if (wide) {
                    vindex = bytes.readUnsignedShort();
                    wide = false; // Clear flag
                } else {
                    vindex = bytes.readUnsignedByte();
                }
                buf.append("\t\t%").append(vindex);
                break;
            /*
             * Remember wide byte which is used to form a 16-bit address in the
             * following instruction. Relies on that the method is called again with
             * the following opcode.
             */
            case OpcodeConst.WIDE:
                wide = true;
                buf.append("\t(wide)");
                break;
            /* Array of basic type.
             */
            case OpcodeConst.NEWARRAY:
                buf.append("\t\t<").append(OpcodeConst.getTypeName(bytes.readByte())).append(">");
                break;
            /* Access object/class fields.
             */
            case OpcodeConst.GETFIELD:
            case OpcodeConst.GETSTATIC:
            case OpcodeConst.PUTFIELD:
            case OpcodeConst.PUTSTATIC:
                index = bytes.readUnsignedShort();
                buf.append("\t\t").append(
                        constant_pool.constantToString(index, CPConst.CONSTANT_Fieldref)).append(
                        verbose ? " (" + index + ")" : "");
                break;
            /* Operands are references to classes in constant pool
             */
            case OpcodeConst.NEW:
            case OpcodeConst.CHECKCAST:
                buf.append("\t");
                //$FALL-THROUGH$
            case OpcodeConst.INSTANCEOF:
                index = bytes.readUnsignedShort();
                buf.append("\t<").append(
                        constant_pool.constantToString(index, CPConst.CONSTANT_Class))
                        .append(">").append(verbose ? " (" + index + ")" : "");
                break;
            /* Operands are references to methods in constant pool
             */
            case OpcodeConst.INVOKESPECIAL:
            case OpcodeConst.INVOKESTATIC:
                index = bytes.readUnsignedShort();
                final Constant c = constant_pool.getConstant(index);
                // With Java8 operand may be either a CONSTANT_Methodref
                // or a CONSTANT_InterfaceMethodref.   (markro)
                buf.append("\t").append(
                        constant_pool.constantToString(index, c.getTag()))
                        .append(verbose ? " (" + index + ")" : "");
                break;
            case OpcodeConst.INVOKEVIRTUAL:
                index = bytes.readUnsignedShort();
                buf.append("\t").append(
                        constant_pool.constantToString(index, CPConst.CONSTANT_Methodref))
                        .append(verbose ? " (" + index + ")" : "");
                break;
            case OpcodeConst.INVOKEINTERFACE:
                index = bytes.readUnsignedShort();
                final int nargs = bytes.readUnsignedByte(); // historical, redundant
                buf.append("\t").append(
                        constant_pool
                                .constantToString(index, CPConst.CONSTANT_InterfaceMethodref))
                        .append(verbose ? " (" + index + ")\t" : "").append(nargs).append("\t")
                        .append(bytes.readUnsignedByte()); // Last byte is a reserved space
                break;
            case OpcodeConst.INVOKEDYNAMIC:
                index = bytes.readUnsignedShort();
                buf.append("\t").append(
                        constant_pool
                                .constantToString(index, CPConst.CONSTANT_InvokeDynamic))
                        .append(verbose ? " (" + index + ")\t" : "")
                        .append(bytes.readUnsignedByte())  // Thrid byte is a reserved space
                        .append(bytes.readUnsignedByte()); // Last byte is a reserved space
                break;
            /* Operands are references to items in constant pool
             */
            case OpcodeConst.LDC_W:
            case OpcodeConst.LDC2_W:
                index = bytes.readUnsignedShort();
                buf.append("\t\t").append(
                        constant_pool.constantToString(index, constant_pool.getConstant(index)
                                .getTag())).append(verbose ? " (" + index + ")" : "");
                break;
            case OpcodeConst.LDC:
                index = bytes.readUnsignedByte();
                buf.append("\t\t").append(
                        constant_pool.constantToString(index, constant_pool.getConstant(index)
                                .getTag())).append(verbose ? " (" + index + ")" : "");
                break;
            /* Array of references.
             */
            case OpcodeConst.ANEWARRAY:
                index = bytes.readUnsignedShort();
                buf.append("\t\t<").append(
                        compactClassName(constant_pool.getConstantString(index,
                                CPConst.CONSTANT_Class), false)).append(">").append(
                        verbose ? " (" + index + ")" : "");
                break;
            /* Multidimensional array of references.
             */
            case OpcodeConst.MULTIANEWARRAY: {
                index = bytes.readUnsignedShort();
                final int dimensions = bytes.readUnsignedByte();
                buf.append("\t<").append(
                        compactClassName(constant_pool.getConstantString(index,
                                CPConst.CONSTANT_Class), false)).append(">\t").append(dimensions)
                        .append(verbose ? " (" + index + ")" : "");
            }
            break;
            /* Increment local variable.
             */
            case OpcodeConst.IINC:
                if (wide) {
                    vindex = bytes.readUnsignedShort();
                    constant = bytes.readShort();
                    wide = false;
                } else {
                    vindex = bytes.readUnsignedByte();
                    constant = bytes.readByte();
                }
                buf.append("\t\t%").append(vindex).append("\t").append(constant);
                break;
            default:
                if (OpcodeConst.getNoOfOperands(opcode) > 0) {
                    for (int i = 0; i < OpcodeConst.getOperandTypeCount(opcode); i++) {
                        buf.append("\t\t");
                        switch (OpcodeConst.getOperandType(opcode, i)) {
                            case OpcodeConst.T_BYTE:
                                buf.append(bytes.readByte());
                                break;
                            case OpcodeConst.T_SHORT:
                                buf.append(bytes.readShort());
                                break;
                            case OpcodeConst.T_INT:
                                buf.append(bytes.readInt());
                                break;
                            default: // Never reached
                                throw new IllegalStateException("Unreachable default case reached!");
                        }
                    }
                }
        }
        return buf.toString();
    }

    /**
     * Fillup char with up to length characters with char `fill' and justify it left or right.
     *
     * @param str string to format
     * @param length length of desired string
     * @param left_justify format left or right
     * @param fill fill character
     * @return formatted string
     */
    public static String fillup( final String str, final int length, final boolean left_justify, final char fill ) {
        final int len = length - str.length();
        final char[] buf = new char[(len < 0) ? 0 : len];
        for (int j = 0; j < buf.length; j++) {
            buf[j] = fill;
        }
        if (left_justify) {
            return str + new String(buf);
        }
        return new String(buf) + str;
    }


}

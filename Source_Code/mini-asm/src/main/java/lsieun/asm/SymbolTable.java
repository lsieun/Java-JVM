package lsieun.asm;

/**
 * The constant pool entries.
 *
 */
final class SymbolTable {
    /**
     * The ClassWriter to which this SymbolTable belongs.
     */
    final ClassWriter classWriter;

    /**
     * The ClassReader from which this SymbolTable was constructed, or {@literal null} if it was
     * constructed from scratch.
     */
    private final ClassReader sourceClassReader;

    /** The major version number of the class to which this symbol table belongs. */
    private int majorVersion;

    /** The internal name of the class to which this symbol table belongs. */
    private String className;

    /**
     * The total number of {@link Entry} instances in {@link #entries}. This includes entries that are
     * accessible (recursively) via {@link Entry#next}.
     */
    private int entryCount;

    /**
     * A hash set of all the entries in this SymbolTable (this includes the constant pool entries, the
     * bootstrap method entries and the type table entries). Each {@link Entry} instance is stored at
     * the array index given by its hash code modulo the array size. If several entries must be stored
     * at the same array index, they are linked together via their {@link Entry#next} field. The
     * factory methods of this class make sure that this table does not contain duplicated entries.
     */
    private Entry[] entries;

    /**
     * The number of constant pool items in {@link #constantPool}, plus 1. The first constant pool
     * item has index 1, and long and double items count for two items.
     */
    private int constantPoolCount;

    /**
     * The content of the ClassFile's constant_pool JVMS structure corresponding to this SymbolTable.
     * The ClassFile's constant_pool_count field is <i>not</i> included.
     */
    private ByteVector constantPool;

    /**
     * The actual number of elements in {@link #typeTable}. These elements are stored from index 0 to
     * typeCount (excluded). The other array entries are empty.
     */
    private int typeCount;

    /**
     * Constructs a new, empty SymbolTable for the given ClassWriter.
     *
     * @param classWriter a ClassWriter.
     */
    SymbolTable(final ClassWriter classWriter) {
        this.classWriter = classWriter;
        this.sourceClassReader = null;
        this.entries = new Entry[256];
        this.constantPoolCount = 1;
        this.constantPool = new ByteVector();
    }

    /**
     * Returns the ClassReader from which this SymbolTable was constructed.
     *
     * @return the ClassReader from which this SymbolTable was constructed, or {@literal null} if it
     *     was constructed from scratch.
     */
    ClassReader getSource() {
        return sourceClassReader;
    }

    /**
     * Returns the major version of the class to which this symbol table belongs.
     *
     * @return the major version of the class to which this symbol table belongs.
     */
    int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Returns the length in bytes of this symbol table's constant_pool array.
     *
     * @return the length in bytes of this symbol table's constant_pool array.
     */
    int getConstantPoolLength() {
        return constantPool.length;
    }

    /**
     * Returns the number of items in this symbol table's constant_pool array (plus 1).
     *
     * @return the number of items in this symbol table's constant_pool array (plus 1).
     */
    int getConstantPoolCount() {
        return constantPoolCount;
    }

    /**
     * Returns the internal name of the class to which this symbol table belongs.
     *
     * @return the internal name of the class to which this symbol table belongs.
     */
    String getClassName() {
        return className;
    }

    /**
     * Sets the major version and the name of the class to which this symbol table belongs. Also adds
     * the class name to the constant pool.
     *
     * @param majorVersion a major ClassFile version number.
     * @param className an internal class name.
     * @return the constant pool index of a new or already existing Symbol with the given class name.
     */
    int setMajorVersionAndClassName(final int majorVersion, final String className) {
        this.majorVersion = majorVersion;
        this.className = className;
        return addConstantClass(className).index;
    }

    /**
     * Puts this symbol table's constant_pool array in the given ByteVector, preceded by the
     * constant_pool_count value.
     *
     * @param output where the JVMS ClassFile's constant_pool array must be put.
     */
    void putConstantPool(final ByteVector output) {
        output.putShort(constantPoolCount).putByteArray(constantPool.data, 0, constantPool.length);
    }

    // -----------------------------------------------------------------------------------------------
    // Constant pool entries management.
    // -----------------------------------------------------------------------------------------------

    /**
     * Adds a number or string constant to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param value the value of the constant to be added to the constant pool. This parameter must be
     *     an {@link Integer}, {@link Byte}, {@link Character}, {@link Short}, {@link Boolean}, {@link
     *     Float}, {@link Long}, {@link Double}, {@link String}, {@link Type} or {@link Handle}.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstant(final Object value) {
        if (value instanceof Integer) {
            return addConstantInteger(((Integer) value).intValue());
        } else if (value instanceof Byte) {
            return addConstantInteger(((Byte) value).intValue());
        } else if (value instanceof Character) {
            return addConstantInteger(((Character) value).charValue());
        } else if (value instanceof Short) {
            return addConstantInteger(((Short) value).intValue());
        } else if (value instanceof Boolean) {
            return addConstantInteger(((Boolean) value).booleanValue() ? 1 : 0);
        } else if (value instanceof Float) {
            return addConstantFloat(((Float) value).floatValue());
        } else if (value instanceof Long) {
            return addConstantLong(((Long) value).longValue());
        } else if (value instanceof Double) {
            return addConstantDouble(((Double) value).doubleValue());
        } else if (value instanceof String) {
            return addConstantString((String) value);
        } else if (value instanceof Type) {
            Type type = (Type) value;
            int typeSort = type.getSort();
            if (typeSort == Type.OBJECT) {
                return addConstantClass(type.getInternalName());
            } else if (typeSort == Type.METHOD) {
                return addConstantMethodType(type.getDescriptor());
            } else { // type is a primitive or array type.
                return addConstantClass(type.getDescriptor());
            }
        } else if (value instanceof Handle) {
            Handle handle = (Handle) value;
            return addConstantMethodHandle(
                    handle.getTag(),
                    handle.getOwner(),
                    handle.getName(),
                    handle.getDesc(),
                    handle.isInterface());
        }
//        else if (value instanceof ConstantDynamic) {
//            ConstantDynamic constantDynamic = (ConstantDynamic) value;
//            return addConstantDynamic(
//                    constantDynamic.getName(),
//                    constantDynamic.getDescriptor(),
//                    constantDynamic.getBootstrapMethod(),
//                    constantDynamic.getBootstrapMethodArgumentsUnsafe());
//        }
        else {
            throw new IllegalArgumentException("value " + value);
        }
    }

    /**
     * Adds a CONSTANT_MethodHandle_info to the constant pool of this symbol table. Does nothing if
     * the constant pool already contains a similar item.
     *
     * @param referenceKind one of {@link Opcodes#H_GETFIELD}, {@link Opcodes#H_GETSTATIC}, {@link
     *     Opcodes#H_PUTFIELD}, {@link Opcodes#H_PUTSTATIC}, {@link Opcodes#H_INVOKEVIRTUAL}, {@link
     *     Opcodes#H_INVOKESTATIC}, {@link Opcodes#H_INVOKESPECIAL}, {@link
     *     Opcodes#H_NEWINVOKESPECIAL} or {@link Opcodes#H_INVOKEINTERFACE}.
     * @param owner the internal name of a class of interface.
     * @param name a field or method name.
     * @param descriptor a field or method descriptor.
     * @param isInterface whether owner is an interface or not.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstantMethodHandle(
            final int referenceKind,
            final String owner,
            final String name,
            final String descriptor,
            final boolean isInterface) {
        final int tag = Symbol.CONSTANT_METHOD_HANDLE_TAG;
        // Note that we don't need to include isInterface in the hash computation, because it is
        // redundant with owner (we can't have the same owner with different isInterface values).
        int hashCode = hash(tag, owner, name, descriptor, referenceKind);
        Entry entry = get(hashCode);
        while (entry != null) {
            if (entry.tag == tag
                    && entry.hashCode == hashCode
                    && entry.data == referenceKind
                    && entry.owner.equals(owner)
                    && entry.name.equals(name)
                    && entry.value.equals(descriptor)) {
                return entry;
            }
            entry = entry.next;
        }
        if (referenceKind <= Opcodes.H_PUTSTATIC) {
            constantPool.put112(tag, referenceKind, addConstantFieldref(owner, name, descriptor).index);
        } else {
            constantPool.put112(
                    tag, referenceKind, addConstantMethodref(owner, name, descriptor, isInterface).index);
        }
        return put(
                new Entry(constantPoolCount++, tag, owner, name, descriptor, referenceKind, hashCode));
    }

    /**
     * Adds a CONSTANT_MethodType_info to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param methodDescriptor a method descriptor.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstantMethodType(final String methodDescriptor) {
        return addConstantUtf8Reference(Symbol.CONSTANT_METHOD_TYPE_TAG, methodDescriptor);
    }

    /**
     * Adds a CONSTANT_Integer_info to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param value an int.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstantInteger(final int value) {
        return addConstantIntegerOrFloat(Symbol.CONSTANT_INTEGER_TAG, value);
    }

    /**
     * Adds a CONSTANT_Float_info to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param value a float.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstantFloat(final float value) {
        return addConstantIntegerOrFloat(Symbol.CONSTANT_FLOAT_TAG, Float.floatToRawIntBits(value));
    }

    /**
     * Adds a CONSTANT_Long_info to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param value a long.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstantLong(final long value) {
        return addConstantLongOrDouble(Symbol.CONSTANT_LONG_TAG, value);
    }

    /**
     * Adds a CONSTANT_Double_info to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param value a double.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstantDouble(final double value) {
        return addConstantLongOrDouble(Symbol.CONSTANT_DOUBLE_TAG, Double.doubleToRawLongBits(value));
    }

    /**
     * Adds a CONSTANT_Long_info or CONSTANT_Double_info to the constant pool of this symbol table.
     * Does nothing if the constant pool already contains a similar item.
     *
     * @param tag one of {@link Symbol#CONSTANT_LONG_TAG} or {@link Symbol#CONSTANT_DOUBLE_TAG}.
     * @param value a long or double.
     * @return a constant pool constant with the given tag and primitive values.
     */
    private Symbol addConstantLongOrDouble(final int tag, final long value) {
        int hashCode = hash(tag, value);
        Entry entry = get(hashCode);
        while (entry != null) {
            if (entry.tag == tag && entry.hashCode == hashCode && entry.data == value) {
                return entry;
            }
            entry = entry.next;
        }
        int index = constantPoolCount;
        constantPool.putByte(tag).putLong(value);
        constantPoolCount += 2;
        return put(new Entry(index, tag, value, hashCode));
    }

    /**
     * Adds a CONSTANT_String_info to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param value a string.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstantString(final String value) {
        return addConstantUtf8Reference(Symbol.CONSTANT_STRING_TAG, value);
    }

    /**
     * Adds a CONSTANT_Integer_info or CONSTANT_Float_info to the constant pool of this symbol table.
     * Does nothing if the constant pool already contains a similar item.
     *
     * @param tag one of {@link Symbol#CONSTANT_INTEGER_TAG} or {@link Symbol#CONSTANT_FLOAT_TAG}.
     * @param value an int or float.
     * @return a constant pool constant with the given tag and primitive values.
     */
    private Symbol addConstantIntegerOrFloat(final int tag, final int value) {
        int hashCode = hash(tag, value);
        Entry entry = get(hashCode);
        while (entry != null) {
            if (entry.tag == tag && entry.hashCode == hashCode && entry.data == value) {
                return entry;
            }
            entry = entry.next;
        }
        constantPool.putByte(tag).putInt(value);
        return put(new Entry(constantPoolCount++, tag, value, hashCode));
    }

    /**
     * Adds a CONSTANT_Class_info to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param value the internal name of a class.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstantClass(final String value) {
        return addConstantUtf8Reference(Symbol.CONSTANT_CLASS_TAG, value);
    }

    /**
     * Adds a CONSTANT_Class_info, CONSTANT_String_info, CONSTANT_MethodType_info,
     * CONSTANT_Module_info or CONSTANT_Package_info to the constant pool of this symbol table. Does
     * nothing if the constant pool already contains a similar item.
     *
     * @param tag one of {@link Symbol#CONSTANT_CLASS_TAG}, {@link Symbol#CONSTANT_STRING_TAG}, {@link
     *     Symbol#CONSTANT_METHOD_TYPE_TAG}, {@link Symbol#CONSTANT_MODULE_TAG} or {@link
     *     Symbol#CONSTANT_PACKAGE_TAG}.
     * @param value an internal class name, an arbitrary string, a method descriptor, a module or a
     *     package name, depending on tag.
     * @return a new or already existing Symbol with the given value.
     */
    private Symbol addConstantUtf8Reference(final int tag, final String value) {
        int hashCode = hash(tag, value);
        Entry entry = get(hashCode);
        while (entry != null) {
            if (entry.tag == tag && entry.hashCode == hashCode && entry.value.equals(value)) {
                return entry;
            }
            entry = entry.next;
        }
        constantPool.put12(tag, addConstantUtf8(value));
        return put(new Entry(constantPoolCount++, tag, value, hashCode));
    }

    /**
     * Adds a CONSTANT_Utf8_info to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param value a string.
     * @return a new or already existing Symbol with the given value.
     */
    int addConstantUtf8(final String value) {
        int hashCode = hash(Symbol.CONSTANT_UTF8_TAG, value);
        Entry entry = get(hashCode);
        while (entry != null) {
            if (entry.tag == Symbol.CONSTANT_UTF8_TAG
                    && entry.hashCode == hashCode
                    && entry.value.equals(value)) {
                return entry.index;
            }
            entry = entry.next;
        }
        constantPool.putByte(Symbol.CONSTANT_UTF8_TAG).putUTF8(value);
        return put(new Entry(constantPoolCount++, Symbol.CONSTANT_UTF8_TAG, value, hashCode)).index;
    }

    /**
     * Adds a CONSTANT_Fieldref_info to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param owner the internal name of a class.
     * @param name a field name.
     * @param descriptor a field descriptor.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstantFieldref(final String owner, final String name, final String descriptor) {
        return addConstantMemberReference(Symbol.CONSTANT_FIELDREF_TAG, owner, name, descriptor);
    }

    /**
     * Adds a CONSTANT_Methodref_info or CONSTANT_InterfaceMethodref_info to the constant pool of this
     * symbol table. Does nothing if the constant pool already contains a similar item.
     *
     * @param owner the internal name of a class.
     * @param name a method name.
     * @param descriptor a method descriptor.
     * @param isInterface whether owner is an interface or not.
     * @return a new or already existing Symbol with the given value.
     */
    Symbol addConstantMethodref(
            final String owner, final String name, final String descriptor, final boolean isInterface) {
        int tag = isInterface ? Symbol.CONSTANT_INTERFACE_METHODREF_TAG : Symbol.CONSTANT_METHODREF_TAG;
        return addConstantMemberReference(tag, owner, name, descriptor);
    }

    /**
     * Adds a CONSTANT_NameAndType_info to the constant pool of this symbol table. Does nothing if the
     * constant pool already contains a similar item.
     *
     * @param name a field or method name.
     * @param descriptor a field or method descriptor.
     * @return a new or already existing Symbol with the given value.
     */
    int addConstantNameAndType(final String name, final String descriptor) {
        final int tag = Symbol.CONSTANT_NAME_AND_TYPE_TAG;
        int hashCode = hash(tag, name, descriptor);
        Entry entry = get(hashCode);
        while (entry != null) {
            if (entry.tag == tag
                    && entry.hashCode == hashCode
                    && entry.name.equals(name)
                    && entry.value.equals(descriptor)) {
                return entry.index;
            }
            entry = entry.next;
        }
        constantPool.put122(tag, addConstantUtf8(name), addConstantUtf8(descriptor));
        return put(new Entry(constantPoolCount++, tag, name, descriptor, hashCode)).index;
    }

    /**
     * Adds a CONSTANT_Fieldref_info, CONSTANT_Methodref_info or CONSTANT_InterfaceMethodref_info to
     * the constant pool of this symbol table. Does nothing if the constant pool already contains a
     * similar item.
     *
     * @param tag one of {@link Symbol#CONSTANT_FIELDREF_TAG}, {@link Symbol#CONSTANT_METHODREF_TAG}
     *     or {@link Symbol#CONSTANT_INTERFACE_METHODREF_TAG}.
     * @param owner the internal name of a class.
     * @param name a field or method name.
     * @param descriptor a field or method descriptor.
     * @return a new or already existing Symbol with the given value.
     */
    private Entry addConstantMemberReference(
            final int tag, final String owner, final String name, final String descriptor) {
        int hashCode = hash(tag, owner, name, descriptor);
        Entry entry = get(hashCode);
        while (entry != null) {
            if (entry.tag == tag
                    && entry.hashCode == hashCode
                    && entry.owner.equals(owner)
                    && entry.name.equals(name)
                    && entry.value.equals(descriptor)) {
                return entry;
            }
            entry = entry.next;
        }
        constantPool.put122(
                tag, addConstantClass(owner).index, addConstantNameAndType(name, descriptor));
        return put(new Entry(constantPoolCount++, tag, owner, name, descriptor, 0, hashCode));
    }

    private static int hash(final int tag, final String value) {
        return 0x7FFFFFFF & (tag + value.hashCode());
    }

    private static int hash(
            final int tag, final String value1, final String value2, final String value3) {
        return 0x7FFFFFFF & (tag + value1.hashCode() * value2.hashCode() * value3.hashCode());
    }

    private static int hash(final int tag, final String value1, final String value2) {
        return 0x7FFFFFFF & (tag + value1.hashCode() * value2.hashCode());
    }

    private static int hash(final int tag, final long value) {
        return 0x7FFFFFFF & (tag + (int) value + (int) (value >>> 32));
    }

    private static int hash(
            final int tag,
            final String value1,
            final String value2,
            final String value3,
            final int value4) {
        return 0x7FFFFFFF & (tag + value1.hashCode() * value2.hashCode() * value3.hashCode() * value4);
    }

    // -----------------------------------------------------------------------------------------------
    // Generic symbol table entries management.
    // -----------------------------------------------------------------------------------------------

    /**
     * Returns the list of entries which can potentially have the given hash code.
     *
     * @param hashCode a {@link Entry#hashCode} value.
     * @return the list of entries which can potentially have the given hash code. The list is stored
     *     via the {@link Entry#next} field.
     */
    private Entry get(final int hashCode) {
        return entries[hashCode % entries.length];
    }

    /**
     * Puts the given entry in the {@link #entries} hash set. This method does <i>not</i> check
     * whether {@link #entries} already contains a similar entry or not. {@link #entries} is resized
     * if necessary to avoid hash collisions (multiple entries needing to be stored at the same {@link
     * #entries} array index) as much as possible, with reasonable memory usage.
     *
     * @param entry an Entry (which must not already be contained in {@link #entries}).
     * @return the given entry
     */
    private Entry put(final Entry entry) {
        if (entryCount > (entries.length * 3) / 4) {
            int currentCapacity = entries.length;
            int newCapacity = currentCapacity * 2 + 1;
            Entry[] newEntries = new Entry[newCapacity];
            for (int i = currentCapacity - 1; i >= 0; --i) {
                Entry currentEntry = entries[i];
                while (currentEntry != null) {
                    int newCurrentEntryIndex = currentEntry.hashCode % newCapacity;
                    Entry nextEntry = currentEntry.next;
                    currentEntry.next = newEntries[newCurrentEntryIndex];
                    newEntries[newCurrentEntryIndex] = currentEntry;
                    currentEntry = nextEntry;
                }
            }
            entries = newEntries;
        }
        entryCount++;
        int index = entry.hashCode % entries.length;
        entry.next = entries[index];
        return entries[index] = entry;
    }

    /**
     * An entry of a SymbolTable. This concrete and private subclass of {@link Symbol} adds two fields
     * which are only used inside SymbolTable, to implement hash sets of symbols (in order to avoid
     * duplicate symbols). See {@link #entries}.
     *
     * @author Eric Bruneton
     */
    private static class Entry extends Symbol {
        /** The hash code of this entry. */
        final int hashCode;

        /**
         * Another entry (and so on recursively) having the same hash code (modulo the size of {@link
         * #entries}) as this one.
         */
        Entry next;

        Entry(
                final int index,
                final int tag,
                final String owner,
                final String name,
                final String value,
                final long data,
                final int hashCode) {
            super(index, tag, owner, name, value, data);
            this.hashCode = hashCode;
        }

        Entry(final int index, final int tag, final String value, final int hashCode) {
            super(index, tag, /* owner = */ null, /* name = */ null, value, /* data = */ 0);
            this.hashCode = hashCode;
        }

        Entry(final int index, final int tag, final String value, final long data, final int hashCode) {
            super(index, tag, /* owner = */ null, /* name = */ null, value, data);
            this.hashCode = hashCode;
        }

        Entry(
                final int index, final int tag, final String name, final String value, final int hashCode) {
            super(index, tag, /* owner = */ null, name, value, /* data = */ 0);
            this.hashCode = hashCode;
        }

        Entry(final int index, final int tag, final long data, final int hashCode) {
            super(index, tag, /* owner = */ null, /* name = */ null, /* value = */ null, data);
            this.hashCode = hashCode;
        }
    }
}

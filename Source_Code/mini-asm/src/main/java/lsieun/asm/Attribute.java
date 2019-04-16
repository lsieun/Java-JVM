package lsieun.asm;

public class Attribute {
    /** The type of this attribute, also called its name in the JVMS. */
    public final String type;

    /**
     * The raw content of this attribute, only used for unknown attributes (see {@link #isUnknown()}).
     * The 6 header bytes of the attribute (attribute_name_index and attribute_length) are <i>not</i>
     * included.
     */
    private byte[] content;

    /**
     * The next attribute in this attribute list (Attribute instances can be linked via this field to
     * store a list of class, field, method or Code attributes). May be {@literal null}.
     */
    Attribute nextAttribute;

    /**
     * Constructs a new empty attribute.
     *
     * @param type the type of the attribute.
     */
    protected Attribute(final String type) {
        this.type = type;
    }

    /**
     * Returns {@literal true} if this type of attribute is unknown. This means that the attribute
     * content can't be parsed to extract constant pool references, labels, etc. Instead, the
     * attribute content is read as an opaque byte array, and written back as is. This can lead to
     * invalid attributes, if the content actually contains constant pool references, labels, or other
     * symbolic references that need to be updated when there are changes to the constant pool, the
     * method bytecode, etc. The default implementation of this method always returns {@literal true}.
     *
     * @return {@literal true} if this type of attribute is unknown.
     */
    public boolean isUnknown() {
        return true;
    }

    /**
     * Returns {@literal true} if this type of attribute is a Code attribute.
     *
     * @return {@literal true} if this type of attribute is a Code attribute.
     */
    public boolean isCodeAttribute() {
        return false;
    }


}

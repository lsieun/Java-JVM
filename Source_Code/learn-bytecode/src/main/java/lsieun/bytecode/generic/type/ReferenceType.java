package lsieun.bytecode.generic.type;

import lsieun.bytecode.generic.cst.TypeConst;

/**
 * Super class for object and array types.
 */
public abstract class ReferenceType extends Type {
    protected ReferenceType(final byte t, final String s) {
        super(t, s);
    }


    /** Class is non-abstract but not instantiable from the outside
     */
    ReferenceType() {
        super(TypeConst.T_OBJECT, "<null object>");
    }

    /**
     * This commutative operation returns the first common superclass (narrowest ReferenceType
     * referencing a class, not an interface).
     * If one of the types is a superclass of the other, the former is returned.
     * If "this" is Type.NULL, then t is returned.
     * If t is Type.NULL, then "this" is returned.
     * If "this" equals t ['this.equals(t)'] "this" is returned.
     * If "this" or t is an ArrayType, then Type.OBJECT is returned;
     * unless their dimensions match. Then an ArrayType of the same
     * number of dimensions is returned, with its basic type being the
     * first common super class of the basic types of "this" and t.
     * If "this" or t is a ReferenceType referencing an interface, then Type.OBJECT is returned.
     * If not all of the two classes' superclasses cannot be found, "null" is returned.
     * See the JVM specification edition 2, "�4.9.2 The Bytecode Verifier".
     *
     * @throws ClassNotFoundException on failure to find superclasses of this
     *  type, or the type passed as a parameter
     */
//    public ReferenceType getFirstCommonSuperclass( final ReferenceType t ) throws ClassNotFoundException {
//        if (this.equals(Type.NULL)) {
//            return t;
//        }
//        if (t.equals(Type.NULL)) {
//            return this;
//        }
//        if (this.equals(t)) {
//            return this;
//            /*
//             * TODO: Above sounds a little arbitrary. On the other hand, there is
//             * no object referenced by Type.NULL so we can also say all the objects
//             * referenced by Type.NULL were derived from java.lang.Object.
//             * However, the Java Language's "instanceof" operator proves us wrong:
//             * "null" is not referring to an instance of java.lang.Object :)
//             */
//        }
//        /* This code is from a bug report by Konstantin Shagin <konst@cs.technion.ac.il> */
//        if ((this instanceof ArrayType) && (t instanceof ArrayType)) {
//            final ArrayType arrType1 = (ArrayType) this;
//            final ArrayType arrType2 = (ArrayType) t;
//            if ((arrType1.getDimensions() == arrType2.getDimensions())
//                    && arrType1.getBasicType() instanceof ObjectType
//                    && arrType2.getBasicType() instanceof ObjectType) {
//                return new ArrayType(((ObjectType) arrType1.getBasicType())
//                        .getFirstCommonSuperclass((ObjectType) arrType2.getBasicType()), arrType1
//                        .getDimensions());
//            }
//        }
//        if ((this instanceof ArrayType) || (t instanceof ArrayType)) {
//            return Type.OBJECT;
//            // TODO: Is there a proof of OBJECT being the direct ancestor of every ArrayType?
//        }
//        if (((this instanceof ObjectType) && ((ObjectType) this).referencesInterfaceExact())
//                || ((t instanceof ObjectType) && ((ObjectType) t).referencesInterfaceExact())) {
//            return Type.OBJECT;
//            // TODO: The above line is correct comparing to the vmspec2. But one could
//            // make class file verification a bit stronger here by using the notion of
//            // superinterfaces or even castability or assignment compatibility.
//        }
//        // this and t are ObjectTypes, see above.
//        final ObjectType thiz = (ObjectType) this;
//        final ObjectType other = (ObjectType) t;
//        final JavaClass[] thiz_sups = Repository.getSuperClasses(thiz.getClassName());
//        final JavaClass[] other_sups = Repository.getSuperClasses(other.getClassName());
//        if ((thiz_sups == null) || (other_sups == null)) {
//            return null;
//        }
//        // Waaahh...
//        final JavaClass[] this_sups = new JavaClass[thiz_sups.length + 1];
//        final JavaClass[] t_sups = new JavaClass[other_sups.length + 1];
//        System.arraycopy(thiz_sups, 0, this_sups, 1, thiz_sups.length);
//        System.arraycopy(other_sups, 0, t_sups, 1, other_sups.length);
//        this_sups[0] = Repository.lookupClass(thiz.getClassName());
//        t_sups[0] = Repository.lookupClass(other.getClassName());
//        for (final JavaClass t_sup : t_sups) {
//            for (final JavaClass this_sup : this_sups) {
//                if (this_sup.equals(t_sup)) {
//                    return ObjectType.getInstance(this_sup.getClassName());
//                }
//            }
//        }
//        // Huh? Did you ask for Type.OBJECT's superclass??
//        return null;
//    }
}

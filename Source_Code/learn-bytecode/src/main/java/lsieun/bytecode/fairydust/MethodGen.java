package lsieun.bytecode.fairydust;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.attrs.code.LocalVariable;
import lsieun.bytecode.classfile.attrs.code.LocalVariableTable;
import lsieun.bytecode.classfile.attrs.code.LocalVariableTypeTable;
import lsieun.bytecode.exceptions.ClassGenException;
import lsieun.bytecode.generic.cst.TypeConst;
import lsieun.bytecode.generic.instruction.InstructionList;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.type.ObjectType;
import lsieun.bytecode.generic.type.Type;
import lsieun.bytecode.utils.BCELComparator;

/**
 * Template class for building up a method. This is done by defining exception
 * handlers, adding thrown exceptions, local variables and attributes, whereas
 * the `LocalVariableTable' and `LineNumberTable' attributes will be set
 * automatically for the code. Use stripAttributes() if you don't like this.
 * <p>
 * While generating code it may be necessary to insert NOP operations. You can
 * use the `removeNOPs' method to get rid off them.
 * The resulting method object can be obtained via the `getMethod()' method.
 */
public class MethodGen extends FieldGenOrMethodGen {
    private String class_name;
    private Type[] arg_types;
    private String[] arg_names;
    private int max_locals;
    private int max_stack;
    private InstructionList il;
    private boolean strip_attributes;
    private LocalVariableTypeTable local_variable_type_table = null;
    private final List<LocalVariableGen> variable_vec = new ArrayList();
    private final List<LineNumberGen> line_number_vec = new ArrayList();
    private final List<CodeExceptionGen> exception_vec = new ArrayList();
    private final List<String> throws_vec = new ArrayList();
    private final List<AttributeInfo> code_attrs_vec = new ArrayList();

    private static BCELComparator bcelComparator = new BCELComparator() {

        @Override
        public boolean equals(final Object obj1, final Object obj2) {
            final MethodGen THIS = (MethodGen) obj1;
            final MethodGen THAT = (MethodGen) obj2;
            return Objects.equals(THIS.getName(), THAT.getName())
                    && Objects.equals(THIS.getSignature(), THAT.getSignature());
        }


        @Override
        public int hashCode(final Object o) {
            final MethodGen THIS = (MethodGen) o;
            return THIS.getSignature().hashCode() ^ THIS.getName().hashCode();
        }
    };

    /**
     * Declare method. If the method is non-static the constructor
     * automatically declares a local variable `$this' in slot 0. The
     * actual code is contained in the `il' parameter, which may further
     * manipulated by the user. But he must take care not to remove any
     * instruction (handles) that are still referenced from this object.
     * <p>
     * For example one may not add a local variable and later remove the
     * instructions it refers to without causing havoc. It is safe
     * however if you remove that local variable, too.
     *
     * @param access_flags access qualifiers
     * @param return_type  method type
     * @param arg_types    argument types
     * @param arg_names    argument names (if this is null, default names will be provided
     *                     for them)
     * @param method_name  name of method
     * @param class_name   class name containing this method (may be null, if you don't care)
     * @param il           instruction list associated with this method, may be null only for
     *                     abstract or native methods
     * @param cp           constant pool
     */
    public MethodGen(final int access_flags, final Type return_type, final Type[] arg_types, String[] arg_names,
                     final String method_name, final String class_name, final InstructionList il, final ConstantPoolGen cp) {
        super(access_flags);
        setType(return_type);
        setArgumentTypes(arg_types);
        setArgumentNames(arg_names);
        setName(method_name);
        setClassName(class_name);
        setInstructionList(il);
        setConstantPool(cp);
        final boolean abstract_ = isAbstract() || isNative();
        InstructionHandle start = null;
        final InstructionHandle end = null;
        if (!abstract_) {
            start = il.getStart();
            // end == null => live to end of method
            /* Add local variables, namely the implicit `this' and the arguments
             */
            if (!isStatic() && (class_name != null)) { // Instance method -> `this' is local var 0
                addLocalVariable("this", ObjectType.getInstance(class_name), start, end);
            }
        }
        if (arg_types != null) {
            final int size = arg_types.length;
            for (final Type arg_type : arg_types) {
                if (Type.VOID == arg_type) {
                    throw new ClassGenException("'void' is an illegal argument type for a method");
                }
            }
            if (arg_names != null) { // Names for variables provided?
                if (size != arg_names.length) {
                    throw new ClassGenException("Mismatch in argument array lengths: " + size
                            + " vs. " + arg_names.length);
                }
            } else { // Give them dummy names
                arg_names = new String[size];
                for (int i = 0; i < size; i++) {
                    arg_names[i] = "arg" + i;
                }
                setArgumentNames(arg_names);
            }
            if (!abstract_) {
                for (int i = 0; i < size; i++) {
                    addLocalVariable(arg_names[i], arg_types[i], start, end);
                }
            }
        }
    }

    /**
     * Instantiate from existing method.
     *
     * @param m          method
     * @param class_name class name containing this method
     * @param cp         constant pool
     */
    public MethodGen(final Method m, final String class_name, final ConstantPoolGen cp) {
        this(m.getAccessFlags(), Type.getReturnType(m.getSignature()), Type.getArgumentTypes(m
                        .getSignature()), null /* may be overridden anyway */
                , m.getName(), class_name,
                ((m.getAccessFlags() & (Const.ACC_ABSTRACT | Const.ACC_NATIVE)) == 0)
                        ? new InstructionList(m.getCode().getCode())
                        : null, cp);
        final Attribute[] attributes = m.getAttributes();
        for (final Attribute attribute : attributes) {
            Attribute a = attribute;
            if (a instanceof Code) {
                final Code c = (Code) a;
                setMaxStack(c.getMaxStack());
                setMaxLocals(c.getMaxLocals());
                final CodeException[] ces = c.getExceptionTable();
                if (ces != null) {
                    for (final CodeException ce : ces) {
                        final int type = ce.getCatchType();
                        ObjectType c_type = null;
                        if (type > 0) {
                            final String cen = m.getConstantPool().getConstantString(type,
                                    Const.CONSTANT_Class);
                            c_type = ObjectType.getInstance(cen);
                        }
                        final int end_pc = ce.getEndPC();
                        final int length = m.getCode().getCode().length;
                        InstructionHandle end;
                        if (length == end_pc) { // May happen, because end_pc is exclusive
                            end = il.getEnd();
                        } else {
                            end = il.findHandle(end_pc);
                            end = end.getPrev(); // Make it inclusive
                        }
                        addExceptionHandler(il.findHandle(ce.getStartPC()), end, il.findHandle(ce
                                .getHandlerPC()), c_type);
                    }
                }
                final Attribute[] c_attributes = c.getAttributes();
                for (final Attribute c_attribute : c_attributes) {
                    a = c_attribute;
                    if (a instanceof LineNumberTable) {
                        final LineNumber[] ln = ((LineNumberTable) a).getLineNumberTable();
                        for (final LineNumber l : ln) {
                            final InstructionHandle ih = il.findHandle(l.getStartPC());
                            if (ih != null) {
                                addLineNumber(ih, l.getLineNumber());
                            }
                        }
                    } else if (a instanceof LocalVariableTable) {
                        updateLocalVariableTable((LocalVariableTable) a);
                    } else if (a instanceof LocalVariableTypeTable) {
                        this.local_variable_type_table = (LocalVariableTypeTable) a.copy(cp.getConstantPool());
                    } else {
                        addCodeAttribute(a);
                    }
                }
            } else if (a instanceof ExceptionTable) {
                final String[] names = ((ExceptionTable) a).getExceptionNames();
                for (final String name2 : names) {
                    addException(name2);
                }
            } else if (a instanceof Annotations) {
                final Annotations runtimeAnnotations = (Annotations) a;
                final AnnotationEntry[] aes = runtimeAnnotations.getAnnotationEntries();
                for (final AnnotationEntry element : aes) {
                    addAnnotationEntry(new AnnotationEntryGen(element, cp, false));
                }
            } else {
                addAttribute(a);
            }
        }
    }

    /**
     * Adds a local variable to this method.
     *
     * @param name       variable name
     * @param type       variable type
     * @param slot       the index of the local variable, if type is long or double, the next available
     *                   index is slot+2
     * @param start      from where the variable is valid
     * @param end        until where the variable is valid
     * @param orig_index the index of the local variable prior to any modifications
     * @return new local variable object
     */
    public LocalVariableGen addLocalVariable(final String name, final Type type, final int slot,
                                             final InstructionHandle start, final InstructionHandle end, final int orig_index) {
        final byte t = type.getType();
        if (t != TypeConst.T_ADDRESS) {
            final int add = type.getSize();
            if (slot + add > max_locals) {
                max_locals = slot + add;
            }
            final LocalVariableGen l = new LocalVariableGen(slot, name, type, start, end, orig_index);
            int i;
            if ((i = variable_vec.indexOf(l)) >= 0) {
                variable_vec.set(i, l);
            } else {
                variable_vec.add(l);
            }
            return l;
        }
        throw new IllegalArgumentException("Can not use " + type
                + " as type for local variable");
    }

    /**
     * Adds a local variable to this method.
     *
     * @param name  variable name
     * @param type  variable type
     * @param slot  the index of the local variable, if type is long or double, the next available
     *              index is slot+2
     * @param start from where the variable is valid
     * @param end   until where the variable is valid
     * @return new local variable object
     */
    public LocalVariableGen addLocalVariable(final String name, final Type type, final int slot,
                                             final InstructionHandle start, final InstructionHandle end) {
        return addLocalVariable(name, type, slot, start, end, slot);
    }

    /**
     * Adds a local variable to this method and assigns an index automatically.
     *
     * @param name  variable name
     * @param type  variable type
     * @param start from where the variable is valid, if this is null,
     *              it is valid from the start
     * @param end   until where the variable is valid, if this is null,
     *              it is valid to the end
     * @return new local variable object
     */
    public LocalVariableGen addLocalVariable(final String name, final Type type, final InstructionHandle start,
                                             final InstructionHandle end) {
        return addLocalVariable(name, type, max_locals, start, end);
    }

    /**
     * Remove a local variable, its slot will not be reused, if you do not use addLocalVariable
     * with an explicit index argument.
     */
    public void removeLocalVariable( final LocalVariableGen l ) {
        l.dispose();
        variable_vec.remove(l);
    }

    /**
     * Remove all local variables.
     */
    public void removeLocalVariables() {
        for (final LocalVariableGen item : variable_vec) {
            item.dispose();
        }
        variable_vec.clear();
    }

    /*
     * If the range of the variable has not been set yet, it will be set to be valid from
     * the start to the end of the instruction list.
     *
     * @return array of declared local variables sorted by index
     */
    public LocalVariableGen[] getLocalVariables() {
        final int size = variable_vec.size();
        final LocalVariableGen[] lg = new LocalVariableGen[size];
        variable_vec.toArray(lg);
        for (int i = 0; i < size; i++) {
            if ((lg[i].getStart() == null) && (il != null)) {
                lg[i].setStart(il.getStart());
            }
            if ((lg[i].getEnd() == null) && (il != null)) {
                lg[i].setEnd(il.getEnd());
            }
        }
        if (size > 1) {
            Arrays.sort(lg, new Comparator<LocalVariableGen>() {
                @Override
                public int compare(final LocalVariableGen o1, final LocalVariableGen o2) {
                    return o1.getIndex() - o2.getIndex();
                }
            });
        }
        return lg;
    }

    /**
     * @return `LocalVariableTable' attribute of all the local variables of this method.
     */
    public LocalVariableTable getLocalVariableTable(final ConstantPoolGen cp ) {
        final LocalVariableGen[] lg = getLocalVariables();
        final int size = lg.length;
        final LocalVariable[] lv = new LocalVariable[size];
        for (int i = 0; i < size; i++) {
            lv[i] = lg[i].getLocalVariable(cp);
        }
        return new LocalVariableTable(cp.addUtf8("LocalVariableTable"), 2 + lv.length * 10, lv, cp
                .getConstantPool());
    }
}

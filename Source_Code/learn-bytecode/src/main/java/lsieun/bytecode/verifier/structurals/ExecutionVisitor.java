package lsieun.bytecode.verifier.structurals;

import lsieun.bytecode.classfile.cp.Constant;
import lsieun.bytecode.classfile.cp.ConstantClass;
import lsieun.bytecode.classfile.cp.ConstantDouble;
import lsieun.bytecode.classfile.cp.ConstantFloat;
import lsieun.bytecode.classfile.cp.ConstantInteger;
import lsieun.bytecode.classfile.cp.ConstantLong;
import lsieun.bytecode.classfile.cp.ConstantString;
import lsieun.bytecode.generic.cst.JVMConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.visitor.EmptyVisitor;
import lsieun.bytecode.generic.opcode.ARRAYLENGTH;
import lsieun.bytecode.generic.opcode.ATHROW;
import lsieun.bytecode.generic.opcode.CHECKCAST;
import lsieun.bytecode.generic.opcode.INSTANCEOF;
import lsieun.bytecode.generic.opcode.MONITORENTER;
import lsieun.bytecode.generic.opcode.MONITOREXIT;
import lsieun.bytecode.generic.opcode.NOP;
import lsieun.bytecode.generic.opcode.allocate.ANEWARRAY;
import lsieun.bytecode.generic.opcode.allocate.MULTIANEWARRAY;
import lsieun.bytecode.generic.opcode.allocate.NEW;
import lsieun.bytecode.generic.opcode.allocate.NEWARRAY;
import lsieun.bytecode.generic.opcode.arithmetic.DADD;
import lsieun.bytecode.generic.opcode.arithmetic.DDIV;
import lsieun.bytecode.generic.opcode.arithmetic.DMUL;
import lsieun.bytecode.generic.opcode.arithmetic.DNEG;
import lsieun.bytecode.generic.opcode.arithmetic.DREM;
import lsieun.bytecode.generic.opcode.arithmetic.DSUB;
import lsieun.bytecode.generic.opcode.arithmetic.FADD;
import lsieun.bytecode.generic.opcode.arithmetic.FDIV;
import lsieun.bytecode.generic.opcode.arithmetic.FMUL;
import lsieun.bytecode.generic.opcode.arithmetic.FNEG;
import lsieun.bytecode.generic.opcode.arithmetic.FREM;
import lsieun.bytecode.generic.opcode.arithmetic.FSUB;
import lsieun.bytecode.generic.opcode.arithmetic.IADD;
import lsieun.bytecode.generic.opcode.arithmetic.IAND;
import lsieun.bytecode.generic.opcode.arithmetic.IDIV;
import lsieun.bytecode.generic.opcode.arithmetic.IMUL;
import lsieun.bytecode.generic.opcode.arithmetic.INEG;
import lsieun.bytecode.generic.opcode.arithmetic.IOR;
import lsieun.bytecode.generic.opcode.arithmetic.IREM;
import lsieun.bytecode.generic.opcode.arithmetic.ISHL;
import lsieun.bytecode.generic.opcode.arithmetic.ISHR;
import lsieun.bytecode.generic.opcode.arithmetic.ISUB;
import lsieun.bytecode.generic.opcode.arithmetic.IUSHR;
import lsieun.bytecode.generic.opcode.arithmetic.IXOR;
import lsieun.bytecode.generic.opcode.arithmetic.LADD;
import lsieun.bytecode.generic.opcode.arithmetic.LAND;
import lsieun.bytecode.generic.opcode.arithmetic.LDIV;
import lsieun.bytecode.generic.opcode.arithmetic.LMUL;
import lsieun.bytecode.generic.opcode.arithmetic.LNEG;
import lsieun.bytecode.generic.opcode.arithmetic.LOR;
import lsieun.bytecode.generic.opcode.arithmetic.LREM;
import lsieun.bytecode.generic.opcode.arithmetic.LSHL;
import lsieun.bytecode.generic.opcode.arithmetic.LSHR;
import lsieun.bytecode.generic.opcode.arithmetic.LSUB;
import lsieun.bytecode.generic.opcode.arithmetic.LUSHR;
import lsieun.bytecode.generic.opcode.arithmetic.LXOR;
import lsieun.bytecode.generic.opcode.array.AALOAD;
import lsieun.bytecode.generic.opcode.array.AASTORE;
import lsieun.bytecode.generic.opcode.array.BALOAD;
import lsieun.bytecode.generic.opcode.array.BASTORE;
import lsieun.bytecode.generic.opcode.array.CALOAD;
import lsieun.bytecode.generic.opcode.array.CASTORE;
import lsieun.bytecode.generic.opcode.array.DALOAD;
import lsieun.bytecode.generic.opcode.array.DASTORE;
import lsieun.bytecode.generic.opcode.array.FALOAD;
import lsieun.bytecode.generic.opcode.array.FASTORE;
import lsieun.bytecode.generic.opcode.array.IALOAD;
import lsieun.bytecode.generic.opcode.array.IASTORE;
import lsieun.bytecode.generic.opcode.array.LALOAD;
import lsieun.bytecode.generic.opcode.array.LASTORE;
import lsieun.bytecode.generic.opcode.array.SALOAD;
import lsieun.bytecode.generic.opcode.array.SASTORE;
import lsieun.bytecode.generic.opcode.branh.GOTO;
import lsieun.bytecode.generic.opcode.branh.GOTO_W;
import lsieun.bytecode.generic.opcode.branh.IFEQ;
import lsieun.bytecode.generic.opcode.branh.IFGE;
import lsieun.bytecode.generic.opcode.branh.IFGT;
import lsieun.bytecode.generic.opcode.branh.IFLE;
import lsieun.bytecode.generic.opcode.branh.IFLT;
import lsieun.bytecode.generic.opcode.branh.IFNE;
import lsieun.bytecode.generic.opcode.branh.IFNONNULL;
import lsieun.bytecode.generic.opcode.branh.IFNULL;
import lsieun.bytecode.generic.opcode.branh.IF_ACMPEQ;
import lsieun.bytecode.generic.opcode.branh.IF_ACMPNE;
import lsieun.bytecode.generic.opcode.branh.IF_ICMPEQ;
import lsieun.bytecode.generic.opcode.branh.IF_ICMPGE;
import lsieun.bytecode.generic.opcode.branh.IF_ICMPGT;
import lsieun.bytecode.generic.opcode.branh.IF_ICMPLE;
import lsieun.bytecode.generic.opcode.branh.IF_ICMPLT;
import lsieun.bytecode.generic.opcode.branh.IF_ICMPNE;
import lsieun.bytecode.generic.opcode.branh.JSR;
import lsieun.bytecode.generic.opcode.branh.JSR_W;
import lsieun.bytecode.generic.opcode.branh.LOOKUPSWITCH;
import lsieun.bytecode.generic.opcode.branh.RET;
import lsieun.bytecode.generic.opcode.branh.TABLESWITCH;
import lsieun.bytecode.generic.opcode.compare.DCMPG;
import lsieun.bytecode.generic.opcode.compare.DCMPL;
import lsieun.bytecode.generic.opcode.compare.FCMPG;
import lsieun.bytecode.generic.opcode.compare.FCMPL;
import lsieun.bytecode.generic.opcode.compare.LCMP;
import lsieun.bytecode.generic.opcode.conversion.D2F;
import lsieun.bytecode.generic.opcode.conversion.D2I;
import lsieun.bytecode.generic.opcode.conversion.D2L;
import lsieun.bytecode.generic.opcode.conversion.F2D;
import lsieun.bytecode.generic.opcode.conversion.F2I;
import lsieun.bytecode.generic.opcode.conversion.F2L;
import lsieun.bytecode.generic.opcode.conversion.I2B;
import lsieun.bytecode.generic.opcode.conversion.I2C;
import lsieun.bytecode.generic.opcode.conversion.I2D;
import lsieun.bytecode.generic.opcode.conversion.I2F;
import lsieun.bytecode.generic.opcode.conversion.I2L;
import lsieun.bytecode.generic.opcode.conversion.I2S;
import lsieun.bytecode.generic.opcode.conversion.L2D;
import lsieun.bytecode.generic.opcode.conversion.L2F;
import lsieun.bytecode.generic.opcode.conversion.L2I;
import lsieun.bytecode.generic.opcode.cst.ACONST_NULL;
import lsieun.bytecode.generic.opcode.cst.BIPUSH;
import lsieun.bytecode.generic.opcode.cst.DCONST;
import lsieun.bytecode.generic.opcode.cst.FCONST;
import lsieun.bytecode.generic.opcode.cst.ICONST;
import lsieun.bytecode.generic.opcode.cst.LCONST;
import lsieun.bytecode.generic.opcode.cst.LDC;
import lsieun.bytecode.generic.opcode.cst.LDC2_W;
import lsieun.bytecode.generic.opcode.cst.LDC_W;
import lsieun.bytecode.generic.opcode.cst.SIPUSH;
import lsieun.bytecode.generic.opcode.field.GETFIELD;
import lsieun.bytecode.generic.opcode.field.GETSTATIC;
import lsieun.bytecode.generic.opcode.field.PUTFIELD;
import lsieun.bytecode.generic.opcode.field.PUTSTATIC;
import lsieun.bytecode.generic.opcode.invoke.INVOKEDYNAMIC;
import lsieun.bytecode.generic.opcode.invoke.INVOKEINTERFACE;
import lsieun.bytecode.generic.opcode.invoke.INVOKESPECIAL;
import lsieun.bytecode.generic.opcode.invoke.INVOKESTATIC;
import lsieun.bytecode.generic.opcode.invoke.INVOKEVIRTUAL;
import lsieun.bytecode.generic.opcode.locals.ALOAD;
import lsieun.bytecode.generic.opcode.locals.ASTORE;
import lsieun.bytecode.generic.opcode.locals.DLOAD;
import lsieun.bytecode.generic.opcode.locals.DSTORE;
import lsieun.bytecode.generic.opcode.locals.FLOAD;
import lsieun.bytecode.generic.opcode.locals.FSTORE;
import lsieun.bytecode.generic.opcode.locals.IINC;
import lsieun.bytecode.generic.opcode.locals.ILOAD;
import lsieun.bytecode.generic.opcode.locals.ISTORE;
import lsieun.bytecode.generic.opcode.locals.LLOAD;
import lsieun.bytecode.generic.opcode.locals.LSTORE;
import lsieun.bytecode.generic.opcode.ret.ARETURN;
import lsieun.bytecode.generic.opcode.ret.DRETURN;
import lsieun.bytecode.generic.opcode.ret.FRETURN;
import lsieun.bytecode.generic.opcode.ret.IRETURN;
import lsieun.bytecode.generic.opcode.ret.LRETURN;
import lsieun.bytecode.generic.opcode.ret.RETURN;
import lsieun.bytecode.generic.opcode.stack.DUP;
import lsieun.bytecode.generic.opcode.stack.DUP2;
import lsieun.bytecode.generic.opcode.stack.DUP2_X1;
import lsieun.bytecode.generic.opcode.stack.DUP2_X2;
import lsieun.bytecode.generic.opcode.stack.DUP_X1;
import lsieun.bytecode.generic.opcode.stack.DUP_X2;
import lsieun.bytecode.generic.opcode.stack.POP;
import lsieun.bytecode.generic.opcode.stack.POP2;
import lsieun.bytecode.generic.opcode.stack.SWAP;
import lsieun.bytecode.generic.type.ArrayType;
import lsieun.bytecode.generic.type.ObjectType;
import lsieun.bytecode.generic.type.ReturnaddressType;
import lsieun.bytecode.generic.type.Type;

public class ExecutionVisitor extends EmptyVisitor {
    /**
     * The executionframe we're operating on.
     */
    private Frame frame = null;

    /**
     * The ConstantPoolGen we're working with.
     *
     * @see #setConstantPoolGen(ConstantPoolGen)
     */
    private ConstantPoolGen cpg = null;

    /**
     * Constructor. Constructs a new instance of this class.
     */
    public ExecutionVisitor() {
    }

    /**
     * The OperandStack from the current Frame we're operating on.
     *
     * @see #setFrame(Frame)
     */
    private OperandStack stack() {
        return frame.getStack();
    }

    /**
     * The LocalVariables from the current Frame we're operating on.
     *
     * @see #setFrame(Frame)
     */
    private LocalVariables locals() {
        return frame.getLocals();
    }

    /**
     * Sets the ConstantPoolGen needed for symbolic execution.
     */
    public void setConstantPoolGen(final ConstantPoolGen cpg) { // TODO could be package-protected?
        this.cpg = cpg;
    }

    /**
     * The only method granting access to the single instance of
     * the ExecutionVisitor class. Before actively using this
     * instance, <B>SET THE ConstantPoolGen FIRST</B>.
     *
     * @see #setConstantPoolGen(ConstantPoolGen)
     */
    public void setFrame(final Frame f) { // TODO could be package-protected?
        this.frame = f;
    }

    @Override
    public void visitAALOAD(final AALOAD o) {
        stack().pop();                                                        // pop the index int
        //System.out.print(stack().peek());
        final Type t = stack().pop(); // Pop Array type
        if (t == Type.NULL) {
            stack().push(Type.NULL);
        }    // Do nothing stackwise --- a NullPointerException is thrown at Run-Time
        else {
            final ArrayType at = (ArrayType) t;
            stack().push(at.getElementType());
        }
    }

    @Override
    public void visitAASTORE(final AASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitACONST_NULL(final ACONST_NULL o) {
        stack().push(Type.NULL);
    }

    @Override
    public void visitALOAD(final ALOAD o) {
        stack().push(locals().get(o.getIndex()));
    }

    @Override
    public void visitANEWARRAY(final ANEWARRAY o) {
        stack().pop(); //count
        stack().push(new ArrayType(o.getType(cpg), 1));
    }

    @Override
    public void visitARETURN(final ARETURN o) {
        stack().pop();
    }

    @Override
    public void visitARRAYLENGTH(final ARRAYLENGTH o) {
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitASTORE(final ASTORE o) {
        locals().set(o.getIndex(), stack().pop());
        //System.err.println("TODO-DEBUG:    set LV '"+o.getIndex()+"' to '"+locals().get(o.getIndex())+"'.");
    }

    @Override
    public void visitATHROW(final ATHROW o) {
        final Type t = stack().pop();
        stack().clear();
        if (t.equals(Type.NULL)) {
            stack().push(Type.getType("Ljava/lang/NullPointerException;"));
        } else {
            stack().push(t);
        }
    }

    @Override
    public void visitBALOAD(final BALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitBASTORE(final BASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitBIPUSH(final BIPUSH o) {
        stack().push(Type.INT);
    }

    @Override
    public void visitCALOAD(final CALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitCASTORE(final CASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitCHECKCAST(final CHECKCAST o) {
        // It's possibly wrong to do so, but SUN's
        // ByteCode verifier seems to do (only) this, too.
        // TODO: One could use a sophisticated analysis here to check
        //       if a type cannot possibly be cated to another and by
        //       so doing predict the ClassCastException at run-time.
        stack().pop();
        stack().push(o.getType(cpg));
    }

    @Override
    public void visitD2F(final D2F o) {
        stack().pop();
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitD2I(final D2I o) {
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitD2L(final D2L o) {
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitDADD(final DADD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitDALOAD(final DALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitDASTORE(final DASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitDCMPG(final DCMPG o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitDCMPL(final DCMPL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitDCONST(final DCONST o) {
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitDDIV(final DDIV o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitDLOAD(final DLOAD o) {
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitDMUL(final DMUL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitDNEG(final DNEG o) {
        stack().pop();
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitDREM(final DREM o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitDRETURN(final DRETURN o) {
        stack().pop();
    }

    @Override
    public void visitDSTORE(final DSTORE o) {
        locals().set(o.getIndex(), stack().pop());
        locals().set(o.getIndex() + 1, Type.UNKNOWN);
    }

    @Override
    public void visitDSUB(final DSUB o) {
        stack().pop();
        stack().pop();
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitDUP(final DUP o) {
        final Type t = stack().pop();
        stack().push(t);
        stack().push(t);
    }

    @Override
    public void visitDUP_X1(final DUP_X1 o) {
        final Type w1 = stack().pop();
        final Type w2 = stack().pop();
        stack().push(w1);
        stack().push(w2);
        stack().push(w1);
    }

    @Override
    public void visitDUP_X2(final DUP_X2 o) {
        final Type w1 = stack().pop();
        final Type w2 = stack().pop();
        if (w2.getSize() == 2) {
            stack().push(w1);
            stack().push(w2);
            stack().push(w1);
        } else {
            final Type w3 = stack().pop();
            stack().push(w1);
            stack().push(w3);
            stack().push(w2);
            stack().push(w1);
        }
    }

    @Override
    public void visitDUP2(final DUP2 o) {
        final Type t = stack().pop();
        if (t.getSize() == 2) {
            stack().push(t);
            stack().push(t);
        } else { // t.getSize() is 1
            final Type u = stack().pop();
            stack().push(u);
            stack().push(t);
            stack().push(u);
            stack().push(t);
        }
    }

    @Override
    public void visitDUP2_X1(final DUP2_X1 o) {
        final Type t = stack().pop();
        if (t.getSize() == 2) {
            final Type u = stack().pop();
            stack().push(t);
            stack().push(u);
            stack().push(t);
        } else { //t.getSize() is1
            final Type u = stack().pop();
            final Type v = stack().pop();
            stack().push(u);
            stack().push(t);
            stack().push(v);
            stack().push(u);
            stack().push(t);
        }
    }

    @Override
    public void visitDUP2_X2(final DUP2_X2 o) {
        final Type t = stack().pop();
        if (t.getSize() == 2) {
            final Type u = stack().pop();
            if (u.getSize() == 2) {
                stack().push(t);
                stack().push(u);
                stack().push(t);
            } else {
                final Type v = stack().pop();
                stack().push(t);
                stack().push(v);
                stack().push(u);
                stack().push(t);
            }
        } else { //t.getSize() is 1
            final Type u = stack().pop();
            final Type v = stack().pop();
            if (v.getSize() == 2) {
                stack().push(u);
                stack().push(t);
                stack().push(v);
                stack().push(u);
                stack().push(t);
            } else {
                final Type w = stack().pop();
                stack().push(u);
                stack().push(t);
                stack().push(w);
                stack().push(v);
                stack().push(u);
                stack().push(t);
            }
        }
    }

    @Override
    public void visitF2D(final F2D o) {
        stack().pop();
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitF2I(final F2I o) {
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitF2L(final F2L o) {
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitFADD(final FADD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitFALOAD(final FALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitFASTORE(final FASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitFCMPG(final FCMPG o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitFCMPL(final FCMPL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitFCONST(final FCONST o) {
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitFDIV(final FDIV o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitFLOAD(final FLOAD o) {
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitFMUL(final FMUL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitFNEG(final FNEG o) {
        stack().pop();
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitFREM(final FREM o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitFRETURN(final FRETURN o) {
        stack().pop();
    }

    @Override
    public void visitFSTORE(final FSTORE o) {
        locals().set(o.getIndex(), stack().pop());
    }

    @Override
    public void visitFSUB(final FSUB o) {
        stack().pop();
        stack().pop();
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitGETFIELD(final GETFIELD o) {
        stack().pop();
        Type t = o.getFieldType(cpg);
        if (t.equals(Type.BOOLEAN) ||
                t.equals(Type.CHAR) ||
                t.equals(Type.BYTE) ||
                t.equals(Type.SHORT)) {
            t = Type.INT;
        }
        stack().push(t);
    }

    @Override
    public void visitGETSTATIC(final GETSTATIC o) {
        Type t = o.getFieldType(cpg);
        if (t.equals(Type.BOOLEAN) ||
                t.equals(Type.CHAR) ||
                t.equals(Type.BYTE) ||
                t.equals(Type.SHORT)) {
            t = Type.INT;
        }
        stack().push(t);
    }

    @Override
    public void visitGOTO(final GOTO o) {
        // no stack changes.
    }

    @Override
    public void visitGOTO_W(final GOTO_W o) {
        // no stack changes.
    }

    @Override
    public void visitI2B(final I2B o) {
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitI2C(final I2C o) {
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitI2D(final I2D o) {
        stack().pop();
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitI2F(final I2F o) {
        stack().pop();
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitI2L(final I2L o) {
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitI2S(final I2S o) {
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitIADD(final IADD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitIALOAD(final IALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitIAND(final IAND o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitIASTORE(final IASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitICONST(final ICONST o) {
        stack().push(Type.INT);
    }

    @Override
    public void visitIDIV(final IDIV o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitIF_ACMPEQ(final IF_ACMPEQ o) {
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitIF_ACMPNE(final IF_ACMPNE o) {
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitIF_ICMPEQ(final IF_ICMPEQ o) {
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitIF_ICMPGE(final IF_ICMPGE o) {
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitIF_ICMPGT(final IF_ICMPGT o) {
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitIF_ICMPLE(final IF_ICMPLE o) {
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitIF_ICMPLT(final IF_ICMPLT o) {
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitIF_ICMPNE(final IF_ICMPNE o) {
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitIFEQ(final IFEQ o) {
        stack().pop();
    }

    @Override
    public void visitIFGE(final IFGE o) {
        stack().pop();
    }

    @Override
    public void visitIFGT(final IFGT o) {
        stack().pop();
    }

    @Override
    public void visitIFLE(final IFLE o) {
        stack().pop();
    }

    @Override
    public void visitIFLT(final IFLT o) {
        stack().pop();
    }

    @Override
    public void visitIFNE(final IFNE o) {
        stack().pop();
    }

    @Override
    public void visitIFNONNULL(final IFNONNULL o) {
        stack().pop();
    }

    @Override
    public void visitIFNULL(final IFNULL o) {
        stack().pop();
    }

    @Override
    public void visitIINC(final IINC o) {
        // stack is not changed.
    }

    @Override
    public void visitILOAD(final ILOAD o) {
        stack().push(Type.INT);
    }

    @Override
    public void visitIMUL(final IMUL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitINEG(final INEG o) {
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitINSTANCEOF(final INSTANCEOF o) {
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitINVOKEDYNAMIC(final INVOKEDYNAMIC o) {
        for (int i = 0; i < o.getArgumentTypes(cpg).length; i++) {
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID) {
            Type t = o.getReturnType(cpg);
            if (t.equals(Type.BOOLEAN) ||
                    t.equals(Type.CHAR) ||
                    t.equals(Type.BYTE) ||
                    t.equals(Type.SHORT)) {
                t = Type.INT;
            }
            stack().push(t);
        }
    }

    @Override
    public void visitINVOKEINTERFACE(final INVOKEINTERFACE o) {
        stack().pop();    //objectref
        for (int i = 0; i < o.getArgumentTypes(cpg).length; i++) {
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID) {
            Type t = o.getReturnType(cpg);
            if (t.equals(Type.BOOLEAN) ||
                    t.equals(Type.CHAR) ||
                    t.equals(Type.BYTE) ||
                    t.equals(Type.SHORT)) {
                t = Type.INT;
            }
            stack().push(t);
        }
    }

    @Override
    public void visitINVOKESPECIAL(final INVOKESPECIAL o) {
        if (o.getMethodName(cpg).equals(JVMConst.CONSTRUCTOR_NAME)) {
            final UninitializedObjectType t = (UninitializedObjectType) stack().peek(o.getArgumentTypes(cpg).length);
            if (t == Frame.getThis()) {
                Frame.setThis(null);
            }
            stack().initializeObject(t);
            locals().initializeObject(t);
        }
        stack().pop();    //objectref
        for (int i = 0; i < o.getArgumentTypes(cpg).length; i++) {
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID) {
            Type t = o.getReturnType(cpg);
            if (t.equals(Type.BOOLEAN) ||
                    t.equals(Type.CHAR) ||
                    t.equals(Type.BYTE) ||
                    t.equals(Type.SHORT)) {
                t = Type.INT;
            }
            stack().push(t);
        }
    }

    @Override
    public void visitINVOKESTATIC(final INVOKESTATIC o) {
        for (int i = 0; i < o.getArgumentTypes(cpg).length; i++) {
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID) {
            Type t = o.getReturnType(cpg);
            if (t.equals(Type.BOOLEAN) ||
                    t.equals(Type.CHAR) ||
                    t.equals(Type.BYTE) ||
                    t.equals(Type.SHORT)) {
                t = Type.INT;
            }
            stack().push(t);
        }
    }

    @Override
    public void visitINVOKEVIRTUAL(final INVOKEVIRTUAL o) {
        stack().pop(); //objectref
        for (int i = 0; i < o.getArgumentTypes(cpg).length; i++) {
            stack().pop();
        }
        // We are sure the invoked method will xRETURN eventually
        // We simulate xRETURNs functionality here because we
        // don't really "jump into" and simulate the invoked
        // method.
        if (o.getReturnType(cpg) != Type.VOID) {
            Type t = o.getReturnType(cpg);
            if (t.equals(Type.BOOLEAN) ||
                    t.equals(Type.CHAR) ||
                    t.equals(Type.BYTE) ||
                    t.equals(Type.SHORT)) {
                t = Type.INT;
            }
            stack().push(t);
        }
    }

    @Override
    public void visitIOR(final IOR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitIREM(final IREM o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitIRETURN(final IRETURN o) {
        stack().pop();
    }

    @Override
    public void visitISHL(final ISHL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitISHR(final ISHR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitISTORE(final ISTORE o) {
        locals().set(o.getIndex(), stack().pop());
    }

    @Override
    public void visitISUB(final ISUB o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitIUSHR(final IUSHR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitIXOR(final IXOR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitJSR(final JSR o) {
        stack().push(new ReturnaddressType(o.physicalSuccessor()));
        //System.err.println("TODO-----------:"+o.physicalSuccessor());
    }

    @Override
    public void visitJSR_W(final JSR_W o) {
        stack().push(new ReturnaddressType(o.physicalSuccessor()));
    }

    @Override
    public void visitL2D(final L2D o) {
        stack().pop();
        stack().push(Type.DOUBLE);
    }

    @Override
    public void visitL2F(final L2F o) {
        stack().pop();
        stack().push(Type.FLOAT);
    }

    @Override
    public void visitL2I(final L2I o) {
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitLADD(final LADD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLALOAD(final LALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLAND(final LAND o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLASTORE(final LASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitLCMP(final LCMP o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitLCONST(final LCONST o) {
        stack().push(Type.LONG);
    }

    @Override
    public void visitLDC(final LDC o) {
        final Constant c = cpg.getConstant(o.getIndex());
        if (c instanceof ConstantInteger) {
            stack().push(Type.INT);
        }
        if (c instanceof ConstantFloat) {
            stack().push(Type.FLOAT);
        }
        if (c instanceof ConstantString) {
            stack().push(Type.STRING);
        }
        if (c instanceof ConstantClass) {
            stack().push(Type.CLASS);
        }
    }

    public void visitLDC_W(final LDC_W o) {
        final Constant c = cpg.getConstant(o.getIndex());
        if (c instanceof ConstantInteger) {
            stack().push(Type.INT);
        }
        if (c instanceof ConstantFloat) {
            stack().push(Type.FLOAT);
        }
        if (c instanceof ConstantString) {
            stack().push(Type.STRING);
        }
        if (c instanceof ConstantClass) {
            stack().push(Type.CLASS);
        }
    }

    @Override
    public void visitLDC2_W(final LDC2_W o) {
        final Constant c = cpg.getConstant(o.getIndex());
        if (c instanceof ConstantLong) {
            stack().push(Type.LONG);
        }
        if (c instanceof ConstantDouble) {
            stack().push(Type.DOUBLE);
        }
    }

    @Override
    public void visitLDIV(final LDIV o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLLOAD(final LLOAD o) {
        stack().push(locals().get(o.getIndex()));
    }

    @Override
    public void visitLMUL(final LMUL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLNEG(final LNEG o) {
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLOOKUPSWITCH(final LOOKUPSWITCH o) {
        stack().pop(); //key
    }

    @Override
    public void visitLOR(final LOR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLREM(final LREM o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLRETURN(final LRETURN o) {
        stack().pop();
    }

    @Override
    public void visitLSHL(final LSHL o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLSHR(final LSHR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLSTORE(final LSTORE o) {
        locals().set(o.getIndex(), stack().pop());
        locals().set(o.getIndex()+1, Type.UNKNOWN);
    }

    @Override
    public void visitLSUB(final LSUB o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLUSHR(final LUSHR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitLXOR(final LXOR o) {
        stack().pop();
        stack().pop();
        stack().push(Type.LONG);
    }

    @Override
    public void visitMONITORENTER(final MONITORENTER o) {
        stack().pop();
    }

    @Override
    public void visitMONITOREXIT(final MONITOREXIT o) {
        stack().pop();
    }

    @Override
    public void visitMULTIANEWARRAY(final MULTIANEWARRAY o) {
        for (int i=0; i<o.getDimensions(); i++) {
            stack().pop();
        }
        stack().push(o.getType(cpg));
    }

    @Override
    public void visitNEW(final NEW o) {
        stack().push(new UninitializedObjectType((ObjectType) (o.getType(cpg))));
    }

    @Override
    public void visitNEWARRAY(final NEWARRAY o) {
        stack().pop();
        stack().push(o.getType());
    }

    @Override
    public void visitNOP(final NOP o) {
    }

    @Override
    public void visitPOP(final POP o) {
        stack().pop();
    }

    @Override
    public void visitPOP2(final POP2 o) {
        final Type t = stack().pop();
        if (t.getSize() == 1) {
            stack().pop();
        }
    }

    @Override
    public void visitPUTFIELD(final PUTFIELD o) {
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitPUTSTATIC(final PUTSTATIC o) {
        stack().pop();
    }

    @Override
    public void visitRET(final RET o) {
        // do nothing, return address
        // is in in the local variables.
    }

    @Override
    public void visitRETURN(final RETURN o) {
        // do nothing.
    }

    @Override
    public void visitSALOAD(final SALOAD o) {
        stack().pop();
        stack().pop();
        stack().push(Type.INT);
    }

    @Override
    public void visitSASTORE(final SASTORE o) {
        stack().pop();
        stack().pop();
        stack().pop();
    }

    @Override
    public void visitSIPUSH(final SIPUSH o) {
        stack().push(Type.INT);
    }

    @Override
    public void visitSWAP(final SWAP o) {
        final Type t = stack().pop();
        final Type u = stack().pop();
        stack().push(t);
        stack().push(u);
    }

    @Override
    public void visitTABLESWITCH(final TABLESWITCH o) {
        stack().pop();
    }
}

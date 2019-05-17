package lsieun.bytecode.generic.instruction.visitor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.bytecode.classfile.cp.Constant;
import lsieun.bytecode.classfile.cp.ConstantClass;
import lsieun.bytecode.classfile.cp.ConstantDouble;
import lsieun.bytecode.classfile.cp.ConstantFloat;
import lsieun.bytecode.classfile.cp.ConstantInteger;
import lsieun.bytecode.classfile.cp.ConstantLong;
import lsieun.bytecode.classfile.cp.ConstantString;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.InstructionList;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;
import lsieun.bytecode.generic.instruction.sub.CPInstruction;
import lsieun.bytecode.generic.instruction.sub.LocalVariableInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.GotoInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.IfInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.SelectInstruction;
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
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.bytecode.utils.clazz.AccessFlagUtils;
import lsieun.bytecode.utils.clazz.AttributeUtils;
import lsieun.bytecode.verifier.structurals.ExecutionVisitor;
import lsieun.bytecode.verifier.structurals.Frame;
import lsieun.bytecode.verifier.structurals.UninitializedObjectType;
import lsieun.utils.radix.HexUtils;

@SuppressWarnings("Duplicates")
public class StackMapFrameVisitor extends EmptyVisitor {
    private static final String NO_ARG_FORMAT = "%04d: %-20s || %s\n";
    private static final String ONE_ARG_FORMAT = "%04d: %-15s %-4s || %s\n";
    private static final String NEWARRAY_FORMAT = "%04d: %-15s %-4s || %s || %s\n";
    private static final String IINC_FORMAT = "%04d: %-10s %-4s %-4s || %s\n";
    private static final String CP_INS_FORMAT = "%04d: %-15s #%-3s || %s // %s\n";
    private static final String WIDE_LOCAL_VAR_FORMAT = "%04d: wide %-10s %-4d || %s\n";

    //[local variable][stack]
    private MethodInfo methodInfo;
    private ConstantPool constantPool;
    private ConstantPoolGen cpg;

    //输入参数-->local variable，
    private boolean isStatic;
    private String descriptor;
    private Code code;
    // Code

    private int max_stack;
    private int max_locals;
    private byte[] code_bytes;
    private int offset;
    // StackMapTable
    private Type[] local_variable;
    private Type[] operand_stack;
    private int current_stack;


    public StackMapFrameVisitor() {

    }

    public void visit(MethodInfo methodInfo, ConstantPool constantPool) {
        //第一部分信息，由传入parameter获取
        this.methodInfo = methodInfo;
        this.constantPool = constantPool;
        this.cpg = new ConstantPoolGen(constantPool);

        //第二部分信息，由第一部分信息获得

        int accessFlags = methodInfo.getAccessFlags();
        this.isStatic = AccessFlagUtils.isStatic(accessFlags);
        int descriptorIndex = methodInfo.getDescriptorIndex();
        this.descriptor = constantPool.getConstantString(descriptorIndex, CPConst.CONSTANT_Utf8);
        this.code = AttributeUtils.findCodeAttribute(methodInfo);

        //第三部分信息，由第二部分信息中的code获得
        max_stack = code.getMaxStack();
        max_locals = code.getMaxLocals();
        code_bytes = code.getCode();

        //第四部分信息，由第三部分的max_stack和max_locals支撑
        operand_stack = new Type[max_stack];
        local_variable = new Type[max_locals];
        current_stack = -1;

        System.out.println("descriptor = " + descriptor);
        System.out.println("isStatic = " + isStatic);
        System.out.println("max_stack = " + max_stack);
        System.out.println("max_locals = " + max_locals);
        System.out.println();

        int idx = 0;
        if (!isStatic) {
            idx++;
            local_variable[0] = new ObjectType("this");
        }

        Type[] argumentTypes = Type.getArgumentTypes(descriptor);
        for (int i = 0; i < argumentTypes.length; i++) {
            Type t = argumentTypes[i];
            local_variable[idx + i] = t;
        }

        System.out.println("HexCode: " + HexUtils.fromBytes(code_bytes));
        InstructionList il = new InstructionList(code_bytes);
        int[] instructionPositions = il.getInstructionPositions();
        InstructionHandle ih = il.getStart();

        Map<Integer, State> map = new HashMap<Integer, State>();

        int count = 0;
        while (ih != null) {
            Instruction instruction = ih.getInstruction();
            offset = instructionPositions[count];

            // （1）恢复原来的Stack状态
            State storedState = map.get(Integer.valueOf(offset));
            if(storedState != null) {
                System.arraycopy(storedState.getOperandStack(), 0, operand_stack, 0, max_stack);
                System.arraycopy(storedState.getLocalVariable(), 0, local_variable, 0, max_locals);
            }

            // (2)处理当前的instruction
            instruction.accept(this);

            // （3）如果当前指令是跳转指令，那么需要存储当前指令处理后的状态，以便在第（1）处恢复
            if(instruction instanceof BranchInstruction) {
                BranchInstruction brIns = (BranchInstruction) instruction;
                int index = brIns.getIndex();
                State state = copyState();
                map.put(Integer.valueOf(offset+index), state);

                if(instruction instanceof SelectInstruction) {
                    SelectInstruction selectIns = (SelectInstruction) instruction;
                    int[] indices = selectIns.getIndices();
                    for(int i : indices) {
                        State s = copyState();
                        map.put(Integer.valueOf(offset+i), s);
                    }
                }
            }


            ih = ih.getNext();
            count++;
        }
    }

    private State copyState() {
        State state = new State(max_stack, max_locals);
        System.arraycopy(operand_stack, 0, state.getOperandStack(), 0, max_stack);
        System.arraycopy(local_variable, 0, state.getLocalVariable(), 0, max_locals);
        return state;
    }

    private static class State {
        private Type[] local_variable;
        private Type[] operand_stack;

        public State(int max_stack, int max_locals) {
            operand_stack = new Type[max_stack];
            local_variable = new Type[max_locals];
        }

        public Type[] getLocalVariable() {
            return local_variable;
        }

        public void setLocalVariable(Type[] local_variable) {
            this.local_variable = local_variable;
        }

        public Type[] getOperandStack() {
            return operand_stack;
        }

        public void setOperandStack(Type[] operand_stack) {
            this.operand_stack = operand_stack;
        }
    }

    public String getLine() {
        String format = "%s %s";
        Type[] array = new Type[current_stack+1];
        System.arraycopy(operand_stack, 0, array, 0, current_stack+1);

        String line = String.format(format, Arrays.toString(local_variable), Arrays.toString(array));
        return line;
    }

    @Override
    public void visitAALOAD(final AALOAD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        Type type = pop();
        ArrayType at = (ArrayType) type;
        push(at.getElementType());
    }

    @Override
    public void visitAASTORE(final AASTORE obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(3);
    }

    @Override
    public void visitACONST_NULL(final ACONST_NULL obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        push(Type.NULL);
    }

    @Override
    public void visitALOAD(final ALOAD obj) {
        // Opcode
        int localVarIndex = visitLocalVarIns(obj);

        // StackMapFrame
        Type type = local_variable[localVarIndex];
        push(type);
    }

    //FIXME: anewarray test
    @Override
    public void visitANEWARRAY(final ANEWARRAY obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        pop();
        Type type = obj.getType(cpg);
        ArrayType at = new ArrayType(type, 1);
        push(at);
    }

    @Override
    public void visitARETURN(final ARETURN obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        clearStack();
    }

    @Override
    public void visitARRAYLENGTH(final ARRAYLENGTH obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.INT);
    }

    @Override
    public void visitASTORE(final ASTORE obj) {
        // Opcode
        int localVarIndex = visitLocalVarIns(obj);

        // StackMapFrame
        Type type = pop();
        local_variable[localVarIndex] = type;
    }

    @Override
    public void visitATHROW(final ATHROW obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        Type type = pop();
        clearStack();
        push(type);
    }

    @Override
    public void visitBALOAD(final BALOAD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitBASTORE(final BASTORE obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(3);
    }

    @Override
    public void visitBIPUSH(final BIPUSH obj) {
        // Opcode
        Number value = obj.getValue();
        visitONEARGIns(obj, String.valueOf(value));

        // StackMapFrame
        push(Type.INT);
    }

    @Override
    public void visitCALOAD(final CALOAD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitCASTORE(final CASTORE obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(3);
    }

    @Override
    public void visitCHECKCAST(final CHECKCAST obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        pop();
        Type type = obj.getType(cpg);
        push(type);
    }

    @Override
    public void visitD2F(final D2F obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.FLOAT);
    }

    @Override
    public void visitD2I(final D2I obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.INT);
    }

    @Override
    public void visitD2L(final D2L obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.LONG);
    }

    @Override
    public void visitDADD(final DADD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.DOUBLE);
    }

    @Override
    public void visitDALOAD(final DALOAD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.DOUBLE);
    }

    @Override
    public void visitDASTORE(final DASTORE obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(3);
    }

    @Override
    public void visitDCMPG(final DCMPG obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitDCMPL(final DCMPL obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitDCONST(final DCONST obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        push(Type.DOUBLE);
    }

    @Override
    public void visitDDIV(final DDIV obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.DOUBLE);
    }

    @Override
    public void visitDLOAD(final DLOAD obj) {
        // Opcode
        int localVarIndex = visitLocalVarIns(obj);

        // StackMapFrame
        push(Type.DOUBLE);
    }

    @Override
    public void visitDMUL(final DMUL obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.DOUBLE);
    }

    @Override
    public void visitDNEG(final DNEG obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.DOUBLE);
    }

    @Override
    public void visitDREM(final DREM obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.DOUBLE);
    }

    @Override
    public void visitDRETURN(final DRETURN obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        clearStack();
    }

    @Override
    public void visitDSTORE(final DSTORE obj) {
        // Opcode
        int localVarIndex = visitLocalVarIns(obj);

        // StackMapFrame
        pop();
        local_variable[localVarIndex] = Type.DOUBLE;
        local_variable[localVarIndex + 1] = Type.UNKNOWN;
    }

    @Override
    public void visitDSUB(final DSUB obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.DOUBLE);
    }

    @Override
    public void visitDUP(final DUP obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        Type type = pop();
        push(type);
        push(type);
    }

    @Override
    public void visitDUP_X1(final DUP_X1 obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        Type t1 = pop();
        Type t2 = pop();
        push(t1);
        push(t2);
        push(t1);
    }

    @Override
    public void visitDUP_X2(final DUP_X2 obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        Type t1 = pop();
        Type t2 = pop();
        Type t3 = pop();
        push(t1);
        push(t3);
        push(t2);
        push(t1);
    }

    @Override
    public void visitDUP2(final DUP2 obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        Type t1 = pop();
        Type t2 = pop();
        push(t2);
        push(t1);
        push(t2);
        push(t1);
    }

    @Override
    public void visitDUP2_X1(final DUP2_X1 obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        Type t1 = pop();
        Type t2 = pop();
        Type t3 = pop();
        push(t2);
        push(t1);
        push(t3);
        push(t2);
        push(t1);
    }

    @Override
    public void visitDUP2_X2(final DUP2_X2 obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        Type t1 = pop();
        Type t2 = pop();
        Type t3 = pop();
        Type t4 = pop();
        push(t2);
        push(t1);
        push(t4);
        push(t3);
        push(t2);
        push(t1);
    }

    @Override
    public void visitF2D(final F2D obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.DOUBLE);
    }

    @Override
    public void visitF2I(final F2I obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.INT);
    }

    @Override
    public void visitF2L(final F2L obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.LONG);
    }

    @Override
    public void visitFADD(final FADD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.FLOAT);
    }

    @Override
    public void visitFALOAD(final FALOAD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.FLOAT);
    }

    @Override
    public void visitFASTORE(final FASTORE obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(3);
    }

    @Override
    public void visitFCMPG(final FCMPG obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitFCMPL(final FCMPL obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitFCONST(final FCONST obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        push(Type.FLOAT);
    }

    @Override
    public void visitFDIV(final FDIV obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.FLOAT);
    }

    @Override
    public void visitFLOAD(final FLOAD obj) {
        // Opcode
        int localVarIndex = visitLocalVarIns(obj);

        // StackMapFrame
        push(Type.FLOAT);
    }

    @Override
    public void visitFMUL(final FMUL obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.FLOAT);
    }

    @Override
    public void visitFNEG(final FNEG obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.FLOAT);
    }

    @Override
    public void visitFREM(final FREM obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.FLOAT);
    }

    @Override
    public void visitFRETURN(final FRETURN obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        clearStack();
    }

    @Override
    public void visitFSTORE(final FSTORE obj) {
        // Opcode
        int localVarIndex = visitLocalVarIns(obj);

        // StackMapFrame
        local_variable[localVarIndex] = Type.FLOAT;
    }

    @Override
    public void visitFSUB(final FSUB obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.FLOAT);
    }

    @Override
    public void visitGETFIELD(final GETFIELD obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        pop();
        Type type = obj.getFieldType(cpg);
        push(type);
    }

    @Override
    public void visitGETSTATIC(final GETSTATIC obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        Type type = obj.getType(cpg);
        push(type);
    }

    @Override
    public void visitGOTO(final GOTO obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // no stack changes.
    }

    @Override
    public void visitGOTO_W(final GOTO_W obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // no stack changes.
    }

    @Override
    public void visitI2B(final I2B obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.INT);
    }

    @Override
    public void visitI2C(final I2C obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.INT);
    }

    @Override
    public void visitI2D(final I2D obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.DOUBLE);
    }

    @Override
    public void visitI2F(final I2F obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.FLOAT);
    }

    @Override
    public void visitI2L(final I2L obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.LONG);
    }

    @Override
    public void visitI2S(final I2S obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.INT);
    }

    @Override
    public void visitIADD(final IADD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitIALOAD(final IALOAD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitIAND(final IAND obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitIASTORE(final IASTORE obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(3);
    }

    @Override
    public void visitICONST(final ICONST obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        push(Type.INT);
    }

    @Override
    public void visitIDIV(final IDIV obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitIF_ACMPEQ(final IF_ACMPEQ obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop(2);
    }

    @Override
    public void visitIF_ACMPNE(final IF_ACMPNE obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop(2);
    }

    @Override
    public void visitIF_ICMPEQ(final IF_ICMPEQ obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop(2);
    }

    @Override
    public void visitIF_ICMPGE(final IF_ICMPGE obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop(2);
    }

    @Override
    public void visitIF_ICMPGT(final IF_ICMPGT obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop(2);
    }

    @Override
    public void visitIF_ICMPLE(final IF_ICMPLE obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop(2);
    }

    @Override
    public void visitIF_ICMPLT(final IF_ICMPLT obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop(2);
    }

    @Override
    public void visitIF_ICMPNE(final IF_ICMPNE obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop(2);
    }

    @Override
    public void visitIFEQ(final IFEQ obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop();
    }

    @Override
    public void visitIFGE(final IFGE obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop();
    }

    @Override
    public void visitIFGT(final IFGT obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop();
    }

    @Override
    public void visitIFLE(final IFLE obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop();
    }

    @Override
    public void visitIFLT(final IFLT obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop();
    }

    @Override
    public void visitIFNE(final IFNE obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop();
    }

    @Override
    public void visitIFNONNULL(final IFNONNULL obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop();
    }

    @Override
    public void visitIFNULL(final IFNULL obj) {
        // Opcode
        int offset = obj.getIndex();
        visitONEARGIns(obj, String.valueOf(offset));

        // StackMapFrame
        pop();
    }

    @Override
    public void visitIINC(final IINC obj) {
        String name = obj.getName();
        int localVarIndex = obj.getIndex();

        int increment = obj.getIncrement();
        System.out.printf(IINC_FORMAT, offset, name, localVarIndex, increment, getLine());

        // stack is not changed.
    }

    @Override
    public void visitILOAD(final ILOAD obj) {
        // Opcode
        visitLocalVarIns(obj);

        // StackMapFrame
        push(Type.INT);
    }

    @Override
    public void visitIMUL(final IMUL obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitINEG(final INEG obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.INT);
    }

    @Override
    public void visitINSTANCEOF(final INSTANCEOF obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        pop();
        push(Type.INT);
    }

    @Override
    public void visitINVOKEDYNAMIC(final INVOKEDYNAMIC obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        int n = obj.getArgumentTypes(cpg).length;
        pop(n);

        Type returnType = obj.getReturnType(cpg);
        if(returnType != Type.VOID) {
            push(returnType);
        }
    }

    @Override
    public void visitINVOKEINTERFACE(final INVOKEINTERFACE obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        pop();
        int n = obj.getArgumentTypes(cpg).length;
        pop(n);

        Type returnType = obj.getReturnType(cpg);
        if(returnType != Type.VOID) {
            push(returnType);
        }
    }

    @Override
    public void visitINVOKESPECIAL(INVOKESPECIAL obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        pop();
        int n = obj.getArgumentTypes(cpg).length;
        pop(n);
        String methodName = obj.getMethodName(cpg);
        if("<init>".equals(methodName)) {
            Type type = operand_stack[current_stack];
            if(type instanceof UninitializedObjectType) {
                UninitializedObjectType t = (UninitializedObjectType) type;
                ObjectType objectType = t.getInitialized();
                operand_stack[current_stack] = objectType;
            }
        }

        Type returnType = obj.getReturnType(cpg);
        if(returnType != Type.VOID) {
            push(returnType);
        }
    }

    @Override
    public void visitINVOKESTATIC(INVOKESTATIC obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        int n = obj.getArgumentTypes(cpg).length;
        pop(n);

        Type returnType = obj.getReturnType(cpg);
        if(returnType != Type.VOID) {
            push(returnType);
        }
    }

    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        pop();
        int n = obj.getArgumentTypes(cpg).length;
        pop(n);

        Type returnType = obj.getReturnType(cpg);
        if(returnType != Type.VOID) {
            push(returnType);
        }
    }

    @Override
    public void visitIOR(final IOR obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitIREM(final IREM obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitIRETURN(final IRETURN obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        clearStack();
    }

    @Override
    public void visitISHL(final ISHL obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitISHR(final ISHR obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitISTORE(final ISTORE obj) {
        // Opcode
        int localVarIndex = visitLocalVarIns(obj);

        // StackMapFrame
        pop();
        local_variable[localVarIndex] = Type.INT;
    }

    @Override
    public void visitISUB(final ISUB obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitIUSHR(final IUSHR obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitIXOR(final IXOR obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitJSR(final JSR obj) {
        Type type = new ReturnaddressType(obj.physicalSuccessor());
        push(type);
    }

    @Override
    public void visitJSR_W(final JSR_W obj) {
        Type type = new ReturnaddressType(obj.physicalSuccessor());
        push(type);
    }

    @Override
    public void visitL2D(final L2D obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.DOUBLE);
    }

    @Override
    public void visitL2F(final L2F obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.FLOAT);
    }

    @Override
    public void visitL2I(final L2I obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.INT);
    }

    @Override
    public void visitLADD(final LADD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLALOAD(final LALOAD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLAND(final LAND obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLASTORE(final LASTORE obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(3);
    }

    @Override
    public void visitLCMP(final LCMP obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitLCONST(final LCONST obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        push(Type.LONG);
    }

    @Override
    public void visitLDC(final LDC obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        final Constant c = cpg.getConstant(obj.getIndex());
        if (c instanceof ConstantInteger) {
            push(Type.INT);
        }
        if (c instanceof ConstantFloat) {
            push(Type.FLOAT);
        }
        if (c instanceof ConstantString) {
            push(Type.STRING);
        }
        if (c instanceof ConstantClass) {
            push(Type.CLASS);
        }
    }

    public void visitLDC_W(final LDC_W obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        final Constant c = cpg.getConstant(obj.getIndex());
        if (c instanceof ConstantInteger) {
            push(Type.INT);
        }
        if (c instanceof ConstantFloat) {
            push(Type.FLOAT);
        }
        if (c instanceof ConstantString) {
            push(Type.STRING);
        }
        if (c instanceof ConstantClass) {
            push(Type.CLASS);
        }
    }

    @Override
    public void visitLDC2_W(final LDC2_W obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        final Constant c = cpg.getConstant(obj.getIndex());
        if (c instanceof ConstantLong) {
            push(Type.LONG);
        }
        if (c instanceof ConstantDouble) {
            push(Type.DOUBLE);
        }
    }

    @Override
    public void visitLDIV(final LDIV obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLLOAD(final LLOAD obj) {
        // Opcode
        visitLocalVarIns(obj);

        // StackMapFrame
        push(Type.LONG);
    }

    @Override
    public void visitLMUL(final LMUL obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLNEG(final LNEG obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
        push(Type.LONG);
    }

    @Override
    public void visitLOOKUPSWITCH(final LOOKUPSWITCH obj) {
        // Opcode
        visitSelectIns(obj);

        // StackMapFrame
        pop();
    }

    @Override
    public void visitLOR(final LOR obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLREM(final LREM obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLRETURN(final LRETURN obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        clearStack();
    }

    @Override
    public void visitLSHL(final LSHL obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLSHR(final LSHR obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLSTORE(final LSTORE obj) {
        // Opcode
        int localVarIndex = visitLocalVarIns(obj);

        // StackMapFrame
        local_variable[localVarIndex] = Type.LONG;
        local_variable[localVarIndex+1] = Type.UNKNOWN;
    }

    @Override
    public void visitLSUB(final LSUB obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLUSHR(final LUSHR obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitLXOR(final LXOR obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.LONG);
    }

    @Override
    public void visitMONITORENTER(final MONITORENTER obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
    }

    @Override
    public void visitMONITOREXIT(final MONITOREXIT obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
    }

    //FIXME: 测试多维数组
    @Override
    public void visitMULTIANEWARRAY(final MULTIANEWARRAY obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        for (int i=0; i<obj.getDimensions(); i++) {
            pop();
        }
        push(obj.getType(cpg));
    }

    @Override
    public void visitNEW(final NEW obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        push(new UninitializedObjectType((ObjectType) (obj.getType(cpg))));
    }

    @Override
    public void visitNEWARRAY(final NEWARRAY obj) {
        // Opcode
        String name = obj.getName();
        byte atype = obj.getTypecode();
        int length = obj.getLength();
        byte[] bytes = ByteDashboard.peekN(code_bytes, offset, length);
        Type type = obj.getType();
        System.out.printf(NEWARRAY_FORMAT, offset, name, atype, HexUtils.fromBytes(bytes), type);

        // StackMapFrame
        pop();
        push(obj.getType());
    }

    @Override
    public void visitNOP(final NOP obj) {
        // Opcode
        visitNOARGIns(obj);
    }

    @Override
    public void visitPOP(final POP obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop();
    }

    @Override
    public void visitPOP2(final POP2 obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        final Type t = pop();
        if (t.getSize() == 1) {
            pop();
        }
    }

    @Override
    public void visitPUTFIELD(final PUTFIELD obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        pop(2);
    }

    @Override
    public void visitPUTSTATIC(final PUTSTATIC obj) {
        // Opcode
        visitCPIns(obj);

        // StackMapFrame
        pop();
    }

    @Override
    public void visitRET(final RET o) {
        // do nothing, return address
        // is in in the local variables.
    }

    @Override
    public void visitRETURN(final RETURN obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        clearStack();
    }

    @Override
    public void visitSALOAD(final SALOAD obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(2);
        push(Type.INT);
    }

    @Override
    public void visitSASTORE(final SASTORE obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        pop(3);
    }

    @Override
    public void visitSIPUSH(final SIPUSH obj) {
        // Opcode
        Number value = obj.getValue();
        visitONEARGIns(obj, String.valueOf(value));

        // StackMapFrame
        push(Type.INT);
    }

    @Override
    public void visitSWAP(final SWAP obj) {
        // Opcode
        visitNOARGIns(obj);

        // StackMapFrame
        final Type t = pop();
        final Type u = pop();
        push(t);
        push(u);
    }

    @Override
    public void visitTABLESWITCH(final TABLESWITCH obj) {
        // Opcode
        visitSelectIns(obj);

        // StackMapFrame
        pop();
    }


    // region auxiliary
    private void visitNOARGIns(Instruction obj) {
        String name = obj.getName();
        System.out.printf(NO_ARG_FORMAT, offset, name, getLine());
    }

    private void visitONEARGIns(Instruction obj, String firstArg) {
        String name = obj.getName();
        System.out.printf(ONE_ARG_FORMAT, offset, name, firstArg, getLine());
    }

    private void visitWIDEARGIns(Instruction obj, String firstArg) {
        String name = obj.getName();
        System.out.printf(WIDE_LOCAL_VAR_FORMAT, offset, name, firstArg, getLine());
    }

    private void visitCPIns(CPInstruction obj) {
        String name = obj.getName();
        int cpIndex = obj.getIndex();
        Constant constant = constantPool.getConstant(cpIndex);
        String cpValue = constant.getValue();

        System.out.printf(CP_INS_FORMAT, offset, name, cpIndex, getLine(), cpValue);
    }

    private int visitLocalVarIns(LocalVariableInstruction obj) {
        int localVarIndex = obj.getIndex();
        boolean wide = obj.wide();

        if(localVarIndex>=0 && localVarIndex<=3) {
            visitNOARGIns(obj);
        }
        else {
            if(!wide) {
                visitONEARGIns(obj, String.valueOf(localVarIndex));
            }
            else {
                visitWIDEARGIns(obj, String.valueOf(localVarIndex));
            }
        }

        return localVarIndex;
    }

    private void visitSelectIns(SelectInstruction obj) {
        String name = obj.getName();
        int branch_offset = obj.getIndex();
        System.out.printf(ONE_ARG_FORMAT, offset, name, branch_offset, getLine());
        int match_length = obj.getMatch_length();
        int[] matchs = obj.getMatchs();
        int[] indices = obj.getIndices();
        String prefix = "      ";
        System.out.println(prefix + "{");
        for(int i=0; i<match_length; i++) {
            String format = "%9s: %s";
            String line = String.format(format, matchs[i], indices[i]);
            System.out.println(prefix + line);
        }
        System.out.println(prefix + "  default: " + branch_offset);
        System.out.println(prefix + "}");
    }
    // endregion


    // region stack operation
    public void push(Type type) {
        int size = type.getSize();
        for(int i=0; i<size; i++) {
            current_stack++;
            operand_stack[current_stack] = type;
        }
    }

    public Type pop() {
        Type type = operand_stack[current_stack];
        int size = type.getSize();
        for(int i=0; i<size; i++) {
            operand_stack[current_stack] = null;
            current_stack--;
        }
        return type;
    }

    public void pop(int n) {
        for(int i=0; i<n; i++) {
            pop();
        }
    }

    public void clearStack() {
        while (current_stack >= 0) {
            operand_stack[current_stack] = null;
            current_stack--;
        }
    }
    // endregion
}

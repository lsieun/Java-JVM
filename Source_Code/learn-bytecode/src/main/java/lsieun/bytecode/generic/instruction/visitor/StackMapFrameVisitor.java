package lsieun.bytecode.generic.instruction.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.bytecode.classfile.cp.Constant;
import lsieun.bytecode.generic.cst.CPConst;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.ConstantPoolGen;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.InstructionList;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.sub.ArithmeticInstruction;
import lsieun.bytecode.generic.instruction.sub.ArrayInstruction;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;
import lsieun.bytecode.generic.instruction.sub.CPInstruction;
import lsieun.bytecode.generic.instruction.sub.CompareInstruction;
import lsieun.bytecode.generic.instruction.sub.ConstantPushInstruction;
import lsieun.bytecode.generic.instruction.sub.ConversionInstruction;
import lsieun.bytecode.generic.instruction.sub.LocalVariableInstruction;
import lsieun.bytecode.generic.instruction.sub.ReturnInstruction;
import lsieun.bytecode.generic.instruction.sub.StackInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.SelectInstruction;
import lsieun.bytecode.generic.instruction.sub.cp.InvokeInstruction;
import lsieun.bytecode.generic.instruction.sub.load.LoadInstruction;
import lsieun.bytecode.generic.instruction.sub.load.StoreInstruction;
import lsieun.bytecode.generic.opcode.ARRAYLENGTH;
import lsieun.bytecode.generic.opcode.ATHROW;
import lsieun.bytecode.generic.opcode.BREAKPOINT;
import lsieun.bytecode.generic.opcode.IMPDEP1;
import lsieun.bytecode.generic.opcode.IMPDEP2;
import lsieun.bytecode.generic.opcode.MONITORENTER;
import lsieun.bytecode.generic.opcode.MONITOREXIT;
import lsieun.bytecode.generic.opcode.NOP;
import lsieun.bytecode.generic.opcode.allocate.NEW;
import lsieun.bytecode.generic.opcode.allocate.NEWARRAY;
import lsieun.bytecode.generic.opcode.branh.JSR;
import lsieun.bytecode.generic.opcode.branh.JSR_W;
import lsieun.bytecode.generic.opcode.branh.RET;
import lsieun.bytecode.generic.opcode.cst.ACONST_NULL;
import lsieun.bytecode.generic.opcode.cst.LDC;
import lsieun.bytecode.generic.opcode.invoke.INVOKESPECIAL;
import lsieun.bytecode.generic.opcode.locals.IINC;
import lsieun.bytecode.generic.opcode.stack.DUP;
import lsieun.bytecode.generic.type.Type;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.bytecode.utils.clazz.AccessFlagUtils;
import lsieun.bytecode.utils.clazz.AttributeUtils;
import lsieun.utils.radix.HexUtils;

public class StackMapFrameVisitor extends OpcodeVisitor {
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
        if(!isStatic) {
            idx++;
        }

        Type[] argumentTypes = Type.getArgumentTypes(descriptor);
        for(int i=0; i<argumentTypes.length; i++) {
            Type t = argumentTypes[i];
            local_variable[idx + i] = t;
        }
        System.out.println(getLine());

        System.out.println("HexCode: " + HexUtils.fromBytes(code_bytes));
        InstructionList il = new InstructionList(code_bytes);
        int[] instructionPositions = il.getInstructionPositions();
        InstructionHandle ih = il.getStart();

        int count = 0;
        while(ih != null) {
            Instruction instruction = ih.getInstruction();
            offset = instructionPositions[count];
            instruction.accept(this);
            ih = ih.getNext();
            count++;
        }
    }

    public String getLine() {
        String format = "%s %s";
        String line = String.format(format, Arrays.toString(local_variable), Arrays.toString(operand_stack));
        return line;
    }


    @Override
    public void visitNOP(NOP obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitLDC(LDC obj) {
        Type type = obj.getType(cpg);
        push(type);
    }

    @Override
    public void visitDUP(DUP obj) {
        Type type = obj.getType(cpg);
        push(type);
    }

    @Override
    public void visitARRAYLENGTH(ARRAYLENGTH obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitATHROW(ATHROW obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitMONITORENTER(MONITORENTER obj) {

    }

    @Override
    public void visitMONITOREXIT(MONITOREXIT obj) {

    }

    @Override
    public void visitJSR(JSR obj) {

    }

    @Override
    public void visitJSR_W(JSR_W obj) {

    }

    @Override
    public void visitRET(RET obj) {

    }

    @Override
    public void visitNEW(NEW obj) {
        Type type = obj.getType(cpg);
        push(type);
    }

    @Override
    public void visitNEWARRAY(NEWARRAY obj) {
        String name = obj.getName();
        byte atype = obj.getTypecode();
        int length = obj.getLength();
        byte[] bytes = ByteDashboard.peekN(code_bytes, offset, length);
        Type type = obj.getType();
        System.out.printf(NEWARRAY_FORMAT, offset, name, atype, HexUtils.fromBytes(bytes), type);
    }

    @Override
    public void visitBREAKPOINT(BREAKPOINT obj) {

    }

    @Override
    public void visitIMPDEP1(IMPDEP1 obj) {

    }

    @Override
    public void visitIMPDEP2(IMPDEP2 obj) {

    }

    // region basic Instruction

    @Override
    public void visitInstruction(Instruction obj) {
        String name = obj.getName();
        System.out.printf(NO_ARG_FORMAT, offset, name, getLine());
    }

    @Override
    public void visitConstantPushInstruction(ConstantPushInstruction obj) {
        String name = obj.getName();
        short opcode = obj.getOpcode();
        Number value = obj.getValue();

        if(opcode>= OpcodeConst.ICONST_M1 && opcode<=OpcodeConst.DCONST_1) {
            System.out.printf(NO_ARG_FORMAT, offset, name, getLine());
        }
        else {
            System.out.printf(ONE_ARG_FORMAT, offset, name, value, getLine());
        }

        Type type = obj.getType(null);
        push(type);
    }

    @Override
    public void visitLocalVariableInstruction(LocalVariableInstruction obj) {
        String name = obj.getName();
        short opcode = obj.getOpcode();
        int localVarIndex = obj.getIndex();
        boolean wide = obj.wide();

        if(opcode == OpcodeConst.IINC) {
            //IINC_FORMAT
            IINC iinc = (IINC) obj;
            int increment = iinc.getIncrement();
            System.out.printf(IINC_FORMAT, offset, name, localVarIndex, increment, getLine());
        }
        else if(localVarIndex>=0 && localVarIndex<=3) {
            System.out.printf(NO_ARG_FORMAT, offset, name, getLine());
        }
        else {
            if(!wide) {
                System.out.printf(ONE_ARG_FORMAT, offset, name, localVarIndex, getLine());
            }
            else {
                System.out.printf(WIDE_LOCAL_VAR_FORMAT, offset, name, localVarIndex, getLine());
            }
        }
    }

    @Override
    public void visitArrayInstruction(ArrayInstruction obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitStackInstruction(StackInstruction obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitArithmeticInstruction(ArithmeticInstruction obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitConversionInstruction(ConversionInstruction obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitCompareInstruction(CompareInstruction obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitBranchInstruction(BranchInstruction obj) {
        String name = obj.getName();
        int branch_offset = obj.getIndex();
        System.out.printf(ONE_ARG_FORMAT, offset, name, branch_offset, getLine());
        if(obj instanceof SelectInstruction) {
            SelectInstruction select = (SelectInstruction) obj;
            int match_length = select.getMatch_length();
            int[] matchs = select.getMatchs();
            int[] indices = select.getIndices();
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
        else {

        }

    }

    @Override
    public void visitReturnInstruction(ReturnInstruction obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitInvokeInstruction(InvokeInstruction obj) {
        int n = obj.consumeStack(cpg);
        while(n-->0) {
            pop();
        }
    }

    // endregion

    // region facet Instruction
    @Override
    public void visitCPInstruction(CPInstruction obj) {
        String name = obj.getName();
        int cpIndex = obj.getIndex();
        Constant constant = constantPool.getConstant(cpIndex);
        String cpValue = constant.getValue();

        System.out.printf(CP_INS_FORMAT, offset, name, cpIndex, getLine(), cpValue);
    }

    @Override
    public void visitStoreInstruction(StoreInstruction obj) {
        int n = obj.getIndex();
        store(n);
    }

    @Override
    public void visitLoadInstruction(LoadInstruction obj) {
        int n = obj.getIndex();
        load(n);
    }

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

    public void store(int n) {
        Type type = pop();
        int size = type.getSize();
        for(int i=0; i<size; i++) {
            local_variable[n + i] = type;
        }
    }

    public void load(int n) {
        Type type = local_variable[n];
        push(type);
    }

    // endregion
}

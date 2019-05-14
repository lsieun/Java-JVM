package lsieun.bytecode.generic.instruction.visitor;


import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.InstructionList;
import lsieun.bytecode.generic.instruction.handle.InstructionHandle;
import lsieun.bytecode.generic.instruction.sub.ArithmeticInstruction;
import lsieun.bytecode.generic.instruction.sub.ArrayInstruction;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;
import lsieun.bytecode.generic.instruction.sub.CPInstruction;
import lsieun.bytecode.generic.instruction.sub.ConstantPushInstruction;
import lsieun.bytecode.generic.instruction.sub.ConversionInstruction;
import lsieun.bytecode.generic.instruction.sub.LocalVariableInstruction;
import lsieun.bytecode.generic.instruction.sub.ReturnInstruction;
import lsieun.bytecode.generic.instruction.sub.StackInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.SelectInstruction;
import lsieun.bytecode.generic.opcode.ARRAYLENGTH;
import lsieun.bytecode.generic.opcode.ATHROW;
import lsieun.bytecode.generic.opcode.BREAKPOINT;
import lsieun.bytecode.generic.opcode.CHECKCAST;
import lsieun.bytecode.generic.opcode.IMPDEP1;
import lsieun.bytecode.generic.opcode.IMPDEP2;
import lsieun.bytecode.generic.opcode.INSTANCEOF;
import lsieun.bytecode.generic.opcode.MONITORENTER;
import lsieun.bytecode.generic.opcode.MONITOREXIT;
import lsieun.bytecode.generic.opcode.NOP;
import lsieun.bytecode.generic.opcode.allocate.ANEWARRAY;
import lsieun.bytecode.generic.opcode.allocate.MULTIANEWARRAY;
import lsieun.bytecode.generic.opcode.allocate.NEWARRAY;
import lsieun.bytecode.generic.opcode.branh.JSR;
import lsieun.bytecode.generic.opcode.branh.JSR_W;
import lsieun.bytecode.generic.opcode.branh.RET;
import lsieun.bytecode.generic.opcode.compare.DCMPG;
import lsieun.bytecode.generic.opcode.compare.DCMPL;
import lsieun.bytecode.generic.opcode.compare.FCMPG;
import lsieun.bytecode.generic.opcode.compare.FCMPL;
import lsieun.bytecode.generic.opcode.compare.LCMP;
import lsieun.bytecode.generic.opcode.cst.ACONST_NULL;
import lsieun.bytecode.generic.opcode.locals.IINC;
import lsieun.bytecode.generic.type.Type;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.HexUtils;

public class CodeStandardVisitor extends OpcodeVisitor {
    private static final String NO_ARG_FORMAT = "%04d: %-20s // %s\n";
    private static final String ONE_ARG_FORMAT = "%04d: %-15s %-4s // %s\n";
    private static final String NEWARRAY_FORMAT = "%04d: %-15s %-4s // %s || %s\n";
    private static final String IINC_FORMAT = "%04d: %-10s %-4s %-4s // %s\n";
    private static final String CP_INS_FORMAT = "%04d: %-15s #%-3s // %s || %s\n";
    private static final String WIDE_LOCAL_VAR_FORMAT = "%04d: wide %-10s %-4d // %s\n";
    private ConstantPool constantPool;
    private byte[] code_bytes;
    private int offset;

    public CodeStandardVisitor() {

    }

    public void visit(ConstantPool constantPool, byte[] code_bytes) {
        this.constantPool = constantPool;
        this.code_bytes = code_bytes;

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

    @Override
    public void visitNOP(NOP obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitLCMP(LCMP obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitFCMPL(FCMPL obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitFCMPG(FCMPG obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitDCMPL(DCMPL obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitDCMPG(DCMPG obj) {
        visitInstruction(obj);
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

    @Override
    public void visitInstruction(Instruction obj) {
        String name = obj.getName();
        int length = obj.getLength();
        byte[] bytes = ByteDashboard.peekN(code_bytes, offset, length);

        System.out.printf(NO_ARG_FORMAT, offset, name, HexUtils.fromBytes(bytes));
    }

    @Override
    public void visitConstantPushInstruction(ConstantPushInstruction obj) {
        String name = obj.getName();
        short opcode = obj.getOpcode();
        Number value = obj.getValue();
        int length = obj.getLength();
        byte[] bytes = ByteDashboard.peekN(code_bytes, offset, length);

        if(opcode>=OpcodeConst.ICONST_M1 && opcode<=OpcodeConst.DCONST_1) {
            System.out.printf(NO_ARG_FORMAT, offset, name, HexUtils.fromBytes(bytes));
        }
        else {
            System.out.printf(ONE_ARG_FORMAT, offset, name, value, HexUtils.fromBytes(bytes));
        }
    }

    @Override
    public void visitCPInstruction(CPInstruction obj) {
        String name = obj.getName();
        int cpIndex = obj.getIndex();
        String cpValue = this.constantPool.getConstant(cpIndex).getValue();

        int length = obj.getLength();
        byte[] bytes = ByteDashboard.peekN(code_bytes, offset, length);

        System.out.printf(CP_INS_FORMAT, offset, name, cpIndex, HexUtils.fromBytes(bytes), cpValue);

    }

    @Override
    public void visitLocalVariableInstruction(LocalVariableInstruction obj) {
        String name = obj.getName();
        short opcode = obj.getOpcode();
        int localVarIndex = obj.getIndex();
        boolean wide = obj.wide();
        int length = obj.getLength();
        byte[] bytes = ByteDashboard.peekN(code_bytes, offset, length);

        if(opcode == OpcodeConst.IINC) {
            //IINC_FORMAT
            IINC iinc = (IINC) obj;
            int increment = iinc.getIncrement();
            System.out.printf(IINC_FORMAT, offset, name, localVarIndex, increment, HexUtils.fromBytes(bytes));
        }
        else if(localVarIndex>=0 && localVarIndex<=3) {
            System.out.printf(NO_ARG_FORMAT, offset, name, HexUtils.fromBytes(bytes));
        }
        else {
            if(!wide) {
                System.out.printf(ONE_ARG_FORMAT, offset, name, localVarIndex, HexUtils.fromBytes(bytes));
            }
            else {
                System.out.printf(WIDE_LOCAL_VAR_FORMAT, offset, name, localVarIndex, HexUtils.fromBytes(bytes));
            }
        }
    }

    @Override
    public void visitArithmeticInstruction(ArithmeticInstruction obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitStackInstruction(StackInstruction obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitArrayInstruction(ArrayInstruction obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitBranchInstruction(BranchInstruction obj) {
        String name = obj.getName();
        int branch_offset = obj.getIndex();
        int length = obj.getLength();
        byte[] bytes = ByteDashboard.peekN(code_bytes, offset, length);
        System.out.printf(ONE_ARG_FORMAT, offset, name, branch_offset, HexUtils.fromBytes(bytes));
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
    public void visitConversionInstruction(ConversionInstruction obj) {
        visitInstruction(obj);
    }

    @Override
    public void visitReturnInstruction(ReturnInstruction obj) {
        visitInstruction(obj);
    }
}

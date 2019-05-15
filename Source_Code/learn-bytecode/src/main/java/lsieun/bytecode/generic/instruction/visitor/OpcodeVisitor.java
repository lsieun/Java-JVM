package lsieun.bytecode.generic.instruction.visitor;

import lsieun.bytecode.generic.instruction.Instruction;
import lsieun.bytecode.generic.instruction.sub.CompareInstruction;
import lsieun.bytecode.generic.instruction.sub.cp.FieldOrMethod;
import lsieun.bytecode.generic.LoadClass;
import lsieun.bytecode.generic.cst.OpcodeConst;
import lsieun.bytecode.generic.instruction.facet.AllocationInstruction;
import lsieun.bytecode.generic.instruction.sub.ArithmeticInstruction;
import lsieun.bytecode.generic.instruction.sub.ArrayInstruction;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;
import lsieun.bytecode.generic.instruction.sub.CPInstruction;
import lsieun.bytecode.generic.instruction.sub.ConstantPushInstruction;
import lsieun.bytecode.generic.instruction.sub.ConversionInstruction;
import lsieun.bytecode.generic.instruction.sub.cp.FieldInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.GotoInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.IfInstruction;
import lsieun.bytecode.generic.instruction.sub.cp.InvokeInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.JsrInstruction;
import lsieun.bytecode.generic.instruction.sub.load.LoadInstruction;
import lsieun.bytecode.generic.instruction.sub.LocalVariableInstruction;
import lsieun.bytecode.generic.instruction.facet.PopInstruction;
import lsieun.bytecode.generic.instruction.facet.PushInstruction;
import lsieun.bytecode.generic.instruction.sub.ReturnInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.SelectInstruction;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.sub.StackInstruction;
import lsieun.bytecode.generic.instruction.facet.StackProducer;
import lsieun.bytecode.generic.instruction.sub.load.StoreInstruction;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;
import lsieun.bytecode.generic.instruction.facet.UnconditionalBranch;
import lsieun.bytecode.generic.instruction.facet.VariableLengthInstruction;
import lsieun.bytecode.generic.instruction.Visitor;
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

public class OpcodeVisitor implements Visitor {

    @Override
    public void visitNOP(NOP obj) {

    }

    // region opcode const
    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {

    }

    @Override
    public void visitICONST(ICONST obj) {

    }

    @Override
    public void visitLCONST(LCONST obj) {

    }

    @Override
    public void visitFCONST(FCONST obj) {

    }

    @Override
    public void visitDCONST(DCONST obj) {

    }

    @Override
    public void visitBIPUSH(BIPUSH obj) {

    }

    @Override
    public void visitSIPUSH(SIPUSH obj) {

    }

    @Override
    public void visitLDC(LDC obj) {

    }

    @Override
    public void visitLDC2_W(LDC2_W obj) {

    }
    // endregion

    // region opcode local variable
    @Override
    public void visitILOAD(ILOAD obj) {

    }

    @Override
    public void visitLLOAD(LLOAD obj) {

    }

    @Override
    public void visitFLOAD(FLOAD obj) {

    }

    @Override
    public void visitDLOAD(DLOAD obj) {

    }

    @Override
    public void visitALOAD(ALOAD obj) {

    }

    @Override
    public void visitISTORE(ISTORE obj) {

    }

    @Override
    public void visitLSTORE(LSTORE obj) {

    }

    @Override
    public void visitFSTORE(FSTORE obj) {

    }

    @Override
    public void visitDSTORE(DSTORE obj) {

    }

    @Override
    public void visitASTORE(ASTORE obj) {

    }
    // endregion

    // region opcode array
    @Override
    public void visitIALOAD(IALOAD obj) {

    }

    @Override
    public void visitLALOAD(LALOAD obj) {

    }

    @Override
    public void visitFALOAD(FALOAD obj) {

    }

    @Override
    public void visitDALOAD(DALOAD obj) {

    }

    @Override
    public void visitAALOAD(AALOAD obj) {

    }

    @Override
    public void visitBALOAD(BALOAD obj) {

    }

    @Override
    public void visitCALOAD(CALOAD obj) {

    }

    @Override
    public void visitSALOAD(SALOAD obj) {

    }

    @Override
    public void visitIASTORE(IASTORE obj) {

    }

    @Override
    public void visitLASTORE(LASTORE obj) {

    }

    @Override
    public void visitFASTORE(FASTORE obj) {

    }

    @Override
    public void visitDASTORE(DASTORE obj) {

    }

    @Override
    public void visitAASTORE(AASTORE obj) {

    }

    @Override
    public void visitBASTORE(BASTORE obj) {

    }

    @Override
    public void visitCASTORE(CASTORE obj) {

    }

    @Override
    public void visitSASTORE(SASTORE obj) {

    }
    // endregion

    // region opcode stack
    @Override
    public void visitPOP(POP obj) {

    }

    @Override
    public void visitPOP2(POP2 obj) {

    }

    @Override
    public void visitDUP(DUP obj) {

    }

    @Override
    public void visitDUP_X1(DUP_X1 obj) {

    }

    @Override
    public void visitDUP_X2(DUP_X2 obj) {

    }

    @Override
    public void visitDUP2(DUP2 obj) {

    }

    @Override
    public void visitDUP2_X1(DUP2_X1 obj) {

    }

    @Override
    public void visitDUP2_X2(DUP2_X2 obj) {

    }

    @Override
    public void visitSWAP(SWAP obj) {

    }
    // endregion

    // region opcode arithmetic
    @Override
    public void visitIADD(IADD obj) {

    }

    @Override
    public void visitLADD(LADD obj) {

    }

    @Override
    public void visitFADD(FADD obj) {

    }

    @Override
    public void visitDADD(DADD obj) {

    }

    @Override
    public void visitISUB(ISUB obj) {

    }

    @Override
    public void visitLSUB(LSUB obj) {

    }

    @Override
    public void visitFSUB(FSUB obj) {

    }

    @Override
    public void visitDSUB(DSUB obj) {

    }

    @Override
    public void visitIMUL(IMUL obj) {

    }

    @Override
    public void visitLMUL(LMUL obj) {

    }

    @Override
    public void visitFMUL(FMUL obj) {

    }

    @Override
    public void visitDMUL(DMUL obj) {

    }

    @Override
    public void visitIDIV(IDIV obj) {

    }

    @Override
    public void visitLDIV(LDIV obj) {

    }

    @Override
    public void visitFDIV(FDIV obj) {

    }

    @Override
    public void visitDDIV(DDIV obj) {

    }

    @Override
    public void visitIREM(IREM obj) {

    }

    @Override
    public void visitLREM(LREM obj) {

    }

    @Override
    public void visitFREM(FREM obj) {

    }

    @Override
    public void visitDREM(DREM obj) {

    }

    @Override
    public void visitINEG(INEG obj) {

    }

    @Override
    public void visitLNEG(LNEG obj) {

    }

    @Override
    public void visitFNEG(FNEG obj) {

    }

    @Override
    public void visitDNEG(DNEG obj) {

    }

    @Override
    public void visitISHL(ISHL obj) {

    }

    @Override
    public void visitLSHL(LSHL obj) {

    }

    @Override
    public void visitISHR(ISHR obj) {

    }

    @Override
    public void visitLSHR(LSHR obj) {

    }

    @Override
    public void visitIUSHR(IUSHR obj) {

    }

    @Override
    public void visitLUSHR(LUSHR obj) {

    }

    @Override
    public void visitIAND(IAND obj) {

    }

    @Override
    public void visitLAND(LAND obj) {

    }

    @Override
    public void visitIOR(IOR obj) {

    }

    @Override
    public void visitLOR(LOR obj) {

    }

    @Override
    public void visitIXOR(IXOR obj) {

    }

    @Override
    public void visitLXOR(LXOR obj) {

    }

    @Override
    public void visitIINC(IINC obj) {

    }
    // endregion

    // region opcode conversion
    @Override
    public void visitI2L(I2L obj) {

    }

    @Override
    public void visitI2F(I2F obj) {

    }

    @Override
    public void visitI2D(I2D obj) {

    }

    @Override
    public void visitL2I(L2I obj) {

    }

    @Override
    public void visitL2F(L2F obj) {

    }

    @Override
    public void visitL2D(L2D obj) {

    }

    @Override
    public void visitF2I(F2I obj) {

    }

    @Override
    public void visitF2L(F2L obj) {

    }

    @Override
    public void visitF2D(F2D obj) {

    }

    @Override
    public void visitD2I(D2I obj) {

    }

    @Override
    public void visitD2L(D2L obj) {

    }

    @Override
    public void visitD2F(D2F obj) {

    }

    @Override
    public void visitI2B(I2B obj) {

    }

    @Override
    public void visitI2C(I2C obj) {

    }

    @Override
    public void visitI2S(I2S obj) {

    }
    // endregion

    // region opcode compare
    @Override
    public void visitLCMP(LCMP obj) {

    }

    @Override
    public void visitFCMPL(FCMPL obj) {

    }

    @Override
    public void visitFCMPG(FCMPG obj) {

    }

    @Override
    public void visitDCMPL(DCMPL obj) {

    }

    @Override
    public void visitDCMPG(DCMPG obj) {

    }
    // endregion

    // region opcode if
    @Override
    public void visitIFEQ(IFEQ obj) {

    }

    @Override
    public void visitIFNE(IFNE obj) {

    }

    @Override
    public void visitIFLT(IFLT obj) {

    }

    @Override
    public void visitIFGE(IFGE obj) {

    }

    @Override
    public void visitIFGT(IFGT obj) {

    }

    @Override
    public void visitIFLE(IFLE obj) {

    }

    @Override
    public void visitIF_ICMPEQ(IF_ICMPEQ obj) {

    }

    @Override
    public void visitIF_ICMPNE(IF_ICMPNE obj) {

    }

    @Override
    public void visitIF_ICMPLT(IF_ICMPLT obj) {

    }

    @Override
    public void visitIF_ICMPGE(IF_ICMPGE obj) {

    }

    @Override
    public void visitIF_ICMPGT(IF_ICMPGT obj) {

    }

    @Override
    public void visitIF_ICMPLE(IF_ICMPLE obj) {

    }

    @Override
    public void visitIF_ACMPEQ(IF_ACMPEQ obj) {

    }

    @Override
    public void visitIF_ACMPNE(IF_ACMPNE obj) {

    }

    @Override
    public void visitIFNULL(IFNULL obj) {

    }

    @Override
    public void visitIFNONNULL(IFNONNULL obj) {

    }
    // endregion

    // region opcode branch
    @Override
    public void visitGOTO(GOTO obj) {

    }

    @Override
    public void visitGOTO_W(GOTO_W obj) {

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
    public void visitTABLESWITCH(TABLESWITCH obj) {

    }

    @Override
    public void visitLOOKUPSWITCH(LOOKUPSWITCH obj) {

    }
    // endregion

    // region opcode return
    @Override
    public void visitIRETURN(IRETURN obj) {

    }

    @Override
    public void visitLRETURN(LRETURN obj) {

    }

    @Override
    public void visitFRETURN(FRETURN obj) {

    }

    @Override
    public void visitDRETURN(DRETURN obj) {

    }

    @Override
    public void visitARETURN(ARETURN obj) {

    }

    @Override
    public void visitRETURN(RETURN obj) {

    }
    // endregion

    // region opcode field
    @Override
    public void visitGETSTATIC(GETSTATIC obj) {

    }

    @Override
    public void visitPUTSTATIC(PUTSTATIC obj) {

    }

    @Override
    public void visitGETFIELD(GETFIELD obj) {

    }

    @Override
    public void visitPUTFIELD(PUTFIELD obj) {

    }
    // endregion

    // region opcode invoke
    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj) {

    }

    @Override
    public void visitINVOKESPECIAL(INVOKESPECIAL obj) {

    }

    @Override
    public void visitINVOKESTATIC(INVOKESTATIC obj) {

    }

    @Override
    public void visitINVOKEINTERFACE(INVOKEINTERFACE obj) {

    }

    @Override
    public void visitINVOKEDYNAMIC(INVOKEDYNAMIC obj) {

    }
    // endregion

    // region opcode allocate
    @Override
    public void visitNEW(NEW obj) {

    }

    @Override
    public void visitNEWARRAY(NEWARRAY obj) {

    }

    @Override
    public void visitANEWARRAY(ANEWARRAY obj) {

    }

    @Override
    public void visitARRAYLENGTH(ARRAYLENGTH obj) {

    }

    @Override
    public void visitMULTIANEWARRAY(MULTIANEWARRAY obj) {

    }
    // endregion

    // region opcode xxx
    @Override
    public void visitATHROW(ATHROW obj) {

    }

    @Override
    public void visitCHECKCAST(CHECKCAST obj) {

    }

    @Override
    public void visitINSTANCEOF(INSTANCEOF obj) {

    }

    @Override
    public void visitMONITORENTER(MONITORENTER obj) {

    }

    @Override
    public void visitMONITOREXIT(MONITOREXIT obj) {

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
    // endregion

    // region basic Instruction
    @Override
    public void visitInstruction(Instruction obj) {

    }

    @Override
    public void visitConstantPushInstruction(ConstantPushInstruction obj) {

    }

    @Override
    public void visitLocalVariableInstruction(LocalVariableInstruction obj) {

    }

    @Override
    public void visitLoadInstruction(LoadInstruction obj) {

    }

    @Override
    public void visitStoreInstruction(StoreInstruction obj) {

    }

    @Override
    public void visitArrayInstruction(ArrayInstruction obj) {

    }

    @Override
    public void visitStackInstruction(StackInstruction obj) {

    }

    @Override
    public void visitArithmeticInstruction(ArithmeticInstruction obj) {

    }

    @Override
    public void visitConversionInstruction(ConversionInstruction obj) {

    }

    @Override
    public void visitCompareInstruction(CompareInstruction obj) {

    }

    @Override
    public void visitBranchInstruction(BranchInstruction obj) {

    }

    @Override
    public void visitIfInstruction(IfInstruction obj) {

    }

    @Override
    public void visitGotoInstruction(GotoInstruction obj) {

    }

    @Override
    public void visitJsrInstruction(JsrInstruction obj) {

    }

    @Override
    public void visitSelectInstruction(SelectInstruction obj) {

    }

    @Override
    public void visitReturnInstruction(ReturnInstruction obj) {

    }

    @Override
    public void visitFieldOrMethod(FieldOrMethod obj) {

    }

    @Override
    public void visitFieldInstruction(FieldInstruction obj) {

    }

    @Override
    public void visitInvokeInstruction(InvokeInstruction obj) {

    }
    // endregion

    // region facet Instruction
    @Override
    public void visitTypedInstruction(TypedInstruction obj) {

    }

    @Override
    public void visitCPInstruction(CPInstruction obj) {

    }

    @Override
    public void visitPushInstruction(PushInstruction obj) {

    }

    @Override
    public void visitPopInstruction(PopInstruction obj) {

    }

    @Override
    public void visitStackProducer(StackProducer obj) {

    }

    @Override
    public void visitStackConsumer(StackConsumer obj) {

    }

    @Override
    public void visitAllocationInstruction(AllocationInstruction obj) {

    }

    @Override
    public void visitLoadClass(LoadClass obj) {

    }

    @Override
    public void visitVariableLengthInstruction(VariableLengthInstruction obj) {

    }

    @Override
    public void visitUnconditionalBranch(UnconditionalBranch obj) {

    }
    // endregion
}

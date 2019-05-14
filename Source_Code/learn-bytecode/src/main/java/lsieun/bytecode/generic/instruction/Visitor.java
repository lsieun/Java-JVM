package lsieun.bytecode.generic.instruction;

import lsieun.bytecode.generic.instruction.facet.AllocationInstruction;
import lsieun.bytecode.generic.instruction.facet.UnconditionalBranch;
import lsieun.bytecode.generic.instruction.facet.VariableLengthInstruction;
import lsieun.bytecode.generic.instruction.sub.cp.FieldInstruction;
import lsieun.bytecode.generic.instruction.sub.cp.FieldOrMethod;
import lsieun.bytecode.generic.LoadClass;
import lsieun.bytecode.generic.instruction.facet.PopInstruction;
import lsieun.bytecode.generic.instruction.facet.PushInstruction;
import lsieun.bytecode.generic.instruction.facet.StackConsumer;
import lsieun.bytecode.generic.instruction.facet.StackProducer;
import lsieun.bytecode.generic.instruction.facet.TypedInstruction;
import lsieun.bytecode.generic.instruction.sub.ArithmeticInstruction;
import lsieun.bytecode.generic.instruction.sub.ArrayInstruction;
import lsieun.bytecode.generic.instruction.sub.BranchInstruction;
import lsieun.bytecode.generic.instruction.sub.CPInstruction;
import lsieun.bytecode.generic.instruction.sub.ConstantPushInstruction;
import lsieun.bytecode.generic.instruction.sub.ConversionInstruction;
import lsieun.bytecode.generic.instruction.sub.StackInstruction;
import lsieun.bytecode.generic.instruction.sub.cp.InvokeInstruction;
import lsieun.bytecode.generic.instruction.sub.load.LoadInstruction;
import lsieun.bytecode.generic.instruction.sub.LocalVariableInstruction;
import lsieun.bytecode.generic.instruction.sub.ReturnInstruction;
import lsieun.bytecode.generic.instruction.sub.load.StoreInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.GotoInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.IfInstruction;
import lsieun.bytecode.generic.instruction.sub.branch.JsrInstruction;
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
import lsieun.bytecode.generic.opcode.array.BALOAD;
import lsieun.bytecode.generic.opcode.array.BASTORE;
import lsieun.bytecode.generic.opcode.array.CALOAD;
import lsieun.bytecode.generic.opcode.array.CASTORE;
import lsieun.bytecode.generic.opcode.array.DALOAD;
import lsieun.bytecode.generic.opcode.array.DASTORE;
import lsieun.bytecode.generic.opcode.cst.BIPUSH;
import lsieun.bytecode.generic.opcode.cst.DCONST;
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
import lsieun.bytecode.generic.opcode.array.FALOAD;
import lsieun.bytecode.generic.opcode.array.FASTORE;
import lsieun.bytecode.generic.opcode.cst.FCONST;
import lsieun.bytecode.generic.opcode.array.IALOAD;
import lsieun.bytecode.generic.opcode.array.IASTORE;
import lsieun.bytecode.generic.opcode.cst.ICONST;
import lsieun.bytecode.generic.opcode.array.LALOAD;
import lsieun.bytecode.generic.opcode.array.LASTORE;
import lsieun.bytecode.generic.opcode.cst.LCONST;
import lsieun.bytecode.generic.opcode.NOP;
import lsieun.bytecode.generic.opcode.stack.POP;
import lsieun.bytecode.generic.opcode.stack.POP2;
import lsieun.bytecode.generic.opcode.array.SALOAD;
import lsieun.bytecode.generic.opcode.array.SASTORE;
import lsieun.bytecode.generic.opcode.stack.SWAP;

/**
 * Interface implementing the Visitor pattern programming style.
 * I.e., a class that implements this interface can handle all types of
 * instructions with the properly typed methods just by calling the accept()
 * method.
 */
public interface Visitor {

    void visitNOP(NOP obj);

    // region opcode const
    void visitACONST_NULL(ACONST_NULL obj);

    void visitICONST(ICONST obj);

    void visitLCONST(LCONST obj);

    void visitFCONST(FCONST obj);

    void visitDCONST(DCONST obj);

    void visitBIPUSH(BIPUSH obj);

    void visitSIPUSH(SIPUSH obj);

    void visitLDC(LDC obj);

    void visitLDC2_W(LDC2_W obj);
    // endregion

    // region opcode array
    void visitIALOAD(IALOAD obj);

    void visitLALOAD(LALOAD obj);

    void visitFALOAD(FALOAD obj);

    void visitDALOAD(DALOAD obj);

    void visitAALOAD(AALOAD obj);

    void visitBALOAD(BALOAD obj);

    void visitCALOAD(CALOAD obj);

    void visitSALOAD(SALOAD obj);

    void visitIASTORE(IASTORE obj);

    void visitLASTORE(LASTORE obj);

    void visitFASTORE(FASTORE obj);

    void visitDASTORE(DASTORE obj);

    void visitAASTORE(AASTORE obj);

    void visitBASTORE(BASTORE obj);

    void visitCASTORE(CASTORE obj);

    void visitSASTORE(SASTORE obj);
    // endregion

    // region opcode stack
    void visitPOP(POP obj);

    void visitPOP2(POP2 obj);

    void visitDUP(DUP obj);

    void visitDUP_X1(DUP_X1 obj);

    void visitDUP_X2(DUP_X2 obj);

    void visitDUP2(DUP2 obj);

    void visitDUP2_X1(DUP2_X1 obj);

    void visitDUP2_X2(DUP2_X2 obj);

    void visitSWAP(SWAP obj);
    // endregion

    // region opcode arithmetic
    void visitIADD(IADD obj);

    void visitLADD(LADD obj);

    void visitFADD(FADD obj);

    void visitDADD(DADD obj);

    void visitISUB(ISUB obj);

    void visitLSUB(LSUB obj);

    void visitFSUB(FSUB obj);

    void visitDSUB(DSUB obj);

    void visitIMUL(IMUL obj);

    void visitLMUL(LMUL obj);

    void visitFMUL(FMUL obj);

    void visitDMUL(DMUL obj);

    void visitIDIV(IDIV obj);

    void visitLDIV(LDIV obj);

    void visitFDIV(FDIV obj);

    void visitDDIV(DDIV obj);

    void visitIREM(IREM obj);

    void visitLREM(LREM obj);

    void visitFREM(FREM obj);

    void visitDREM(DREM obj);

    void visitINEG(INEG obj);

    void visitLNEG(LNEG obj);

    void visitFNEG(FNEG obj);

    void visitDNEG(DNEG obj);

    void visitISHL(ISHL obj);

    void visitLSHL(LSHL obj);

    void visitISHR(ISHR obj);

    void visitLSHR(LSHR obj);

    void visitIUSHR(IUSHR obj);

    void visitLUSHR(LUSHR obj);

    void visitIAND(IAND obj);

    void visitLAND(LAND obj);

    void visitIOR(IOR obj);

    void visitLOR(LOR obj);

    void visitIXOR(IXOR obj);

    void visitLXOR(LXOR obj);
    // endregion

    // region opcode conversion
    void visitI2L(I2L obj);

    void visitI2F(I2F obj);

    void visitI2D(I2D obj);

    void visitL2I(L2I obj);

    void visitL2F(L2F obj);

    void visitL2D(L2D obj);

    void visitF2I(F2I obj);

    void visitF2L(F2L obj);

    void visitF2D(F2D obj);

    void visitD2I(D2I obj);

    void visitD2L(D2L obj);

    void visitD2F(D2F obj);

    void visitI2B(I2B obj);

    void visitI2C(I2C obj);

    void visitI2S(I2S obj);
    // endregion

    // region opcode compare
    void visitLCMP(LCMP obj);

    void visitFCMPL(FCMPL obj);

    void visitFCMPG(FCMPG obj);

    void visitDCMPL(DCMPL obj);

    void visitDCMPG(DCMPG obj);

    // endregion

    // region opcode return
    void visitIRETURN(IRETURN obj);

    void visitLRETURN(LRETURN obj);

    void visitFRETURN(FRETURN obj);

    void visitDRETURN(DRETURN obj);

    void visitARETURN(ARETURN obj);

    void visitRETURN(RETURN obj);
    // endregion

    // region opcode xxx
    void visitARRAYLENGTH(ARRAYLENGTH obj);

    void visitATHROW(ATHROW obj);

    void visitMONITORENTER(MONITORENTER obj);

    void visitMONITOREXIT(MONITOREXIT obj);
    // endregion

    // region opcode local variable
    void visitALOAD(ALOAD obj);

    void visitILOAD(ILOAD obj);

    void visitASTORE(ASTORE obj);

    void visitISTORE(ISTORE obj);

    void visitLLOAD(LLOAD obj);

    void visitFLOAD(FLOAD obj);

    void visitDLOAD(DLOAD obj);

    void visitLSTORE(LSTORE obj);

    void visitFSTORE(FSTORE obj);

    void visitDSTORE(DSTORE obj);

    void visitIINC(IINC obj);
    // endregion

    // region opcode branch
    void visitIFNE(IFNE obj);

    void visitIFEQ(IFEQ obj);

    void visitIFGE(IFGE obj);

    void visitIFLT(IFLT obj);

    void visitIFLE(IFLE obj);

    void visitIFGT(IFGT obj);

    void visitIF_ICMPNE(IF_ICMPNE obj);

    void visitIF_ICMPEQ(IF_ICMPEQ obj);

    void visitIF_ICMPGE(IF_ICMPGE obj);

    void visitIF_ICMPLT(IF_ICMPLT obj);

    void visitIF_ICMPLE(IF_ICMPLE obj);

    void visitIF_ICMPGT(IF_ICMPGT obj);

    void visitIF_ACMPNE(IF_ACMPNE obj);

    void visitIF_ACMPEQ(IF_ACMPEQ obj);

    void visitIFNONNULL(IFNONNULL obj);

    void visitIFNULL(IFNULL obj);

    void visitGOTO(GOTO obj);

    void visitGOTO_W(GOTO_W obj);

    void visitJSR(JSR obj);

    void visitJSR_W(JSR_W obj);

    void visitRET(RET obj);

    void visitSelect(SelectInstruction obj);

    void visitTABLESWITCH(TABLESWITCH obj);

    void visitLOOKUPSWITCH(LOOKUPSWITCH obj);
    // endregion

    // region opcode field
    void visitPUTSTATIC(PUTSTATIC obj);

    void visitGETSTATIC(GETSTATIC obj);

    void visitGETFIELD(GETFIELD obj);

    void visitPUTFIELD(PUTFIELD obj);
    // endregion

    // region opcode invoke
    void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj);

    void visitINVOKESPECIAL(INVOKESPECIAL obj);

    void visitINVOKESTATIC(INVOKESTATIC obj);

    void visitINVOKEINTERFACE(INVOKEINTERFACE obj);

    void visitINVOKEDYNAMIC(INVOKEDYNAMIC obj);
    // endregion

    // region opcode allocate
    void visitNEW(NEW obj);

    void visitNEWARRAY(NEWARRAY obj);

    void visitANEWARRAY(ANEWARRAY obj);

    void visitMULTIANEWARRAY(MULTIANEWARRAY obj);
    // endregion

    // region opcode xxx
    void visitCHECKCAST(CHECKCAST obj);

    void visitINSTANCEOF(INSTANCEOF obj);

    void visitBREAKPOINT(BREAKPOINT obj);

    void visitIMPDEP1(IMPDEP1 obj);

    void visitIMPDEP2(IMPDEP2 obj);
    // endregion

    // region opcode xxx

    // endregion


    // region Instruction
    void visitInstruction(Instruction obj);

    void visitTypedInstruction(TypedInstruction obj);

    void visitPushInstruction(PushInstruction obj);

    void visitStackProducer(StackProducer obj);

    void visitConstantPushInstruction(ConstantPushInstruction obj);

    void visitArrayInstruction(ArrayInstruction obj);

    void visitStackConsumer(StackConsumer obj);

    void visitPopInstruction(PopInstruction obj);

    void visitStackInstruction(StackInstruction obj);

    void visitArithmeticInstruction(ArithmeticInstruction obj);

    void visitConversionInstruction(ConversionInstruction obj);

    void visitReturnInstruction(ReturnInstruction obj);

    void visitUnconditionalBranch(UnconditionalBranch obj);

    void visitLocalVariableInstruction(LocalVariableInstruction obj);

    void visitLoadInstruction(LoadInstruction obj);

    void visitStoreInstruction(StoreInstruction obj);

    void visitCPInstruction(CPInstruction obj);

    void visitBranchInstruction(BranchInstruction obj);

    void visitIfInstruction(IfInstruction obj);

    void visitVariableLengthInstruction(VariableLengthInstruction obj);

    void visitGotoInstruction(GotoInstruction obj);

    void visitJsrInstruction(JsrInstruction obj);

    void visitLoadClass(LoadClass obj);

    void visitFieldOrMethod(FieldOrMethod obj);

    void visitFieldInstruction(FieldInstruction obj);

    void visitInvokeInstruction(InvokeInstruction obj);

    void visitAllocationInstruction(AllocationInstruction obj);
    // endregion

//    void visitExceptionThrower(ExceptionThrower obj);

}

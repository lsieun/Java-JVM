package lsieun.bytecode.generic.instruction;

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
import lsieun.bytecode.generic.opcode.cst.ACONST_NULL;
import lsieun.bytecode.generic.opcode.array.BALOAD;
import lsieun.bytecode.generic.opcode.array.BASTORE;
import lsieun.bytecode.generic.opcode.array.CALOAD;
import lsieun.bytecode.generic.opcode.array.CASTORE;
import lsieun.bytecode.generic.opcode.array.DALOAD;
import lsieun.bytecode.generic.opcode.array.DASTORE;
import lsieun.bytecode.generic.opcode.cst.DCONST;
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


    // region Instruction
    void visitTypedInstruction(TypedInstruction obj);

    void visitPushInstruction(PushInstruction obj);

    void visitStackProducer(StackProducer obj);

    void visitConstantPushInstruction(ConstantPushInstruction obj);

    void visitArrayInstruction(ArrayInstruction obj);

    void visitStackConsumer(StackConsumer obj);

    void visitPopInstruction(PopInstruction obj);

    void visitStackInstruction(StackInstruction obj);

    void visitArithmeticInstruction(ArithmeticInstruction obj);
    // endregion

//


//    void visitLocalVariableInstruction(LocalVariableInstruction obj);


//    void visitBranchInstruction(BranchInstruction obj);


//    void visitLoadClass(LoadClass obj);


//    void visitFieldInstruction(FieldInstruction obj);


//    void visitIfInstruction(IfInstruction obj);


//    void visitConversionInstruction(ConversionInstruction obj);


//


//    void visitStoreInstruction(StoreInstruction obj);


//    void visitSelect(Select obj);


//    void visitJsrInstruction(JsrInstruction obj);


//    void visitGotoInstruction(GotoInstruction obj);


//    void visitUnconditionalBranch(UnconditionalBranch obj);


//


//    void visitCPInstruction(CPInstruction obj);


//    void visitInvokeInstruction(InvokeInstruction obj);


//


//    void visitAllocationInstruction(AllocationInstruction obj);


//    void visitReturnInstruction(ReturnInstruction obj);


//    void visitFieldOrMethod(FieldOrMethod obj);


//


//    void visitExceptionThrower(ExceptionThrower obj);


//    void visitLoadInstruction(LoadInstruction obj);


//    void visitVariableLengthInstruction(VariableLengthInstruction obj);


//

//    void visitGETSTATIC(GETSTATIC obj);


//    void visitIF_ICMPLT(IF_ICMPLT obj);


//    void visitMONITOREXIT(MONITOREXIT obj);


//    void visitIFLT(IFLT obj);


//    void visitLSTORE(LSTORE obj);


//


//


//    void visitISTORE(ISTORE obj);


//    void visitCHECKCAST(CHECKCAST obj);
//
//
//    void visitFCMPG(FCMPG obj);
//
//
//    void visitI2F(I2F obj);
//
//
//    void visitATHROW(ATHROW obj);
//
//
//    void visitDCMPL(DCMPL obj);
//
//
//    void visitARRAYLENGTH(ARRAYLENGTH obj);
//
//
//
//
//
//    void visitINVOKESTATIC(INVOKESTATIC obj);
//
//
//
//
//
//
//
//
//    void visitIFGE(IFGE obj);
//
//
//
//
//
//
//
//
//    void visitI2D(I2D obj);
//
//
//
//
//
//    void visitINVOKESPECIAL(INVOKESPECIAL obj);
//
//
//
//
//
//    void visitPUTFIELD(PUTFIELD obj);
//
//
//    void visitILOAD(ILOAD obj);
//
//
//    void visitDLOAD(DLOAD obj);
//
//
//
//
//
//    void visitNEW(NEW obj);
//
//
//    void visitIFNULL(IFNULL obj);
//
//
//
//
//
//    void visitL2I(L2I obj);
//
//
//
//
//
//    void visitTABLESWITCH(TABLESWITCH obj);
//
//
//    void visitIINC(IINC obj);
//
//
//    void visitDRETURN(DRETURN obj);
//
//
//    void visitFSTORE(FSTORE obj);
//
//
//
//
//
//
//
//
//
//
//
//    void visitIF_ICMPGE(IF_ICMPGE obj);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    void visitLDC(LDC obj);
//
//
//    void visitBIPUSH(BIPUSH obj);
//
//
//    void visitDSTORE(DSTORE obj);
//
//
//    void visitF2L(F2L obj);
//
//
//
//
//
//    void visitLLOAD(LLOAD obj);
//
//
//    void visitJSR(JSR obj);
//
//
//
//
//
//
//
//
//    void visitALOAD(ALOAD obj);
//
//
//
//
//
//    void visitRETURN(RETURN obj);
//
//
//
//
//
//    void visitSIPUSH(SIPUSH obj);
//
//
//
//
//
//    void visitL2F(L2F obj);
//
//
//    void visitIF_ICMPGT(IF_ICMPGT obj);
//
//
//    void visitF2D(F2D obj);
//
//
//    void visitI2L(I2L obj);
//
//
//    void visitIF_ACMPNE(IF_ACMPNE obj);
//
//
//
//
//
//    void visitI2S(I2S obj);
//
//
//    void visitIFEQ(IFEQ obj);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    void visitNEWARRAY(NEWARRAY obj);
//
//
//    void visitINVOKEINTERFACE(INVOKEINTERFACE obj);
//
//
//
//
//
//    void visitLCMP(LCMP obj);
//
//
//    void visitJSR_W(JSR_W obj);
//
//
//    void visitMULTIANEWARRAY(MULTIANEWARRAY obj);
//
//
//
//
//
//
//
//
//    void visitIFNONNULL(IFNONNULL obj);
//
//
//
//
//
//    void visitIFNE(IFNE obj);
//
//
//    void visitIF_ICMPLE(IF_ICMPLE obj);
//
//
//    void visitLDC2_W(LDC2_W obj);
//
//
//    void visitGETFIELD(GETFIELD obj);
//
//
//
//
//
//
//
//
//
//
//
//    void visitINSTANCEOF(INSTANCEOF obj);
//
//
//    void visitIFLE(IFLE obj);
//
//
//
//
//
//    void visitLRETURN(LRETURN obj);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    void visitIF_ACMPEQ(IF_ACMPEQ obj);
//
//
//    void visitIMPDEP1(IMPDEP1 obj);
//
//
//    void visitMONITORENTER(MONITORENTER obj);
//
//
//
//
//
//    void visitDCMPG(DCMPG obj);
//
//
//    void visitD2L(D2L obj);
//
//
//    void visitIMPDEP2(IMPDEP2 obj);
//
//
//    void visitL2D(L2D obj);
//
//
//    void visitRET(RET obj);
//
//
//    void visitIFGT(IFGT obj);
//
//
//
//
//
//    void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj);
//
//
//    void visitINVOKEDYNAMIC(INVOKEDYNAMIC obj);
//
//
//
//
//
//    void visitIRETURN(IRETURN obj);
//
//
//    void visitIF_ICMPNE(IF_ICMPNE obj);
//
//
//    void visitFLOAD(FLOAD obj);
//
//
//
//
//
//    void visitPUTSTATIC(PUTSTATIC obj);
//
//
//
//
//
//    void visitD2I(D2I obj);
//
//
//    void visitIF_ICMPEQ(IF_ICMPEQ obj);
//
//
//
//
//
//    void visitARETURN(ARETURN obj);
//
//
//
//
//
//
//
//
//    void visitGOTO_W(GOTO_W obj);
//
//
//    void visitD2F(D2F obj);
//
//
//    void visitGOTO(GOTO obj);
//
//
//
//
//
//    void visitF2I(F2I obj);
//
//
//
//
//

//
//
//
//
//
//    void visitI2B(I2B obj);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    void visitLOOKUPSWITCH(LOOKUPSWITCH obj);
//
//
//
//
//
//    void visitFCMPL(FCMPL obj);
//
//
//    void visitI2C(I2C obj);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    void visitASTORE(ASTORE obj);
//
//
//    void visitANEWARRAY(ANEWARRAY obj);
//
//
//    void visitFRETURN(FRETURN obj);
//
//
//
//
//
//    void visitBREAKPOINT(BREAKPOINT obj);
}

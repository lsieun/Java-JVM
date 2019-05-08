package lsieun.bytecode.classfile.visitors;

import lsieun.bytecode.classfile.attrs.classfile.SourceFile;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.radix.HexUtils;

public class AttributeClassFileVisitor extends AttributeVisitor {
    // region ClassFile

    @Override
    public void visitSourceFile(SourceFile obj) {
        System.out.println("SourceFile");
        System.out.printf("HexCode='%s'\n", obj.getHexCode());
        byte[] bytes = obj.getBytes();
        ByteDashboard byteDashboard = new ByteDashboard("SourceFile", bytes);
        int attribute_length = processAttributeHeader(byteDashboard);
        byte[] sourcefile_index_bytes = byteDashboard.nextN(2);
        System.out.printf("sourcefile_index='0x%s'(%d)\n", HexUtils.fromBytes(sourcefile_index_bytes), obj.getSourcefileIndex());
    }

    // endregion
}

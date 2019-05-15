package lsieun.bytecode.classfile.visitors;

import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.ConstantPool;
import lsieun.bytecode.classfile.FieldInfo;
import lsieun.bytecode.classfile.Fields;
import lsieun.bytecode.classfile.attrs.field.ConstantValue;
import lsieun.bytecode.utils.clazz.AttributeUtils;
import lsieun.bytecode.utils.clazz.FieldUtils;

public class FieldStandardVisitor extends AbstractVisitor {
    private String nameAndType;

    public FieldStandardVisitor(String nameAndType) {
        this.nameAndType = nameAndType;
    }

    @Override
    public void visitClassFile(ClassFile obj) {
        ConstantPool constantPool = obj.getConstantPool();
        constantPool.accept(this);

        Fields fields = obj.getFields();
        FieldInfo fieldInfo = FieldUtils.findField(fields, nameAndType);
        if(fieldInfo != null) {
            fieldInfo.accept(this);
        }

        fields.accept(this);
    }

    @Override
    public void visitFields(Fields obj) {
        FieldInfo[] entries = obj.getEntries();
        if(entries != null && entries.length > 0) {
            System.out.println("\nAvailable Fields:");
            for(FieldInfo item : entries) {
                Attributes attributes = item.getAttributes();
                String attrNames = AttributeUtils.getAttributeNames(attributes);

                String line = String.format("    Field='%s', AccessFlags='%s', Attrs='%s'",
                        item.getValue(),
                        item.getAccessFlagsString(),
                        attrNames);
                System.out.println(line);
            }
        }
    }

    @Override
    public void visitFieldInfo(FieldInfo obj) {
        Attributes attributes = obj.getAttributes();
        String attrNames = AttributeUtils.getAttributeNames(attributes);

        String line = String.format("\nFieldInfo {\n    Name='%s'\n    AccessFlags='%s'\n    Attrs='%s'\n    HexCode='%s'\n}",
                obj.getValue(),
                obj.getAccessFlagsString(),
                attrNames,
                obj.getHexCode());
        System.out.println(line);

        attributes.accept(this);
    }

    @Override
    public void visitConstantValue(ConstantValue obj) {
        String line = String.format("\n%s {Value='%s', HexCode='%s'}", obj.getName(), obj.getValue(), obj.getHexCode());
        System.out.println(line);
    }
}

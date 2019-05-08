package lsieun.bytecode.classfile.visitors;

import lsieun.bytecode.classfile.attrs.field.ConstantValue;

public class AttributeFieldVisitor extends AttributeVisitor {
    // region Field

    @Override
    public void visitConstantValue(ConstantValue obj) {
        String pattern = "attribute_name_index(u2)-attribte_length(u4)-constantvalue_index(u2)";

        String formatOne = "%s\nPattern='%s'\nHexCode='%s'";
        String firstLine = String.format(formatOne,
                obj.getName(), pattern, obj.getHexCode());
        System.out.println(firstLine);

        String formatTwo = "    attribute_name_index='%d'\n    attribte_length='%d'\n    constantvalue_index='%d'";
        String secondLine = String.format(formatTwo,
                obj.getAttributeNameIndex(),
                obj.getAttributeLength(),
                obj.getConstantValueIndex());
        System.out.println(secondLine);
    }

    // endregion
}

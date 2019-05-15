package lsieun.bytecode.utils.clazz;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.utils.StringUtils;

public class AttributeUtils {
    public static AttributeInfo findAttribute(Attributes attributes, String attrName) {
        if(StringUtils.isBlank(attrName)) return null;

        AttributeInfo[] entries = attributes.getEntries();
        for(int i=0; i<entries.length; i++) {
            AttributeInfo item = entries[i];
            String name = item.getName();
            if(attrName.equals(name)) {
                return item;
            }
        }

        System.out.println("Attribute DOES NOT EXIST: " + attrName);
        return null;
    }

    public static Code findCodeAttribute(MethodInfo methodInfo) {
        Attributes attributes = methodInfo.getAttributes();
        AttributeInfo attr = findAttribute(attributes, "Code");
        if(attr == null) return null;

        Code code = (Code) attr;
        return code;
    }


    public static String getAttributeNames(Attributes attributes) {
        AttributeInfo[] entries = attributes.getEntries();

        List<String> attr_list = new ArrayList();
        for(int i=0; i<entries.length; i++) {
            AttributeInfo item = entries[i];
            String name = item.getName();
            attr_list.add(name);
        }

        String attrNames = StringUtils.list2str(attr_list, "[", "]", ", ");
        if(attrNames == null) {
            attrNames = "[]";
        }
        return attrNames;
    }
}

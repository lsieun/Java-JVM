package lsieun.bytecode.utils.clazz;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.AttributeInfo;
import lsieun.bytecode.classfile.Attributes;
import lsieun.bytecode.classfile.ClassFile;
import lsieun.bytecode.classfile.MethodInfo;
import lsieun.bytecode.classfile.Methods;
import lsieun.bytecode.classfile.attrs.method.Code;
import lsieun.utils.StringUtils;

public class MethodUtils {
    // 接收方法名、签名，来找到具体的方法
    public static MethodInfo findMethod(ClassFile classFile, String nameAndType) {
        Methods methods = classFile.getMethods();
        return findMethod(methods, nameAndType);
    }

    public static MethodInfo findMethod(Methods methods, String nameAndType) {
        if(StringUtils.isBlank(nameAndType)) return null;

        MethodInfo[] entries = methods.getEntries();
        for(int i = 0; i<entries.length; i++) {
            MethodInfo item = entries[i];
            String value = item.getValue();
            if(nameAndType.equals(value)) {
                return item;
            }
        }

        System.out.println("Method DOES NOT EXIST: " + nameAndType);
        return null;
    }

    public static void displayAvailableMethods(ClassFile classFile) {
        Methods methods = classFile.getMethods();
        displayAvailableMethods(methods);
    }

    public static void displayAvailableMethods(Methods methods) {
        MethodInfo[] entries = methods.getEntries();
        if(entries != null && entries.length > 0) {
            System.out.println("\nAvailable Methods:");
            for(MethodInfo item : entries) {
                Attributes attributes = item.getAttributes();
                String attrNames = AttributeUtils.getAttributeNames(attributes);

                String codeAttrs = "";
                AttributeInfo codeAttribute = AttributeUtils.findAttribute(attributes, "Code");
                if(codeAttribute != null) {
                    Code code = (Code) codeAttribute;
                    codeAttrs = AttributeUtils.getAttributeNames(code.getAttributes());
                }

                String format = "    Method='%s', AccessFlags='%s', Attrs='%s' CodeAttrs='%s'";
                String line = String.format(format, item.getValue(), item.getAccessFlagsString(), attrNames, codeAttrs);
                System.out.println(line);
            }
        }
    }


    public static String getMethodNames(Methods methods) {
        MethodInfo[] entries = methods.getEntries();
        List<String> list = new ArrayList();
        for(MethodInfo item : entries) {
            String value = item.getValue();
            list.add(value);
        }
        return StringUtils.list2str(list, ", ");
    }
}

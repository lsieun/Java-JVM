package lsieun.bytecode.utils.clazz;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.classfile.FieldInfo;
import lsieun.bytecode.classfile.Fields;
import lsieun.utils.StringUtils;

public class FieldUtils {
    public static FieldInfo findField(Fields fields, String nameAndType) {
        if(StringUtils.isBlank(nameAndType)) return null;

        FieldInfo[] entries = fields.getEntries();
        for(int i = 0; i<entries.length; i++) {
            FieldInfo item = entries[i];
            String value = item.getValue();
            if(nameAndType.equals(value)) {
                return item;
            }
        }
        return null;
    }

    public String getFieldNames(Fields fields) {
        FieldInfo[] entries = fields.getEntries();

        List<String> list = new ArrayList();
        for(FieldInfo item : entries) {
            String value = item.getValue();
            list.add(value);
        }
        return StringUtils.list2str(list, ", ");
    }
}

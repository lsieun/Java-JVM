package lsieun.bytecode.classfile;

import java.util.ArrayList;
import java.util.List;

import lsieun.bytecode.generic.cnst.CPConst;
import lsieun.bytecode.classfile.cp.Constant;
import lsieun.bytecode.classfile.cp.ConstantClass;
import lsieun.bytecode.classfile.cp.ConstantDynamic;
import lsieun.bytecode.classfile.cp.ConstantFieldref;
import lsieun.bytecode.classfile.cp.ConstantInterfaceMethodref;
import lsieun.bytecode.classfile.cp.ConstantInvokeDynamic;
import lsieun.bytecode.classfile.cp.ConstantMethodHandle;
import lsieun.bytecode.classfile.cp.ConstantMethodType;
import lsieun.bytecode.classfile.cp.ConstantMethodref;
import lsieun.bytecode.classfile.cp.ConstantModule;
import lsieun.bytecode.classfile.cp.ConstantNameAndType;
import lsieun.bytecode.classfile.cp.ConstantPackage;
import lsieun.bytecode.classfile.cp.ConstantString;
import lsieun.bytecode.exceptions.ClassFormatException;
import lsieun.bytecode.utils.ByteDashboard;
import lsieun.utils.StringUtils;

public class ConstantPool extends Node {
    private final int count;
    private final Constant[] entries;

    public ConstantPool(final ByteDashboard byteDashboard, int count) {
        this.count = count;
        this.entries = new Constant[count];

        for(int i=1; i<count; i++) {
            Constant item = Constant.readConstant(byteDashboard);
            item.setIndex(i);

            this.entries[i] = item;
            /* Quote from the JVM specification:
             * "All eight byte constants take up two spots in the constant pool.
             * If this is the n'th byte in the constant pool, then the next item
             * will be numbered n+2"
             *
             * Thus we have to increment the index counter.
             */
            byte tag = item.getTag();
            if ((tag == CPConst.CONSTANT_Double) || (tag == CPConst.CONSTANT_Long)) {
                i++;
            }
        }
    }

    public int getCount() {
        return count;
    }

    public Constant[] getEntries() {
        return entries;
    }

    public void merge() {
        List<Constant> list1 = new ArrayList();
        List<Constant> list2 = new ArrayList();
        List<Constant> list3 = new ArrayList();
        List<Constant> list4 = new ArrayList();

        for(int i = 0; i<entries.length; i++) {
            Constant item = entries[i];
            if(item == null) continue;
            byte tag = item.getTag();

            if(tag == CPConst.CONSTANT_Utf8 ||
                    tag == CPConst.CONSTANT_Integer ||
                    tag == CPConst.CONSTANT_Float ||
                    tag == CPConst.CONSTANT_Long ||
                    tag == CPConst.CONSTANT_Double
            ) {
                list1.add(item);
            }
            else if(tag == CPConst.CONSTANT_Class ||
                    tag == CPConst.CONSTANT_String ||
                    tag == CPConst.CONSTANT_NameAndType ||
                    tag == CPConst.CONSTANT_MethodType ||
                    tag == CPConst.CONSTANT_Module ||
                    tag == CPConst.CONSTANT_Package
            ) {
                list2.add(item);
            }
            else if(tag == CPConst.CONSTANT_Fieldref ||
                    tag == CPConst.CONSTANT_Methodref ||
                    tag == CPConst.CONSTANT_InterfaceMethodref ||
                    tag == CPConst.CONSTANT_Dynamic ||
                    tag == CPConst.CONSTANT_InvokeDynamic
            ) {
                list3.add(item);
            }
            else if(tag == CPConst.CONSTANT_MethodHandle) {
                list4.add(item);
            }
            else {
                continue;
            }

            processList2(list2);
            processList3(list3);
            processList4(list4);
        }
    }

    public void processList2(List<Constant> list) {
        for(int i=0; i<list.size(); i++) {
            Constant item = list.get(i);
            byte tag = item.getTag();

            if(tag == CPConst.CONSTANT_Class) {
                ConstantClass sub = (ConstantClass) item;
                int nameIndex = sub.getNameIndex();

                String value = getConstantString(nameIndex, CPConst.CONSTANT_Utf8);
                sub.setValue(value);
            }
            else if(tag == CPConst.CONSTANT_String) {
                ConstantString sub = (ConstantString) item;
                int stringIndex = sub.getStringIndex();

                String value = getConstantString(stringIndex, CPConst.CONSTANT_Utf8);
                sub.setValue(value);
            }
            else if(tag == CPConst.CONSTANT_NameAndType) {
                ConstantNameAndType sub = (ConstantNameAndType) item;
                int nameIndex = sub.getNameIndex();
                int descriptorIndex = sub.getDescriptorIndex();

                String name = getConstantString(nameIndex, CPConst.CONSTANT_Utf8);
                String descriptor = getConstantString(descriptorIndex, CPConst.CONSTANT_Utf8);
                String value = name + ":" + descriptor;
                sub.setValue(value);
            }
            else if(tag == CPConst.CONSTANT_MethodType) {
                ConstantMethodType sub = (ConstantMethodType) item;
                int descriptorIndex = sub.getDescriptorIndex();

                String value = getConstantString(descriptorIndex, CPConst.CONSTANT_Utf8);
                sub.setValue(value);
            }
            else if(tag == CPConst.CONSTANT_Module) {
                ConstantModule sub = (ConstantModule) item;
                int nameIndex = sub.getNameIndex();

                String value = getConstantString(nameIndex, CPConst.CONSTANT_Utf8);
                sub.setValue(value);
            }
            else if(tag == CPConst.CONSTANT_Package) {
                ConstantPackage sub = (ConstantPackage) item;
                int nameIndex = sub.getNameIndex();

                String value = getConstantString(nameIndex, CPConst.CONSTANT_Utf8);
                sub.setValue(value);
            }
            else {
                continue;
            }
        }
    }

    @SuppressWarnings("Duplicates")
    public void processList3(List<Constant> list) {
        for(int i=0; i<list.size(); i++) {
            Constant item = list.get(i);
            byte tag = item.getTag();

            if(tag == CPConst.CONSTANT_Fieldref) {
                ConstantFieldref sub = (ConstantFieldref) item;
                int classIndex = sub.getClassIndex();
                int nameAndTypeIndex = sub.getNameAndTypeIndex();

                String className = getConstantString(classIndex, CPConst.CONSTANT_Class);
                String nameAndType = getConstantString(nameAndTypeIndex, CPConst.CONSTANT_NameAndType);
                String value = className + "." + nameAndType;

                sub.setValue(value);
            }
            else if(tag == CPConst.CONSTANT_Methodref) {
                ConstantMethodref sub = (ConstantMethodref) item;
                int classIndex = sub.getClassIndex();
                int nameAndTypeIndex = sub.getNameAndTypeIndex();

                String className = getConstantString(classIndex, CPConst.CONSTANT_Class);
                String nameAndType = getConstantString(nameAndTypeIndex, CPConst.CONSTANT_NameAndType);
                String value = className + "." + nameAndType;

                sub.setValue(value);
            }
            else if(tag == CPConst.CONSTANT_InterfaceMethodref) {
                ConstantInterfaceMethodref sub = (ConstantInterfaceMethodref) item;
                int classIndex = sub.getClassIndex();
                int nameAndTypeIndex = sub.getNameAndTypeIndex();

                String className = getConstantString(classIndex, CPConst.CONSTANT_Class);
                String nameAndType = getConstantString(nameAndTypeIndex, CPConst.CONSTANT_NameAndType);
                String value = className + "." + nameAndType;

                sub.setValue(value);
            }
            else if(tag == CPConst.CONSTANT_Dynamic) {
                ConstantDynamic sub = (ConstantDynamic) item;
                int nameAndTypeIndex = sub.getNameAndTypeIndex();

                String value = getConstantString(nameAndTypeIndex, CPConst.CONSTANT_NameAndType);
                sub.setValue(value);
            }
            else if(tag == CPConst.CONSTANT_InvokeDynamic) {
                ConstantInvokeDynamic sub = (ConstantInvokeDynamic) item;
                int nameAndTypeIndex = sub.getNameAndTypeIndex();

                String value = getConstantString(nameAndTypeIndex, CPConst.CONSTANT_NameAndType);
                sub.setValue(value);
            }
            else {
                continue;
            }
        }
    }

    public void processList4(List<Constant> list) {
        for(int i=0; i<list.size(); i++) {
            Constant item = list.get(i);
            byte tag = item.getTag();

            if(tag == CPConst.CONSTANT_MethodHandle) {
                ConstantMethodHandle sub = (ConstantMethodHandle) item;
                int referenceIndex = sub.getReferenceIndex();

                Constant target = getConstant(referenceIndex);
                String value = target.getValue();

                sub.setValue(value);
            }
        }
    }


    public Constant getConstant(final int index) {
        if (index >= count || index < 0) {
            throw new ClassFormatException("Invalid constant pool reference: " + index
                    + ". Constant pool size is: " + this.count);
        }
        return entries[index];
    }

    public Constant getConstant(final int index, final byte tag) throws ClassFormatException {
        Constant c = getConstant(index);
        if (c == null) {
            throw new ClassFormatException("Constant pool at index " + index + " is null.");
        }
        if (c.getTag() != tag) {
            throw new ClassFormatException("Expected class `" + CPConst.getConstantName(tag)
                    + "' at index " + index + " and got " + c);
        }
        return c;
    }

    public String getConstantString(final int index, final byte tag) {
        Constant constant = getConstant(index, tag);
        return constant.getValue();
    }

    @Override
    public void accept(Visitor obj) {
        obj.visitConstantPool(this);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String toString() {
        List<String> list = new ArrayList();

        for(int i = 0; i<entries.length; i++) {
            Constant item = entries[i];
            list.add("    " + item + StringUtils.LF);
        }



        String content = StringUtils.list2str(list, "");

        StringBuilder buf = new StringBuilder();
        buf.append("ConstantPool {" + StringUtils.LF);
        buf.append(content);
        buf.append("}");
        return buf.toString();
    }
}

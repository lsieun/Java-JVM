# StackMapTable

<!-- TOC -->

- [1. Position](#1-position)
- [2. StackMapTable_attribute](#2-stackmaptableattribute)
  - [2.1. stack map frame](#21-stack-map-frame)
  - [2.2. 定性描述](#22-%E5%AE%9A%E6%80%A7%E6%8F%8F%E8%BF%B0)
  - [2.3. 定量表达](#23-%E5%AE%9A%E9%87%8F%E8%A1%A8%E8%BE%BE)
- [3. verification_type_info](#3-verificationtypeinfo)
  - [3.1. Top_variable_info](#31-topvariableinfo)
  - [3.2. Integer_variable_info](#32-integervariableinfo)
  - [3.3. Float_variable_info](#33-floatvariableinfo)
  - [3.4. Null_variable_info](#34-nullvariableinfo)
  - [3.5. UninitializedThis_variable_info](#35-uninitializedthisvariableinfo)
  - [3.6. Object_variable_info](#36-objectvariableinfo)
  - [3.7. Uninitialized_variable_info](#37-uninitializedvariableinfo)
  - [3.8. Long_variable_info](#38-longvariableinfo)
  - [3.9. Double_variable_info](#39-doublevariableinfo)
- [4. stack_map_frame](#4-stackmapframe)
  - [4.1. same_frame](#41-sameframe)
  - [4.2. same_locals_1_stack_item_frame](#42-samelocals1stackitemframe)
  - [4.3. reserved](#43-reserved)
  - [4.4. same_locals_1_stack_item_frame_extended](#44-samelocals1stackitemframeextended)
  - [4.5. chop_frame](#45-chopframe)
  - [4.6. same_frame_extended](#46-sameframeextended)
  - [4.7. append_frame](#47-appendframe)
  - [4.8. full_frame](#48-fullframe)
- [Examples](#examples)
  - [append_frame example](#appendframe-example)

<!-- /TOC -->

A `StackMapTable` attribute is used during **the process of verification** by type checking<sub>【注：讲它的作用】</sub>.

## 1. Position

The `StackMapTable` attribute is a variable-length attribute in the `attributes` table of a `Code` attribute<sub>【注：讲它的位置】</sub>.

> Position: Code_attribute-->attributes-->StackMapTable

```txt
Code_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 max_stack;
    u2 max_locals;
    u4 code_length;
    u1 code[code_length];
    u2 exception_table_length;
    {
        u2 start_pc;
        u2 end_pc;
        u2 handler_pc;
        u2 catch_type;
    } exception_table[exception_table_length];
    u2 attributes_count;
    attribute_info attributes[attributes_count];
}
```

There may be **at most one** `StackMapTable` attribute in the `attributes` table of a `Code` attribute<sub>【注：讲它的数量】</sub>.

> 数量对比：Code_attribute-->1:1-->StackMapTable

## 2. StackMapTable_attribute

The `StackMapTable` attribute has the following format:

```txt
StackMapTable_attribute {
    u2              attribute_name_index;
    u4              attribute_length;
    u2              number_of_entries;
    stack_map_frame entries[number_of_entries];
}
```

### 2.1. stack map frame

A **stack map frame** specifies (either explicitly or implicitly) **the bytecode offset** at which it applies, and **the verification types** of **local variables** and **operand stack entries** for that offset.

- stack map frame
  - the bytecode offset
  - the verification types
    - local variables
    - operand stack entries

### 2.2. 定性描述

**Each stack map frame** described in the `entries` table relies on **the previous frame** for some of its semantics<sub>【注：】一种“向前依赖”的关系</sub>. **The first stack map frame** of a method is implicit, and computed from **the method descriptor** by the type checker<sub>【注：讲述第一个stack map frame】</sub>. The `stack_map_frame` structure at `entries[0]` therefore describes **the second stack map frame** of the method<sub>【注：讲述第二个stack map frame】</sub>.

### 2.3. 定量表达

The **bytecode offset** at which a stack map frame applies is calculated by taking the value `offset_delta` specified in the frame (either explicitly or implicitly), and adding `offset_delta + 1` to the **bytecode offset of the previous frame**<sub>【注：常规情况、其计算方式】</sub>, unless the previous frame is **the initial frame** of the method<sub>【注：特殊情况】</sub>. In that case, **the bytecode offset** at which the stack map frame applies is the value `offset_delta` specified in the frame<sub>【注：特殊情况的计算方式】</sub>.

## 3. verification_type_info

We say that **an instruction in the bytecode** has a corresponding **stack map frame**<sub>【注：先说结果，再说条件，if后面有两个条件】</sub> if the instruction starts at offset `i` in the `code` array of a `Code` attribute<sub>【注：第一个条件，在offset为i的地方有一个instruction】</sub>, and the `Code` attribute has a `StackMapTable` attribute whose `entries` array contains a stack map frame that applies at bytecode offset `i`<sub>【注：第二个条件，在offset为i的地方有一个stack map frame】</sub>.

A **verification type** specifies **the type of either one or two locations**, where **a location** is either **a single local variable** or **a single operand stack entry**. A **verification type** is represented by a discriminated union, `verification_type_info` , that consists of **a one-byte tag**, indicating which item of the union is in use, followed by zero or more bytes, giving more information about the tag.

```txt
union verification_type_info {
    Top_variable_info;
    Integer_variable_info;
    Float_variable_info;
    Long_variable_info;
    Double_variable_info;
    Null_variable_info;
    UninitializedThis_variable_info;
    Object_variable_info;
    Uninitialized_variable_info;
}
```

A **verification type** that specifies one location in the **local variable array** or in the **operand stack** is represented by the following items of the `verification_type_info` union:

### 3.1. Top_variable_info

- The `Top_variable_info` item indicates that the **local variable** has the verification type `top`.

```txt
Top_variable_info {
    u1 tag = ITEM_Top; /* 0 */
}
```

### 3.2. Integer_variable_info

- The `Integer_variable_info` item indicates that the location has the verification type `int`.

```txt
Integer_variable_info {
    u1 tag = ITEM_Integer; /* 1 */
}
```

### 3.3. Float_variable_info

- The `Float_variable_info` item indicates that the location has the verification type `float`.

```txt
Float_variable_info {
    u1 tag = ITEM_Float; /* 2 */
}
```

### 3.4. Null_variable_info

- The `Null_variable_info` type indicates that the location has the verification type `null`.

```txt
Null_variable_info {
    u1 tag = ITEM_Null; /* 5 */
}
```

### 3.5. UninitializedThis_variable_info

- The `UninitializedThis_variable_info` item indicates that the location has the verification type `uninitializedThis`.

```txt
UninitializedThis_variable_info {
    u1 tag = ITEM_UninitializedThis; /* 6 */
}
```

### 3.6. Object_variable_info

- The `Object_variable_info` item indicates that the location has the verification type which is the class represented by the `CONSTANT_Class_info` structure found in the `constant_pool` table at the index given by `cpool_index`.

```txt
Object_variable_info {
    u1 tag = ITEM_Object; /* 7 */
    u2 cpool_index;
}
```

### 3.7. Uninitialized_variable_info

- The `Uninitialized_variable_info` item indicates that the location has the verification type `uninitialized(Offset)`. The `Offset` item indicates the offset<sub>【注：飞机起飞】</sub>, in the `code` array of the `Code` attribute that contains this `StackMapTable` attribute,<sub>【注：飞机降落】</sub> of the `new` instruction that created the object being stored in the location.

```txt
Uninitialized_variable_info {
    u1 tag = ITEM_Uninitialized; /* 8 */
    u2 offset;
}
```

A **verification type** that specifies **two locations** in the **local variable array** or in the **operand stack** is represented by the following items of the `verification_type_info` union:

### 3.8. Long_variable_info

- The `Long_variable_info` item indicates that **the first of two locations** has the verification type `long`.

```txt
Long_variable_info {
    u1 tag = ITEM_Long; /* 4 */
}
```

### 3.9. Double_variable_info

- The `Double_variable_info` item indicates that **the first of two locations** has the verification type `double`.

```txt
Double_variable_info {
    u1 tag = ITEM_Double; /* 3 */
}
```

## 4. stack_map_frame

A **stack map frame** is represented by a discriminated union, `stack_map_frame`, which consists of a **one-byte tag**, indicating which item of the union is in use, followed by zero or more bytes, giving more information about the tag.

```txt
union stack_map_frame {
    same_frame;
    same_locals_1_stack_item_frame;
    same_locals_1_stack_item_frame_extended;
    chop_frame;
    same_frame_extended;
    append_frame;
    full_frame;
}
```

The **tag** indicates the **frame type** of the stack map frame:

### 4.1. same_frame

- The frame type `same_frame` is represented by tags in the range `[0-63]`<sub>【注：取值范围】</sub>. This frame type indicates that **the frame** has exactly **the same local variables** as **the previous frame** and that **the operand stack is empty**. The `offset_delta` value for the frame is the value of the tag item, `frame_type`.

```txt
same_frame {
    u1 frame_type = SAME; /* 0-63 */
}
```

### 4.2. same_locals_1_stack_item_frame

- The frame type `same_locals_1_stack_item_frame` is represented by tags in the range `[64, 127]`. This frame type indicates that **the frame** has exactly **the same local variables** as **the previous frame** and that **the operand stack has one entry**. The `offset_delta` value for the frame is given by the formula `frame_type - 64`. The **verification type** of the one stack entry appears after the frame type.

```txt
same_locals_1_stack_item_frame {
    u1 frame_type = SAME_LOCALS_1_STACK_ITEM; /* 64-127 */
    verification_type_info stack[1];
}
```

### 4.3. reserved

- Tags in the range `[128-246]` are reserved for future use.

### 4.4. same_locals_1_stack_item_frame_extended

- The frame type `same_locals_1_stack_item_frame_extended` is represented by the tag `247`. This frame type indicates that **the frame** has exactly **the same local variables** as **the previous frame** and that **the operand stack has one entry**. The `offset_delta` value for the frame is given explicitly, unlike in the frame type `same_locals_1_stack_item_frame` . The verification type of the one stack entry appears after `offset_delta`.

```txt
same_locals_1_stack_item_frame_extended {
    u1 frame_type = SAME_LOCALS_1_STACK_ITEM_EXTENDED; /* 247 */
    u2 offset_delta;
    verification_type_info stack[1];
}
```

### 4.5. chop_frame

The frame type `chop_frame` is represented by tags in the range `[248-250]`. This frame type indicates that **the frame** has **the same local variables** as **the previous frame** except that the last `k` local variables are absent, and that **the operand stack is empty**. The value of `k` is given by the formula `251 - frame_type`. The `offset_delta` value for the frame is given explicitly.

```txt
chop_frame {
    u1 frame_type = CHOP; /* 248-250 */
    u2 offset_delta;
}
```

### 4.6. same_frame_extended

- The frame type `same_frame_extended` is represented by the tag `251`. This frame type indicates that **the frame** has exactly **the same local variables** as **the previous frame** and that **the operand stack is empty**. The `offset_delta` value for the frame is given explicitly, unlike in the frame type `same_frame`.

```txt
same_frame_extended {
    u1 frame_type = SAME_FRAME_EXTENDED; /* 251 */
    u2 offset_delta;
}
```

### 4.7. append_frame

- The frame type `append_frame` is represented by tags in the range `[252-254]`. This frame type indicates that **the frame** has **the same locals** as **the previous frame** except that `k` additional locals are defined, and that **the operand stack is empty**. The value of `k` is given by the formula `frame_type - 251`. The `offset_delta` value for the frame is given explicitly.

```txt
append_frame {
    u1 frame_type = APPEND; /* 252-254 */
    u2 offset_delta;
    verification_type_info locals[frame_type - 251];
}
```

### 4.8. full_frame

- The frame type `full_frame` is represented by the tag `255`. The `offset_delta` value for the frame is given explicitly.

```txt
full_frame {
    u1 frame_type = FULL_FRAME; /* 255 */
    u2 offset_delta;
    u2 number_of_locals;
    verification_type_info locals[number_of_locals];
    u2 number_of_stack_items;
    verification_type_info stack[number_of_stack_items];
}
```

## Examples

### append_frame example

```java
public class HelloWorld {
    public static void testSimple() {
        boolean flag = true;
        int a = 0;
        Object obj = null;
        if(flag) {
            a = 1;
        }
        else {
            a = 2;
        }
    }
}
```


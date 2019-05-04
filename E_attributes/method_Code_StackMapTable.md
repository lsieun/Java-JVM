# StackMapTable

The `StackMapTable` attribute is a variable-length attribute in the attributes table of a `Code` attribute<sub>【注：讲它的位置】</sub>. A `StackMapTable` attribute is used during the process of verification by type checking<sub>【注：讲它的作用】</sub>.

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

The `StackMapTable` attribute has the following format:

```txt
StackMapTable_attribute {
    u2              attribute_name_index;
    u4              attribute_length;
    u2              number_of_entries;
    stack_map_frame entries[number_of_entries];
}
```

# Java-JVM

:pig: JVM相关内容

## Reference

- [Java Language and Virtual Machine Specifications](https://docs.oracle.com/javase/specs/)
- [Java™ Platform, Standard Edition 8 API Specification](https://docs.oracle.com/javase/8/docs/api/)

## 编写文章

- 要问自己是否可以把文章可以写的更简单、明了吗？
- 要有整体的图景、画面、Picture
- 要写下自己的讲解思路
- 要有明显的过渡段落。例如：「过渡段落」
- git book
- 程序代码learn-bytecode本身，也是一个需要讲解的部分

## git commit

```bash
git commit -m "<message>"
```

`<message>`包括三个字段：type（必需）、scope（可选）和subject（必需）。

- （1）type。用于说明 commit 的类别，只允许使用下面7个标识。
  - feat：新功能（feature）
  - fix：修补bug
  - docs：文档（documentation）
  - style： 格式（不影响代码运行的变动）
  - refactor：重构（即不是新增功能，也不是修改bug的代码变动）
  - test：增加测试
  - chore：构建过程或辅助工具的变动

如果type为feat和fix，则该 commit 将肯定出现在 Change log 之中。其他情况（docs、chore、style、refactor、test）由你决定，要不要放入 Change log，建议是不要。

- （2）scope，用于说明 commit 影响的范围，比如数据层、控制层、视图层等等，视项目不同而不同。
- （3）subject，是 commit 目的的简短描述，不超过50个字符。以动词开头，使用第一人称现在时，比如change，而不是changed或changes。第一个字母小写。结尾不加句号（.）

- chore n.家务活；杂事；琐事 a tedious but necessary task.

示例：

```bash
# 类别为“琐事”，范围是“IDE开发工具”，描述是“清除不需要的文件”
git commit -m "chore(ide):remove unnecessary files"
```

## 标题

学习目标：

示例代码：

```java

```

输出结果：

```txt

```

## 注释

- <sub>【注：】</sub>
- <sub><font color="red">【注：】</font></sub>
- <sub><font color="blue">【注：】</font></sub>
- <sub><font color="green">【注：】</font></sub>

## Summing-up

知识整理

## JDK

- bcel: `rt.jar` FQCN=`com.sun.org.apache.bcel.internal.classfile`
- asm: `rt.jar` FQCN=`jdk.internal.org.objectweb.asm.ClassReader`

## TODO

- [ ] lsieun-bytecode github

```txt
Settings-->Editor-->TODO
```

GOOD_CODE

- prosaic

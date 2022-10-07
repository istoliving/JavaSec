
#### 0x1 引子
SerialVesionUID不一致导致反序列化漏洞利用失败也算是实战中比较常见的问题了，面试也会经常提及。


#### 0x2 代码分析
反序列化的调用栈
```
initNonProxy:595, ObjectStreamClass (java.io)
readNonProxyDesc:1829, ObjectInputStream (java.io)
readClassDesc:1713, ObjectInputStream (java.io)
readOrdinaryObject:1986, ObjectInputStream (java.io)
readObject0:1535, ObjectInputStream (java.io)
readObject:422, ObjectInputStream (java.io)
main:18, SerializableDemo2 (serialVersionUID)
```

- java.io.ObjectStreamClass#initNonProxy

![image](https://user-images.githubusercontent.com/55024146/155457995-8eb346c3-e88d-49cc-b7f0-45c9f2a6710b.png)

对serialVersionUID做了比较，如果发现不相等，则直接抛出异常。

- java.io.ObjectStreamClass#getSerialVersionUID

![image](https://user-images.githubusercontent.com/55024146/155458101-55c08f41-5312-4768-88f2-9df636ecf0b2.png)

在没有定义serialVersionUID的时候，会调用computeDefaultSUID 方法，生成一个默认的serialVersionUID。



#### 0x3 解决方案

- [使用自定义ClassLoader解决反序列化serialVesionUID不一致问题](https://gv7.me/articles/2020/deserialization-of-serialvesionuid-conflicts-using-a-custom-classloader/)

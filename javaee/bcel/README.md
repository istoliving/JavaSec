前置基础
---


相关利用
---

### BCEL 编码/解码工具 X-BCELCode.jar

- BCEL编码/解码，常用于构造反序列化漏洞payload的场景
```
# java version "1.8.0_201"
java -jar .\X-BCELCode.jar
```
![X-BCELCode-1](https://user-images.githubusercontent.com/55024146/159114851-0cad5313-adec-496e-8fdf-17be1ce7b198.png)

```
java -jar .\X-BCELCode.jar --encode .\src\Evil.class
java -jar .\X-BCELCode.jar --decode '$$BCEL$$$l$8b$I$A$A$A$'
```
![X-BCELCode-2](https://user-images.githubusercontent.com/55024146/159114854-d162a6a0-e599-42dc-8f14-4e300495bbbe.png)

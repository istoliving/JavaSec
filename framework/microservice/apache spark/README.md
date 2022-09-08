前置基础
---


相关漏洞
---

### Spark Shell命令注入漏洞
> 占坑

- [详见](https://t.zsxq.com/IQRZrRZ)


在 org.apache.hadoop.fs.FileUtill 类的 unTar 方法里，用的 bash shell 命令拼接，所以可以使用命令加载恶意文件名的 tar 文件达到rce的效果。

![image](https://user-images.githubusercontent.com/55024146/160282134-cf0ea581-9bd5-41fe-bb0a-888b90307e19.png)


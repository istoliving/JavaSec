


### Rce via Log4shell

根据公开的payload

![image-20211221141800963](vulnerability-research.assets/image-20211221141800963.png)

定位source

```
sodu -rn "aaa.aaa.aaa" ~/Desktop/apache-druid-0.21.1/
```

![image-20211221141728711](vulnerability-research.assets/image-20211221141728711.png)



![image-20211221141932644](vulnerability-research.assets/image-20211221141932644.png)

找到

- org.apache.druid.server.lookup.cache.LookupCoordinatorManager

IDEA全局

![image-20211221142053238](vulnerability-research.assets/image-20211221142053238.png)

最后成功定位到source

- org.apache.druid.server.lookup.cache.LookupCoordinatorManager#deleteTier

![image-20211216180541227](vulnerability-research.assets/image-20211216180541227.png)


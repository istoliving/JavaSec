相关漏洞
---

### CVE-2021-44228 Log4Shell

根据流传的payload搭建测试环境

- log4j_rce.java

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class log4j_rce {
    private static final Logger logger = LogManager.getLogger(log4j_rce.class);
    public static void main(String[] args) {
        logger.error("暂时打码处理"}");
    }
}
```

一步一步跟进，最后跟到
- org.apache.logging.log4j.core.lookup.Interpolator#lookup

![image](https://user-images.githubusercontent.com/55024146/165895592-5e910b2b-1e49-4cc6-92cf-faddfb24e975.png)


似乎就是这里，断点调试

![image](https://user-images.githubusercontent.com/55024146/165895618-b81e3fcc-4bc8-4407-a927-9781318bc188.png)

答案呼之欲出，lookup + jndi！

其实这里可以触发的不只是error,默认情况下fatal也可以，即便实际的业务场景肯定会有所不同(只会更多)。

### 漏洞复现

起一个恶意的LDAPRefServer、恶意类Evil

![image](https://user-images.githubusercontent.com/55024146/165895658-d4eea680-015a-411f-96c7-2384fa08d462.png)

然后触发即可

![image](https://user-images.githubusercontent.com/55024146/165895677-ab26a7bb-69a3-4504-9156-7aade555ea07.png)

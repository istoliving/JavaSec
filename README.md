> Focus on Java Security since November 1, 2021 👣


**Java基础 / 漏洞类型**
- Java SE 
    - [x] [rt.jar](https://github.com/pen4uin/JavaSec/blob/main/basic-knowledge/javase/rt.jar/rt.md)
- Java Vulnerability
    - [ ] 反序列化 Deserialization
    - [ ] 代码执行
    - [ ] 命令注入
    - [ ] 表达式注入
    - [ ] 文件操作
    - [ ] DoS
    - [ ] SQL 注入
    - [ ] SSRF
    - [ ] XXE
    - [ ] ZipSlip

**中间件 / Servlet容器**

- JBoss
- Jetty
    - [x] [内存马](https://github.com/pen4uin/JavaSec/blob/main/jetty/fileless-shell.md)  
- Resin
    - [x] [内存马](https://github.com/pen4uin/JavaSec/blob/main/resin/fileless-shell.md)
    - [x] [漏洞研究](https://github.com/pen4uin/JavaSec/blob/main/resin/vulnerability-research.md)
- Spring 
    - [x] 内存马
- Tomcat 
    - [x] [内存马](https://github.com/pen4uin/JavaSec/blob/main/tomcat/fileless-shell.md)
- Weblogic
    - [x] [内存马](https://github.com/pen4uin/JavaSec/blob/main/weblogic/fileless-shell.md)
    - [ ] [漏洞研究](https://github.com/pen4uin/JavaSec/blob/main/weblogic/vulnerability-research.md)
        - [x] 获取Weblogic console用户名&密码(免解密)
- WebSphere

**组件 / 库 / 类库**
- Fastjson
- [Groovy](https://github.com/pen4uin/JavaSec/blob/main/groovy/source-analysis.md)
- [Log4j2](https://github.com/pen4uin/JavaSec/blob/main/log4j2/vulnerability-research.md)
    - [x] CVE-2021-44228
- XMLDecoder
- XStream
- SnakeYAML
- Jackson

**CMS / 框架 / Others**
- [Apache Druid](https://github.com/pen4uin/JavaSec/blob/main/apache%20druid/vulnerability-research.md)
    - [x] Rce via Log4shell CVE-2021-44228
- [Apache Shiro](https://github.com/pen4uin/JavaSec/blob/main/shiro/vulnerability-research.md)
    - [x] Shiro Key的修改（仅供娱乐）
- [Apache Struts2](https://github.com/pen4uin/JavaSec/blob/main/struts2/vulnerability-research.md)
    - [x] Rce via Log4shell CVE-2021-44228
- Hessian
- Hibernate
- [Inxedu 因酷网校在线教育系统](https://github.com/pen4uin/JavaSec/blob/main/inxedu/2021_08_05_Inxedu.pdf)
- [OFCMS](https://github.com/pen4uin/JavaSec/blob/main/ofcms/vulnerability-research.md)
- [Spring Messaging](https://github.com/pen4uin/JavaSec/blob/main/spring%20messaging/vulnerability-research.md)

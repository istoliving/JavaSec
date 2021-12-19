> Focus on Java Security since November 1, 2021 👣


**Java基础 / 常见漏洞**
- Java SE 
    - [x] [rt.jar](https://github.com/pen4uin/JavaSec/blob/main/basic-knowledge/jar_rt.md)
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

**中间件 / 框架**
- Hessian
- Hibernate
- JBoss
- Jetty
    - [x] [内存马(filter)](https://github.com/pen4uin/JavaSec/tree/main/fileless-shell/jetty)  
- MyBatis
- Resin
    - [x] [内存马(filter/servlet)](https://github.com/pen4uin/JavaSec/blob/main/fileless-shell/resin/)
    - [x] [漏洞分析](https://github.com/pen4uin/JavaSec/tree/main/vulnerability-analysis/resin)
- Shiro
    - [x] 漏洞利用 修改key 
- Struts2
    - [x] [Apache Struts2 RCE via Log4j2 CVE-2021-44228](https://github.com/pen4uin/JavaSec/tree/main/vulnerability-analysis/struts2)
- Spring 
    - [x] 内存马 (controller/interceptor)
- Tomcat 
    - [x] [内存马 (filter/servlet/listener/valve)](https://github.com/pen4uin/JavaSec/tree/main/fileless-shell/tomcat)
- Weblogic
    - [x] [内存马 (filter/listener/servlet)](https://github.com/pen4uin/JavaSec/blob/main/fileless-shell/weblogic/)
- WebSphere

**组件 / 库 / 类库**
- Fastjson
- Groovy
    - [x] [deserialization gadget chain](https://github.com/pen4uin/JavaSec/blob/main/post-exploitation/deserialization/jar_groovy.md)
- Log4j2
    - [x] [CVE-2021-44228](https://github.com/pen4uin/JavaSec/tree/main/vulnerability-analysis/log4j2)
- XMLDecoder
- XStream
- SnakeYAML
- Jackson

**CMS / Other**
- OFCMS
    - [x] [漏洞分析](https://github.com/pen4uin/JavaSec/tree/main/code-audit/01_ofcms)


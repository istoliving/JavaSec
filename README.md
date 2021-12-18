> Focus on Java Security since November 1, 2021 👣



## TODO 
— 任务驱动式学习路线
- 实现xxx中间件的内存马
- 分析xxx应用的历史漏洞
- 阅读xxx工具的源码
- 开发xxx功能的工具
### 中间件 / 框架
- [ ] Hessian
- [ ] Hibernate
- [ ] JBoss
- [ ] Jetty
    - [x] [内存马(filter)](https://github.com/pen4uin/JavaSec/tree/main/fileless-shell/jetty)  
- [ ] MyBatis
- [x] Resin
    - [x] [内存马(filter/servlet)](https://github.com/pen4uin/JavaSec/blob/main/fileless-shell/resin/)
    - [x] [漏洞分析](https://github.com/pen4uin/JavaSec/tree/main/vulnerability-analysis/resin)
- [ ] Shiro
    - [x] 漏洞利用 修改key 
- [ ] Struts2
    - [x] [Apache Struts2 RCE via Log4j2 CVE-2021-44228](https://github.com/pen4uin/JavaSec/tree/main/vulnerability-analysis/struts2)
- [x] Spring 
    - [x] 内存马 (controller/interceptor)
    - [ ] 漏洞分析
- [ ] Tomcat 
    - [x] [内存马 (filter/servlet/listener/valve)](https://github.com/pen4uin/JavaSec/tree/main/fileless-shell/tomcat)
- [x] Weblogic
    - [x] [内存马 (filter/listener/servlet)](https://github.com/pen4uin/JavaSec/blob/main/fileless-shell/weblogic/)
    - [ ] 漏洞分析
- [ ] WebSphere

### 组件 / 库 / 类库
- [ ] Fastjson
- [x] [Groovy](https://github.com/pen4uin/JavaSec/blob/main/post-exploitation/deserialization/jar_groovy.md)
- [x] [Log4j2](https://github.com/pen4uin/JavaSec/tree/main/vulnerability-analysis/log4j2)
- [ ] XMLDecoder
- [ ] XStream
- [ ] SnakeYAML
- [ ] Jackson

## 代码审计
```
希望能坚持更新到至少10+案例 👨‍💻
```
- [01 OFCMS](https://github.com/pen4uin/JavaSec/tree/main/code-audit/01_ofcms)


## Java基础
- [rt.jar](https://github.com/pen4uin/JavaSec/blob/main/basic-knowledge/jar_rt.md)

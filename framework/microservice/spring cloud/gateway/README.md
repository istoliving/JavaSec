相关漏洞
---


### CVE-2022-22947 SpEL Injection -> RCE

参考
- [Spring cloud gateway通过SPEL注入内存马](https://gv7.me/articles/2022/the-spring-cloud-gateway-inject-memshell-through-spel-expressions/)
- [表达式注入 -> RCE](https://github.com/nbxiglk0/Note/blob/master/%E4%BB%A3%E7%A0%81%E5%AE%A1%E8%AE%A1/Java/Spring%20Cloud%20GateWay/CVE-2022-22947/CVE-2022-22947.md) 
- https://github.com/vulhub/vulhub/blob/master/spring/CVE-2022-22947/README.zh-cn.md


环境搭建
```
git clone https://github.com/spring-cloud/spring-cloud-gateway
cd spring-cloud-gateway
git checkout v3.1.0
```
创建供codeql使用的数据库
```
codeql database create ..\databases\spring-cloud-gateway-310  --language="java" --command="mvn clean install --file pom.xml -Dmaven.test.skip=true"
```


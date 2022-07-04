前置基础
---


相关漏洞
---
### CVE-2021-33037 HTTP Request Smuggling

- https://xz.aliyun.com/t/9866

### CVE-2020-9484  Session Deserialization -> RCE

- https://mp.weixin.qq.com/s/r8Mk1TYJqFIxDk8SkWorrg

### CVE-2020-13935 WebSocket DoS

- https://xz.aliyun.com/t/8550


### CVE-2020-1938  AJP  File Read/Inclusion -> RCE

- https://www.anquanke.com/post/id/199448
- https://xz.aliyun.com/t/7325

### CVE-2019-0232  CGI Servlet RCE

- https://paper.seebug.org/958/

### CVE-2019-0221 XSS

- https://www.exploit-db.com/exploits/50119


### CVE-2018-11784 Open Redirect

- https://www.exploit-db.com/exploits/50118

### CVE-2017-12617 HTTP PUT -> RCE(12615 bypass)

- https://www.exploit-db.com/exploits/43008

### CVE-2017-12615 HTTP PUT -> RCE

- https://xz.aliyun.com/t/5610

利用研究
---

### Text Interface + WAR -> Post-RCE


**前言**

> Tomcat的一种鸡肋利用，需要获取`manager-script roles`角色用户的凭证。

  翻tomcat文档考古 -> `Deploy A New Application Archive (WAR) Remotely`

- https://tomcat.apache.org/tomcat-7.0-doc/manager-howto.html#Deploy_a_Directory_or_WAR_by_URL

![image](https://user-images.githubusercontent.com/55024146/177111478-ef51df0a-0a65-412b-8f75-85fbfce82d55.png)

重点：This command is executed by an HTTP `PUT` request.  也许可能大概可以用来绕安全设备??? 


**复现步骤**

0）测试环境
- apache-tomcat-8.5.55
- conf/tomcat-user.xml
  ```xml
  <?xml version='1.0' encoding='utf-8'?>
  <tomcat-users>
    <role rolename="manager-script"/>
    <user username="tomcat" password="tomcat" roles="manager-script"/>
  </tomcat-users>
  ```

1）制作WAR包
```
jar -cvf demo.war *
```

2）上传并部署WAR包
```
curl -u "tomcat:tomcat" -X PUT -T "demo.war" "http://10.10.10.1:8080/manager/text/deploy?path=/demo" --proxy "127.0.0.1:9090"
```

3）测试效果

![image](https://user-images.githubusercontent.com/55024146/177114675-61d9ecb3-c279-4b77-9d1d-26c083272c19.png)


PS: 测完发现@indishell1046在18年就在提出了这种[姿势](https://twitter.com/indishell1046/status/978704150014844928)，security "re-searcher"再一次在自己这儿实锤🤦‍♂️. 


### URL解析差异

### 回显

### 内存马

### 中间件持久化后门

- https://gv7.me/articles/2021/an-idea-of-keeping-persistent-backdoor-in-tomcat-middleware/
- https://xz.aliyun.com/t/10582
- https://xz.aliyun.com/t/10577

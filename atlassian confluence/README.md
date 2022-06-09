相关漏洞
---

### CVE-2022-26134 OGNL -> RCE
- https://www.rapid7.com/blog/post/2022/06/02/active-exploitation-of-confluence-cve-2022-26134/
- https://github.com/jbaines-r7/through_the_wire



#### Exploit Development


##### 1、无损检测
- https://twitter.com/httpvoid0x2f/status/1532924263556169728

代码实现

![image](https://user-images.githubusercontent.com/55024146/172790218-28dab3b3-8fc1-4140-9c1e-44301df4c54f.png)

测试效果

![image](https://user-images.githubusercontent.com/55024146/172812431-49d69ee3-0a05-4159-8add-0030175fd1a2.png)

##### 2、命令执行-回显
- https://twitter.com/phithon_xg/status/1533381232590958592

代码实现

![image](https://user-images.githubusercontent.com/55024146/172790507-2d546bef-04b2-46f2-8977-fa6f914821cc.png)

测试效果

![image](https://user-images.githubusercontent.com/55024146/172812526-352c93e0-66e8-43f6-92b8-c89501cec478.png)


##### 3、添加用户
- https://twitter.com/httpvoid0x2f/status/1532924261035384832

代码实现

![image](https://user-images.githubusercontent.com/55024146/172813077-3c929c96-2a99-45b0-aa6a-d287184bf988.png)


测试效果

![image](https://user-images.githubusercontent.com/55024146/172813574-182fcf36-6268-4b72-980a-f2a89142bacf.png)


##### 4、内存马植入

- https://github.com/BeichenDream/CVE-2022-26134-Godzilla-MEMSHELL

(遇坑，待处理)



### CVE-2021-26084 OGNL -> RCE
- https://github.com/httpvoid/writeups/blob/main/Confluence-RCE.md
- https://mp.weixin.qq.com/s/XDxIhLLWaoXaODaKW2mkrg

### CVE-2019-3396 Velocity SSTi=I -> RCE/File Read
- https://paper.seebug.org/884/

### CVE-2020-4027 SSTI -> RCE
- [Confluence模板注入（CVE-2020-4027）复现](https://blog.play2win.top/2021/10/20/Confluence%E6%A8%A1%E6%9D%BF%E6%B3%A8%E5%85%A5%EF%BC%88CVE-2020-4027%EF%BC%89%E5%A4%8D%E7%8E%B0/)

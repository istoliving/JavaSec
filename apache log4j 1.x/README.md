相关漏洞
---


### CVE-2019-17571 SocketServer RCE
**漏洞分析**

SimpleSocketServer若开启了xxxx端口，会对socket接收的数据进行反序列化操作

![image](https://user-images.githubusercontent.com/55024146/165814421-f6762915-25bc-4f59-b7d3-86d606e84373.png)

测试效果

![image](https://user-images.githubusercontent.com/55024146/165814599-c09f968b-c9aa-4719-ab2a-12ed283049c6.png)

**漏洞验证**

![image](https://user-images.githubusercontent.com/55024146/165814894-ebe9a146-8d59-4912-a059-00acb21f570e.png)



### CVE-2022-23307 Chainsaw RCE

**漏洞分析**

设置监听端口

![image](https://user-images.githubusercontent.com/55024146/165812050-49b13eb6-d3b7-47b3-9ca3-906c19891226.png)

然后使用 LoggingReceiver 处理相关连接信息，反序列化ois对象时触发漏洞

![image](https://user-images.githubusercontent.com/55024146/165812417-bc91774f-4916-42e6-89e0-bf1e6959870c.png)


**漏洞验证**

![image](https://user-images.githubusercontent.com/55024146/165812930-73c69adf-7dab-4556-b894-cc64fe056818.png)

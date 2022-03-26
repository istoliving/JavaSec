前置基础
---
**简介**

SpringCloudFunction是SpringBoot开发的一个Servless中间件（FAAS），支持基于SpEL的函数式动态路由。

相关漏洞
---

### Spring Cloud Function v3.x SpEL RCE

详细分析见
- [Spring Cloud Function v3.x SpEL RCE](https://mp.weixin.qq.com/s/U7YJ3FttuWSOgCodVSqemg)

**第1种利用：需要修改配置+任意路由**

![%BHK HK{RHH0E0~1}WTWGYO](https://user-images.githubusercontent.com/55024146/160249394-794b2933-46ac-40d9-ba93-1be98207b462.png)


**第2种利用：默认配置+特定路由**

![R)2817XQE$4O7428EPMC0II](https://user-images.githubusercontent.com/55024146/160249416-645ee796-279a-4112-9ee4-5f75f0fdf9b3.png)

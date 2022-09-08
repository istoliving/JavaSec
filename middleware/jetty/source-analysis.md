测试版本：9.4.43.v20210629

# 架构分析

均来自以下参考，纯当个人看一遍代码混个眼熟

- https://mp.weixin.qq.com/s/OQ24UmRHjoQObs_gpjJ7Ww
- https://xz.aliyun.com/t/9247
- https://blog.csdn.net/acm_lkl/article/details/78837539 Jetty服务器启动过程分析
- https://www.ph0ly.com/



## 配置文件

### web.xml

`分析一下Jetty 加载web.xml的流程`

- org.eclipse.jetty.webapp.WebXmlConfiguration#findWebXml

![image-20211217001324250](source-analysis.assets/image-20211217001324250.png)

断点，获取调用栈，再逐一分析

![image-20211217002035189](source-analysis.assets/image-20211217002035189.png)

> 调用context.getWebInf()

跟进

- org.eclipse.jetty.webapp.WebAppContext#getWebInf

![image-20211217001503808](source-analysis.assets/image-20211217001503808.png)

> 调用getBaseResource()，然后再拼接"WEB-INF"目录

跟进

- org.eclipse.jetty.server.handler.ContextHandler#getBaseResource

![image-20211217002249571](source-analysis.assets/image-20211217002249571.png)

回溯调用栈

![image-20211217004418583](source-analysis.assets/image-20211217004418583.png)

- org.eclipse.jetty.webapp.WebXmlConfiguration#preConfigure
  - org.eclipse.jetty.webapp.WebAppContext#preConfigure
    - org.eclipse.jetty.webapp.WebAppContext#doStart

断点，跟进

- org.eclipse.jetty.webapp.WebAppContext#doStart

![image-20211217010553895](source-analysis.assets/image-20211217010553895.png)

> 调用preConfigure()

跟进

- org.eclipse.jetty.webapp.WebAppContext#preConfigure

![image-20211217010831953](source-analysis.assets/image-20211217010831953.png)

跟进

- org.eclipse.jetty.webapp.WebAppContext#loadConfigurations

![image-20211217011014183](source-analysis.assets/image-20211217011014183.png)

进行预加载

![image-20211217011112574](source-analysis.assets/image-20211217011112574.png)

跳过无关紧要的部分，跟进到

![image-20211217013218109](source-analysis.assets/image-20211217013218109.png)

> 遍历所有Configuration，然后调用其preConfigure方法进行预加载，在WebAppContext#preConfigure执行结束后，调用super.doStart()

跟进

- org.eclipse.jetty.servlet.ServletContextHandler#doStart

  - org.eclipse.jetty.server.handler.ContextHandler#doStart

    ![image-20211217014201379](source-analysis.assets/image-20211217014201379.png)

    - org.eclipse.jetty.server.handler.ContextHandler#startContext

      - org.eclipse.jetty.webapp.WebAppContext#startContext

        ![image-20211217014312945](source-analysis.assets/image-20211217014312945.png)

跟进

- org.eclipse.jetty.webapp.WebAppContext#configure

![image-20211217014404379](source-analysis.assets/image-20211217014404379.png)

> 遍历所有Configuration，然后调用其configure方法

跟进

- org.eclipse.jetty.webapp.WebXmlConfiguration#configure

![image-20211217014610469](source-analysis.assets/image-20211217014610469.png)

> 给WebAppContext的MetaData添加了一个DescriptorProcessor

跟进

- org.eclipse.jetty.webapp.StandardDescriptorProcessor#StandardDescriptorProcessor

![image-20211217014753526](source-analysis.assets/image-20211217014753526.png)

> bio,与web.xml的标签对应上了，调用registerVisitor注册了web.xml中的元素中的方法，后续会用反射机制来调用这些方法。比如filter元素用visitFilter处理。

跟进

- org.eclipse.jetty.webapp.IterativeDescriptorProcessor#registerVisitor

![image-20211217014939255](source-analysis.assets/image-20211217014939255.png)

> ？？？

configuration.configure遍历结束后，调用_metadata.resolve

跟进

- org.eclipse.jetty.webapp.MetaData#resolve

![image-20211217020041053](source-analysis.assets/image-20211217020041053.png)

关键代码

![image-20211217020157232](source-analysis.assets/image-20211217020157232.png)

> 调用p.process

跟进

- org.eclipse.jetty.webapp.IterativeDescriptorProcessor#process

![image-20211217020358436](source-analysis.assets/image-20211217020358436.png)

> 调用了visit()

跟进

- org.eclipse.jetty.webapp.IterativeDescriptorProcessor#visit

![image-20211217020502765](source-analysis.assets/image-20211217020502765.png)

> 当解析到web.xml的某个tag时，反射调用WebXmlConfiguration#configure注册的相应方法

`此处省略部分分析，当metadata被resolve完后，会调用super.startContext(),也就是它的父类ServletContextHandler的startContext方法`

跟进

- org.eclipse.jetty.servlet.ServletContextHandler#startContext

![image-20211217021404772](source-analysis.assets/image-20211217021404772.png)

> 调用super.startContext，执行结束后，调用initialize方法

跟进

- org.eclipse.jetty.server.handler.ContextHandler#startContext

![image-20211217021835307](source-analysis.assets/image-20211217021835307.png)

> 回到initialize方法

跟进

- org.eclipse.jetty.servlet.ServletHandler#initialize
  - 初始化

![image-20211217023418128](source-analysis.assets/image-20211217023418128.png)



## 源码剖析

### 接口 Handler 

![image-20211217172652458](source-analysis.assets/image-20211217172652458.png)

实现类

![image-20211217172728837](source-analysis.assets/image-20211217172728837.png)



#### 类 ContextHandler 

![image-20211217173619222](source-analysis.assets/image-20211217173619222.png)

Is subclassed by

![image-20211217174108730](source-analysis.assets/image-20211217174108730.png)

#####  类 ServletContextHandler

![image-20211217174219636](source-analysis.assets/image-20211217174219636.png)



#####  类 WebAppContext 

- 继承自ServletContextHandler，同时实现了WebAppClassLoader.Context接口，完成系统类判断和控制类加载顺序

![image-20211218231623192](source-analysis.assets/image-20211218231623192.png)

###### 构造方法 WebAppContext

- 创建WebAppContext

![image-20211218231832064](source-analysis.assets/image-20211218231832064.png)

![image-20211218231948762](source-analysis.assets/image-20211218231948762.png)

###### 成员方法 doStart() 

- 启动 WebAppContext

![image-20211218232235404](source-analysis.assets/image-20211218232235404.png)

> 调用preConfigure进行预加载

![image-20211218232506305](source-analysis.assets/image-20211218232506305.png)

会判断是否设置过类加载器，没有则自动创建一个WebAppClassLoader

![image-20211218232817015](source-analysis.assets/image-20211218232817015.png)

###### 类加载器 WebAppClassLoader

- 继承至URLClassLoader

![image-20211218232933399](source-analysis.assets/image-20211218232933399.png)



**成员方法 loadClass**

![image-20211218233320071](source-analysis.assets/image-20211218233321940.png)

- [详见《血泪的 Jetty ClassLoader》](https://toutiao.io/posts/xy5ng7/preview)

###### 成员方法 startContext

- 启动上下文

![image-20211218233654127](source-analysis.assets/image-20211218233654127.png)







#### 类 ServletHandler 

- 很多熟悉的面孔！！！
- ServletHandler是一个用于管理Filter、FilterMapping、Servlet、ServletMapping的容器

![image-20211217172917268](source-analysis.assets/image-20211217172917268.png)

部分成员方法（以下以filter相关的方法为例）

- 获取到ServletHandler，即可调用add*系列API动态添加filter、listener、servlet内存马
- 依赖FilterHolder、ServletHolder、ServletMapping、FilterMapping四大组件

![image-20211217173209887](source-analysis.assets/image-20211217173209887.png)

##### 成员方法 addFilter

![image-20211217175856347](source-analysis.assets/image-20211217175856347.png)

##### 成员方法 setFilters

![image-20211217175432854](source-analysis.assets/image-20211217175432854.png)

> 将当前ServletHandler容器中的所有FilterHolder设置上关联的ServletHandler，并将这些filters放到容器托管生命周期，同时更新_filters。

##### 成员方法 addListener 

![image-20211217180215190](source-analysis.assets/image-20211217180215190.png)

##### 成员方法 addFilterWithMapping

![image-20211217180939747](source-analysis.assets/image-20211217180939747.png)

> 获取到当前容器中的Filters，然后复制一份数组对象（浅拷贝），加锁去重判断是否存在，并设置这个FilterHolder到_filters数组，创建FilterMapping，传入必要的参数，调用addFilterMapping

##### 成员方法 addFilterMapping

![image-20211217181125392](source-analysis.assets/image-20211217181125392.png)

> 获取mapping对应的FilterHolder的source并进行判断，根据不同的条件插到不同位置

##### 成员方法 prependFilterMapping

![image-20211217183240304](source-analysis.assets/image-20211217183240304.png)

> 将新的FilterMapping添加到数组前


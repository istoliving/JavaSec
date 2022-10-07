历史漏洞

- pre-auth  SSRF/FileRead - custom.jsp
- post-auth SQLi - kmImeetingRes.do
- post-auth XMLDecoderDeserialization - sysSearchMain.do
- post-auth RCE = getBean() + bsh.Interpreter - dataxml.jsp
- post-auth JDBC RCE - admin.do
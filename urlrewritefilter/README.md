

#### [Arbitrary resource file download in urlrewrite.xml](https://jira.atlassian.com/browse/CONFSERVER-26888) 

**Description**

There is an arbitrary resource file download vulnerability triggered by a third party library org.tuckey.web.filters.urlrewrite.UrlRewriteFilter.

The urlrewrite.xml rules file shows the pattern that will trigger a <to type="forward"> forward rule, which is the equivelant of performing dp = request.getServletContext().getRequestDispatcher(resource); dp.forward(request, response);. This construct allows a user to forward requests to any resource file on the server, such as /WEB-INF/web.xml - which could potentially contain sensitive information like usernames and passwords.

web.xml
```xml
<!-- this filter is used to rewrite through the /s/* filter to add caching headers. see: urlrewrite.xml -->
<filter>
    <filter-name>UrlRewriteFilter</filter-name>
    <filter-class>org.tuckey.web.filters.urlrewrite.UrlRewriteFilter</filter-class>
</filter>
```
urlrewrite.xml
```xml
...
    <rule>
	<from>^/s/(.*)/_/([^\?]*).*</from>
        <run class="com.atlassian.plugin.servlet.ResourceDownloadUtils" method="addPublicCachingHeaders" />
        <to type="forward">/$2</to>
    </rule>
</urlrewrite>
```

The attached screenshot shows this issue being exploited.

![image](https://user-images.githubusercontent.com/55024146/186207280-b6436a75-5da9-4c69-887d-772887afbcb3.png)

#### CVE-2021-26085 & CVE-2021-26086

- https://hackerone.com/reports/1369288
- https://xz.aliyun.com/t/10109
- https://tttang.com/archive/1323/
  
#### CVE-2022–31656

- https://petrusviet.medium.com/dancing-on-the-architecture-of-vmware-workspace-one-access-eng-ad592ae1b6dd

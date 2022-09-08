##### v2.7.2

###### 缺省设置 - StandardMultipartFile - 可路径穿越

- spring-autoconfigure-metadata.properties

![](img/Pasted%20image%2020220818202936.png)
- org.springframework.web.servlet.DispatcherServlet#checkMultipart

![](img/Pasted%20image%2020220818202952.png)

- org.springframework.web.multipart.support.StandardMultipartHttpServletRequest.StandardMultipartFile#getOriginalFilename

```java
public String getOriginalFilename() {  
    return this.filename;  
}
```

没有对文件名进行处理，可以使用 `../` 进行路径穿越

![](img/Pasted%20image%2020220818203007.png)

![](img/Pasted%20image%2020220818203017.png)

###### 自定义设置 - CommonsMultipartResolver - 不可路径穿越

需要引入 `commons-fileupload` 依赖

```xml
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.4</version>
</dependency>
```

- org.springframework.web.multipart.commons.CommonsMultipartFile#getOriginalFilename

```java
public String getOriginalFilename() {  
    String filename = this.fileItem.getName();  
    if (filename == null) {  
        return "";  
    } else if (this.preserveFilename) {  
        return filename;  
    } else {
        // 出现 Linux 下分隔符的最后一个位置 `\`  
        int unixSep = filename.lastIndexOf(47); 
        // 出现 Windows 下分隔符的最后一个位置 `/`
        int winSep = filename.lastIndexOf(92);
        // 比较 Lin 分隔符 和 Win 分隔符的位置，选择最靠后的位置
        int pos = Math.max(winSep, unixSep);  
        // 截取最靠后的分隔符的位置进行截取 
        // ../../\\/./filename -> filename
        return pos != -1 ? filename.substring(pos + 1) : filename;  
    }  
}
```

![](img/Pasted%20image%2020220818183404.png)

##### <= v1.2.7.RELEASE

###### 自定义设置 - CommonsMultipartResolver - 可路径穿越

> 内嵌 spring-web-4.1.8，而 Windows 下的路径穿越问题在 4.1.9 才修复。

- org.springframework.web.multipart.commons.CommonsMultipartFile#getOriginalFilename

```java
public String getOriginalFilename() {  
    String filename = this.fileItem.getName();  
    if (filename == null) {  
        return "";  
    } else {  
        int pos = filename.lastIndexOf("/"); 
        /**
        *  filename = ../..\\flag.txt 
        *  pos == 2 != -1
        *  filename = filename.substring(pos + 1) = 
        * */
        
        if (pos == -1) {  
            pos = filename.lastIndexOf("\\");  
        }  
  
        return pos != -1 ? filename.substring(pos + 1) : filename;  
    }  
}
```

已在 v4.0.9 修复

![](img/Pasted%20image%2020220818203038.png)

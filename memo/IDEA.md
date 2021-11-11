**操作备忘**:

- 01 [常见图标的含义](https://blog.csdn.net/cgl125167016/article/details/78671232)
-  02 [查看类的继承关系图形](https://www.cnblogs.com/deng-cc/p/6927447.html)






**报错备忘**
- 01 Could not transfer artifact org.springframework.boot:
```
<--加入阿里镜像-->
<repositories>
  <repository>
    <id>aliyunmaven</id>
     <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
   </repository>
</repositories>
```
- 02 Dependency 'org.springframework.boot:spring-boot-starter-web:2.3.0.RELEASE' not found

 > Invalidate Caches and Restart

![image](https://user-images.githubusercontent.com/55024146/141257755-9e6ddd31-1248-4d3f-bb6e-5b05fda8d6e2.png)


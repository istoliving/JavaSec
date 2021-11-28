**操作备忘**:

- 01 [常见图标的含义](https://blog.csdn.net/cgl125167016/article/details/78671232)
- 02 [查看类的继承关系图形](https://www.cnblogs.com/deng-cc/p/6927447.html)




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

- 03 pom.xml 导入本地依赖包

```
<!-- spring MVC依赖包 -->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-webmvc</artifactId>
  <version>5.2.1.RELEASE</version>
  <scope>system</scope>
  <systemPath>${project.basedir}/../lib/spring-webmvc-5.2.1.RELEASE.jar</systemPath>
</dependency>
```
终于不报错了！！！

![image](https://user-images.githubusercontent.com/55024146/141419472-0cac0cda-b04d-47d2-9fef-ff8de678ff6a.png)

- 04 解决IDEA 不识别webapp文件夹的问题

![image](https://user-images.githubusercontent.com/55024146/143774426-ea2cd01e-b48a-4797-9a31-9bcd9b6d3f12.png)


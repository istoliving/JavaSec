## 基础篇
### 系统属性

常与System.getProperty()、System.getProperties()方法搭配使用

```
java.version                    Java 运行时环境版本
java.vendor                     Java 运行时环境供应商
java.vendor.url                 Java 供应商的 URL
java.vm.specification.version   Java 虚拟机规范版本
java.vm.specification.vendor    Java 虚拟机规范供应商
java.vm.specification.name      Java 虚拟机规范名称
java.vm.version                 Java 虚拟机实现版本
java.vm.vendor                  Java 虚拟机实现供应商
java.vm.name                    Java 虚拟机实现名称
java.specification.version      Java 运行时环境规范版本
java.specification.vendor       Java 运行时环境规范供应商
java.specification.name         Java 运行时环境规范名称
os.name                         操作系统的名称
os.arch                         操作系统的架构
os.version                      操作系统的版本
file.separator                  文件分隔符（在 UNIX 系统中是“ / ”）
path.separator                  路径分隔符（在 UNIX 系统中是“ : ”）
line.separator                  行分隔符（在 UNIX 系统中是“ /n ”）
java.home                       Java 安装目录
java.class.version              Java 类格式版本号
java.class.path                 Java 类路径
java.library.path               加载库时搜索的路径列表
java.io.tmpdir                  默认的临时文件路径
java.compiler                   要使用的 JIT 编译器的名称
java.ext.dirs                   一个或多个扩展目录的路径
user.name                       用户的账户名称
user.home                       用户的主目录
user.dir                        用户当前工作目录
```

## 漏洞篇

### Command Execution 命令执行

Java下几种执行命令的方式

- java.lang.Runtime
  - exec()
  - load()
- java.lang.ProcessBuilder
- java.lang.ProcessImpl
- JNI(暂放)

#### java.lang.Runtime

##### exec()

测试代码

```java
import java.io.IOException;

public class CE01 {
    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec("calc");
    }
}
```

测试效果

![image-20220119163614488](command%20execution.assets/image-20220119163614488.png)

调用栈

```
create:-1, ProcessImpl (java.lang)
<init>:386, ProcessImpl (java.lang)
start:137, ProcessImpl (java.lang)
start:1029, ProcessBuilder (java.lang)
exec:620, Runtime (java.lang)
exec:450, Runtime (java.lang)
exec:347, Runtime (java.lang)
main:6, CE01 (CommandExecution.Runtime)
```



##### load()

- 加载动态链接库，如linux下的so文件，windows下的dll文件。

准备dll - 弹计算器

```
msfvenom -p windows/x64/exec --platform win -a x64 CMD=calc.exe EXITFUNC=thread -f dll> calc.dll
```

![image-20211108002026565](command%20execution.assets/image-20211108002026565.png)

测试代码

```java
package CommandExecution.Runtime;

public class CE00 {
    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        rt.load("F:\\Java_Sec\\java\\java_vuln\\src\\main\\java\\CommandExecution\\Runtime\\calc.dll");
    }
}
```

测试效果

![image-20220120170955546](command%20execution.assets/image-20220120170955546.png)

#### java.lang.ProcessBuilder

测试代码

```java
import java.io.IOException;

public class CE02 {
    public static void main(String[] args) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("calc");
        Process process = pb.start();
        System.out.println(process);
    }
}
```

测试效果

![image-20220119164953772](command%20execution.assets/image-20220119164953772.png)

调用栈

```java
create:-1, ProcessImpl (java.lang)
<init>:386, ProcessImpl (java.lang)
start:137, ProcessImpl (java.lang)
start:1029, ProcessBuilder (java.lang)
main:7, CE02 (CommandExecution.ProcessBuilder)
```

#### java.lang.ProcessImpl

测试代码

```java
import java.lang.reflect.Method;
import java.util.Map;

public class CE03 {
    public static void main(String[] args) throws Exception{
        /**
         * 反射调用 java.lang.ProcessImpl#start(java.lang.String[], java.util.Map, java.lang.String, java.lang.ProcessBuilder.Redirect[], boolean)
         */
        Class clazz = Class.forName("java.lang.ProcessImpl");
        Method start = clazz.getDeclaredMethod("start", String[].class, Map.class, String.class, ProcessBuilder.Redirect[].class, boolean.class);
        start.setAccessible(true);
        start.invoke(null, new String[]{"calc"}, null, null, null, false);
    }
}
```

测试效果

![image-20220120171337998](command%20execution.assets/image-20220120171337998.png)



调用栈

```java
create:-1, ProcessImpl (java.lang)
<init>:386, ProcessImpl (java.lang)
start:137, ProcessImpl (java.lang)
invoke0:-1, NativeMethodAccessorImpl (sun.reflect)
invoke:62, NativeMethodAccessorImpl (sun.reflect)
invoke:43, DelegatingMethodAccessorImpl (sun.reflect)
invoke:498, Method (java.lang.reflect)
main:14, CE03 (CommandExecution.ProcessImpl)
```

#### JNI

(暂放)






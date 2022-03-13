前置基础
---


相关漏洞
---

### 0x01 默认账号密码
```
sysadmin/1
```
对应数据库的
- 表名：`HrmResourceManager` 
- 字段：`password`

### 0x02 XStream 反序列化漏洞

需要考虑的实战场景：
- XStream的不出网利用
  - CVE-2021-39149 TemplatesImpl
  - CVE-2021-21350 BCEL
- 回显 & 内存马




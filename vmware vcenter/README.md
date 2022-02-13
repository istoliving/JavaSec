基础信息
--- 

#### 获取版本号
version_detect.xml
```xml
<env:Envelope xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      <env:Body>
      <RetrieveServiceContent xmlns="urn:vim25">
        <_this type="ServiceInstance">ServiceInstance</_this>
      </RetrieveServiceContent>
      </env:Body>
      </env:Envelope>
```

Curl One Liner
```shell
type version_detect.xml | curl -X POST -k -H 'Content-type:text/xml' -d @- https://10.10.100.100/sdk
```

![image](https://user-images.githubusercontent.com/55024146/153741369-ea745c9d-7851-4971-a2df-a3eb08605e6a.png)

#### 数据库配置文件
```shell
find -name vcdb.properties
cat /etc/vmware-vpx/vcdb.properties
# cat /etc/vmware/service-state/vpxd/vcdb.properties
```
![image](https://user-images.githubusercontent.com/55024146/153741704-673cb757-be5e-4be5-93bd-cd2cef0fa93a.png)


相关漏洞
---
- CVE-2021-44228 VMware Product RCE via Log4Shell
- CVE-2021-22017 VMware vCenter rhttpproxy Bypass
- CVE-2021-22005 VMware vCenter 文件上传
- CVE-2021-21985 VMware vCenter 远程代码执行
- CVE-2021-21973 VMware vCenter SSRF - /sdk
- CVE-2021-21972 VMware vCenter 远程命令执行
- CVE-2021-00000 VMware vCenter 文件读取 - /eam/vib?id=
- CVE-2021-00000 VMware vCenter SSRF/文件读取 - /ui/vcav-bootstrap/rest/vcav-providers/provider-logo?url=

### CVE-2021-22005

####  漏洞描述

####  漏洞复现

##### 环境搭建

##### 漏洞验证

#### 漏洞分析

#### 漏洞修复

#### 漏洞利用（可选）

攻击路径
--- 
- CVE-2021-44228(root) -> CVE-2020-3952 -> gain Administrative access
- CVE-2021-22005(root) -> CVE-2020-3952 -> gain Administrative access
- CVE-2021-21985(no root) -> CVE-2021-3156/CVE-2021-4034(root) -> CVE-2020-3952 -> gain Administrative access
- CVE-2021-21972(no root) -> CVE-2021-3156/CVE-2021-4034(root) -> CVE-2020-3952 -> gain Administrative access

### CVE-2021-22005(root) -> CVE-2020-3952 -> gain Administrative access
> 实战案例

CVE-2021-22005获取初始webshell权限

CVE-2020-3952提取IdP证书、伪造管理员cookie获取后台权限
- 工具地址
    - [vcenter_saml_login](https://github.com/horizon3ai/vcenter_saml_login)

data.mdb位置：
- Linux:
    
    ```
    /storage/db/vmware-vmdir/data.mdb
    ```
- Windows
    
    ```
    C:\ProgramData\VMware\vCenterServer\data\vmdird\data.mdb
    ```

![image](https://user-images.githubusercontent.com/55024146/144646720-bc6bb84a-def4-41e6-8ccc-8aedd89165b8.png)

访问https://10.10.10.1/ui，在 /ui 路径下替换上一步所获得的cookie

![image](https://user-images.githubusercontent.com/55024146/144649479-4b2c2947-5aa2-44bb-b0d5-c7e610799e78.png)


扩大战果
- 可通过vcenter的快照功能获取虚拟机的快照，然后通过内存取证的姿势dump凭证，pth；
- 也可将快照传到本地，再恢复成虚拟机，然后通过PE，重命名CMD.EXE为OSK.exe覆盖原OSK.exe，此时开机打开屏幕键盘会弹出SYSTEM权限的命令行窗口，本地上线cs然后hashdump抓取凭证，pth即可。（by banliz1）

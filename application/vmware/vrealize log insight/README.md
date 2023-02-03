VMSA-2023-0001 pre-auth ZipSlip -> RCE
--- 

#### 漏洞分析


漏洞的重点在于两处：
- 1 Thrift RPC 未授权的调用
- 2 .pak 文件处理时的 zip slip 漏洞

**1 Thrift RPC 未授权的调用**

DaemonCommands 实现的 Thrift RPC 命令
```java
processMap.put("waitUntilStarted", new waitUntilStarted());
processMap.put("waitUntilDaemonStarted", new waitUntilDaemonStarted());
processMap.put("requestCommand", new requestCommand());
processMap.put("runCommand", new runCommand());
processMap.put("getCommandStatus", new getCommandStatus());
processMap.put("cancelQueuedCommand", new cancelQueuedCommand());
processMap.put("cancelRunningCommand", new cancelRunningCommand());
processMap.put("getQueuedCommands", new getQueuedCommands());
processMap.put("getRunningCommands", new getRunningCommands());
processMap.put("getExitedCommands", new getExitedCommands());
processMap.put("showInstrumentationStats", new showInstrumentationStats());
processMap.put("resetInstrumentationStats", new resetInstrumentationStats());
processMap.put("getHealthStatus", new getHealthStatus());
processMap.put("getRecentHealthStatus", new getRecentHealthStatus());
processMap.put("getArchiveStatus", new getArchiveStatus());
processMap.put("getArchiveStatuses", new getArchiveStatuses());
processMap.put("getLiveHealthMeter", new getLiveHealthMeter());
processMap.put("serviceStartResult", new serviceStartResult());
processMap.put("getVsphereHosts", new getVsphereHosts());
processMap.put("configureVsphereHosts", new configureVsphereHosts());
processMap.put("sampleEventType", new sampleEventType());
processMap.put("expandEventType", new expandEventType());
processMap.put("expandPattern", new expandPattern());
processMap.put("getLocalStats", new getLocalStats());
processMap.put("getClusterStats", new getClusterStats());
processMap.put("getClusterHealthStatus", new getClusterHealthStatus());
processMap.put("getClusterLiveHealthMeter", new getClusterLiveHealthMeter());
processMap.put("getConfig", new getConfig());
processMap.put("setConfig", new setConfig());
processMap.put("waitUntilBootstrapped", new waitUntilBootstrapped());
processMap.put("getNodeType", new getNodeType());
processMap.put("setNodeType", new setNodeType());
processMap.put("setTokenOnWorker", new setTokenOnWorker());
processMap.put("isBootstrapped", new isBootstrapped());
processMap.put("isRestartRequired", new isRestartRequired());
processMap.put("setBootstrapped", new setBootstrapped());
processMap.put("unbootstrap", new unbootstrap());
processMap.put("join", new join());
processMap.put("applyMembership", new applyMembership());
processMap.put("approveMembership", new approveMembership());
processMap.put("removeMembership", new removeMembership());
processMap.put("setMaintenanceMode", new setMaintenanceMode());
processMap.put("getMaintenanceNodes", new getMaintenanceNodes());
processMap.put("getMembers", new getMembers());
processMap.put("getMembershipPendingWorkers", new getMembershipPendingWorkers());
processMap.put("getNodeStatus", new getNodeStatus());
processMap.put("getNodeIPs", new getNodeIPs());
processMap.put("sendAlertNotification", new sendAlertNotification());
processMap.put("runRemoteUpgradeCommand", new runRemoteUpgradeCommand());
processMap.put("updateClusterSslCertificate", new updateClusterSslCertificate());
processMap.put("getClusterCertificate", new getClusterCertificate());
processMap.put("restoreDefaultClusterSslCertificate", new restoreDefaultClusterSslCertificate());
processMap.put("isCustomCertificateUsed", new isCustomCertificateUsed());
processMap.put("addClusterCACertificate", new addClusterCACertificate());
processMap.put("removeClusterCACertificate", new removeClusterCACertificate());
processMap.put("repairCassandra", new repairCassandra());
processMap.put("runPostUpgradeMigrations", new runPostUpgradeMigrations());
```

当前 rce chain 只用到了 requestCommand 、getConfig

1) 通过 getConfig 获取 token，为什么需要这个token？ 
- com.vmware.loginsight.daemon.commands.SystemCommands#remotePakDownloadCommand

```java
public AbstractCommandExecutor remotePakDownloadCommand(final RemotePakDownloadCommand remotePakDownloadCommand, DistributedConfig distributedConfig) throws DistributedConfigException, MalformedURLException, RemoteUpgradeException {
    if (!remotePakDownloadCommand.getSourceNodeToken().equals(distributedConfig.getMasterDaemon().getToken())) {
        throw new RemoteUpgradeException("Remote PAK Download command must come from master.");
    } else {
```

2) 通过 requestCommand 可执行的命令

- com.vmware.loginsight.daemon.DaemonCommandsHandler#requestCommand 

```java
switch (command.commandType) {
    case SHUTDOWN_COMMAND:
        ShutdownCommandExecutor sce = new ShutdownCommandExecutor(this.shutdownRunnable);
        if (!command.shutdownCommand.immediately) {
            return this.commandManager.addCommand(command, sce, !command.shutdownCommand.waitForQueued);
        }

        sce.now();
        break;
    case REPO_IMPORT_COMMAND:
        return this.commandManager.addCommand(command, this.repoCommands.importCommand(command.repoImportCommand));
    case RESTART_COMMAND:
        return this.commandManager.addCommand(command, this.systemCommands.restartCommand(command.restartCommand), !command.restartCommand.waitForQueued);
    case PHONE_HOME_FEEDBACK_COMMAND:
        return this.commandManager.addCommand(command, this.systemCommands.phoneHomeFeedbackCommand(command.phoneHomeFeedbackCommand));
    case HOST_SYNC_COMMAND:
        return this.commandManager.addCommand(command, this.systemCommands.hostSyncCommand(command.hostSyncCommand));
    case NTP_SYNC_COMMAND:
        return this.commandManager.addCommand(command, this.systemCommands.ntpSyncCommand(command.ntpSyncCommand));
    case SUPPORT_BUNDLE_COMMAND:
        return this.commandManager.addCommand(command, this.systemCommands.supportBundleCommand(command.supportBundleCommand));
    case PAK_UPGRADE_COMMAND:
        return this.commandManager.addCommand(command, this.systemCommands.pakUpgradeCommand(command.pakUpgradeCommand));
    case CONFIG_LOL_COMMAND:
        return this.commandManager.addCommand(command, this.systemCommands.configLolCommand());
    case REMOTE_PAK_DOWNLOAD_COMMAND:
        return this.commandManager.addCommand(command, this.systemCommands.remotePakDownloadCommand(command.remotePakDownloadCommand, (DistributedConfig)this.configurationHolder.getConfiguration(DistributedConfig.class)));
  }
```

分别调用 remotePakDownloadCommand 和 pakUpgradeCommand 下载恶意的 pak 并进行处理

**2 .pak 文件处理时的 zip slip 漏洞**

- com.vmware.loginsight.daemon.commands.SystemCommands#pakUpgradeCommand

pakUpgradeCommand 的执行流程中会调用py脚本执行 pak 的处理

- opt/vmware/bin/loginsight-pak-upgrade

```python
if __name__ == '__main__':
    loginsight_home = '/usr/lib/loginsight'
    cmd = [os.path.join(loginsight_home, 'application', 'sbin', 'loginsight-pak-upgrade.py')]
    cmd.extend(sys.argv[1:])
    p = subprocess.Popen(cmd, preexec_fn=os.setsid)
    p.wait()
    sys.exit(p.returncode)
```

- /usr/lib/loginsight/application/sbin/loginsight-pak-upgrade.py

关键代码

```python

# application/sbin/loginsight-pak-upgrade.py:378
import tarfile

# Extract necessary files from the pak file
def extractFiles(inputFile, fileList):
    try:
        tar = tarfile.open(inputFile, "r")
    except:
        raise Exception("Cannot open " + inputFile)
    try:
        if len(fileList) == 0:
            tar.extractall()
        else:
            for fname in fileList:
                tar.extract(fname)
    except:
        raise Exception("Cannot extract file from pak file")
    finally:
        tar.close()
    return
```

在从 pak 文件中提取文件，调用python 的 tarfile库进行解压

- https://www.securecodewarrior.com/article/traversal-bug-in-pythons-tarfile-module

由于该库存在zipslip漏洞，从而导致任意文件写入

---

注意: 当调用 extractFiles(inputFile, fileList)，且fileList不为空时，只会解压filelist中的文件，所以需要找到fileList为空的调用

```python
 # perform upgrade
else:
    fileList = [pak.mfFile, pak.certFile]
    # extract .cert and .mf files
    extractFiles(inputFile, fileList)
    # verify certificate and signature
    if pak.verifyCertificate():
        if pak.validateSignature():
            manifest = Manifest.parseManifest(pak.mfFile)
            if options.force_install:
                manifest.skip_version_check = True
            version = str(manifest.verTo)
            # check and perform upgrade
            if manifest.checkSupportVersion():
                if manifest.checkDiskSpace():
                    # extract all files
                    extractFiles(inputFile, [])
```

所以构造的pak文件需要满足3个条件:

```
if pak.verifyCertificate():
if pak.validateSignature():
if manifest.checkSupportVersion():
```

#### 漏洞复现

horizon3ai 后面放出了基于 py 的利用工具 https://github.com/horizon3ai/vRealizeLogInsightRCE ，NB但复杂

ps: 用 java 写 50 行左右代码足以


![image](https://user-images.githubusercontent.com/55024146/216533004-86098604-80c1-4b49-856c-36b8f0032bbd.png)



ref
- https://www.horizon3.ai/vmware-vrealize-cve-2022-31706-iocs/
- https://www.horizon3.ai/vmware-vrealize-log-insight-vmsa-2023-0001-technical-deep-dive/



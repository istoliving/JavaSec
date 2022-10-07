cipher = input("输入密文：\n")  # 密文
PASSWORD_MASK_ARRAY = [19, 78, 10, 15, 100, 213, 43, 23] 
password = ""
cipher = cipher[3:]  
for i in range(int(len(cipher) / 4)):
    c1 = int("0x" + cipher[i * 4:(i + 1) * 4], 16)
    c2 = c1 ^ PASSWORD_MASK_ARRAY[i % 8]
    password = password + chr(c2)
print("明文密码：\n"+password)

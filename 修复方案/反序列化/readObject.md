
### 01 ValidatingObjectInputStream（commons-io.jar）

commons-io.jar 的 ValidatingObjectInputStream 类提供了 accept/reject 方法来控制允许反序列化/不允许反序列化的类, 以达到黑/白名单的效果。


案例: GoAnywhere MFT (CVE-2023-0669)

修复前
```java
private static byte[] verify(byte[] paramArrayOfByte, KeyConfig paramKeyConfig) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnrecoverableKeyException, CertificateException, KeyStoreException {
    objectInputStream = null;
    try {
      String str = "SHA1withDSA";
      if ("2".equals(paramKeyConfig.getVersion())) {
        str = "SHA512withRSA";
      }
      PublicKey publicKey = getPublicKey(paramKeyConfig);
      objectInputStream = new ObjectInputStream(new ByteArrayInputStream(paramArrayOfByte));
      SignedObject signedObject = (SignedObject)objectInputStream.readObject();
```

修复后
```java
private static byte[] verify(byte[] var0, KeyConfig var1) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnrecoverableKeyException, CertificateException, KeyStoreException {
    // 只允许反序列化的类为 SignedObject、[B
    ObjectInputStream var2 = getSecureObjectInputStream(var0, SignedObject.class, byte[].class);

    byte[] var9;
    try {
        String var3 = "SHA1withDSA";
        if ("2".equals(var1.getVersion())) {
            var3 = "SHA512withRSA";
        }

        PublicKey var4 = getPublicKey(var1);
        SignedObject var5 = (SignedObject)var2.readObject();
```

修复方案

```java
    private static ObjectInputStream getSecureObjectInputStream(byte[] var0, Class<?>... var1) throws IOException {
        ValidatingObjectInputStream var2 = new ValidatingObjectInputStream(new ByteArrayInputStream(var0));
        var2.accept(var1);
        return var2;
    }
```

通过 ValidatingObjectInputStream 设置白名单，只允许反序列化的类为 `SignedObject` 和 `[B`，SignedObject虽然重写了 readObect(), 但是没发现有啥可以进一步绕过的地方。

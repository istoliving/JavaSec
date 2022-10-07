package code;

public class FineReport {
    private static final int[] PASSWORD_MASK_ARRAY = new int[]{19, 78, 10, 15, 100, 213, 43, 23};
    public static String passwordEncode(String paramString) {
        final StringBuilder sb = new StringBuilder();
        sb.append("___");
        int n = 0;
        for (int i = 0; i < paramString.length(); ++i) {
            if (n == PASSWORD_MASK_ARRAY.length) {
                n = 0;
            }
            String s2 = Integer.toHexString(paramString.charAt(i) ^ PASSWORD_MASK_ARRAY[n]);
            for (int length = s2.length(), j = 0; j < 4 - length; ++j) {
                s2 = "0" + s2;
            }
            sb.append(s2);
            ++n;
        }
        return sb.toString();
    }

    private static String passwordDecode(String paramString) {
        if (paramString != null && paramString.startsWith("___")) {
            paramString = paramString.substring(3);
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (int j = 0; j <= paramString.length() - 4; j += 4) {
                if (i == PASSWORD_MASK_ARRAY.length) {
                    i = 0;
                }
                String str = paramString.substring(j, j + 4);
                int k = Integer.parseInt(str, 16) ^ PASSWORD_MASK_ARRAY[i];
                sb.append((char) k);
                ++i;
            }
            paramString = sb.toString();
        }
        return paramString;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage:");
            System.out.println("FineReportX.jar --encode admin");
            System.out.println("FineReportX.jar --decode ___0072002a00670066000a00e400190024");
        } else {
            if ("--encode".equals(args[0])) {
                String pass = passwordEncode(args[1]);
                System.out.println(pass);
            } else if ("--decode".equals(args[0])) {
                String pass = passwordDecode(args[1]);
                System.out.println(pass);
            }
        }
    }
}

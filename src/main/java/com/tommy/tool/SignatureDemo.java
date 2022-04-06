package com.tommy.tool;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import static java.nio.charset.StandardCharsets.UTF_8;

public class SignatureDemo {

    private static void withdrawalSignature() {
        String key = "13653cb1-1cf5-4c43-b123-611dd8f720d7";
        String algorithm = "HMACSHA512";
        String contentType = "application/json; charset=utf-8";
        String keyId = "bc4d3b07-e8b4-4ed0-97f8-7fc188b7b12e";
        String body = """
            {"referenceId":"tommyWithdrawal27894995179400","deviceType":"WEB","bankCode":"ICBC","bankBranch":"北京北京","cardHolderName":"湯米","cardNumber":"012345678901234567","amount":100.00}
            """;
        String sign = new Signature().sign(key, algorithm, contentType, keyId, body);
        System.out.println(sign);
    }

    private static void depositSignature() {
        String key = "13653cb1-1cf5-4c43-b123-611dd8f720d7";
        String algorithm = "HMACSHA512";
        String contentType = "application/json; charset=utf-8";
        String keyId = "bc4d3b07-e8b4-4ed0-97f8-7fc188b7b12e";
        String body = """
            {"deviceType":"WEB","identity": "depositTommyTest0000000001"}
            """;
        String sign = new Signature().sign(key, algorithm, contentType, keyId, body);
        System.out.println(sign);
    }

    public static String sign(String key, String contentType, String keyId, String body) {
        String algorithm = "HMACSHA512";
        String sign = new Signature().sign(key, algorithm, contentType, keyId, body);
        System.out.println(sign);
        return sign;
    }


    public static void main(String[] args) {

        System.out.println("[LOG] depositSignature");
        depositSignature();
        System.out.println("[LOG] depositSignature");

        System.out.println("[LOG] withdrawalSignature");
        withdrawalSignature();
        System.out.println("[LOG] withdrawalSignature");


    }


    public static class Signature {
        public static final String ALGORITHM_HEADER = "algorithm";
        public static final String CONTENT_TYPE_HEADER = "content-type";
        public static final String KEY_ID_HEADER = "key-id";
        public static final String HEADER_DELIMITER = ":";
        public static final String DELIMITER = "\n";

        public String sign(String key, String algorithm, String contentType, String keyId, String body) {
            String message = String.join(DELIMITER,
                header(ALGORITHM_HEADER, algorithm),
                header(CONTENT_TYPE_HEADER, contentType),
                header(KEY_ID_HEADER, keyId),
                body.trim());

            byte[] digest = digest(key.getBytes(UTF_8), message.getBytes(UTF_8));
            return base64(digest);
        }

        public byte[] digest(byte[] key, byte[] message) {
            if (key == null) throw new Error("key must not be null");
            try {
                Mac mac = Mac.getInstance("HmacSHA512");
                SecretKey secretKey = new SecretKeySpec(key, "HmacSHA512");
                mac.init(secretKey);
                return mac.doFinal(message);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new Error(e);
            }
        }

        private String base64(byte[] bytes) {
            return new String(Base64.getEncoder().encode(bytes), UTF_8);
        }

        private String header(String key, String value) {
            return toLowerCase(key) + HEADER_DELIMITER + value.trim();
        }

        private String toLowerCase(String text) {
            if (text == null) return null;
            int length = text.length();
            for (int i = 0; i < length; i++) {
                if (isUpperCase(text.charAt(i))) {
                    char[] chars = text.toCharArray();
                    for (int j = i; j < length; j++) {
                        char ch = chars[j];
                        if (isUpperCase(ch)) {
                            chars[j] = (char) (ch ^ 0x20);
                        }
                    }
                    return String.valueOf(chars);
                }
            }
            return text;
        }

        private boolean isUpperCase(char ch) {
            return ch >= 'A' && ch <= 'Z';
        }
    }


}

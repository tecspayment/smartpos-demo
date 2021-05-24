package at.tecs.smartpos_demo;

import java.nio.charset.StandardCharsets;

public class Utils {
    public static String bytes2HexStr(byte[] bytes) {
        /*
        StringBuilder sb = new StringBuilder();
        String temp;
        for (byte b : bytes) {
            // Combine each byte with 0xFF, convert it to decimal, and then convert it to hex by Integer
            temp = Integer.toHexString(0xFF & b);
            if (temp.length() == 1) {
                // Each byte 8 is converted to a hexadecimal flag, 2 hexadecimal digits
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString().toUpperCase();

         */
        byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

}

package test.task.api.Tools;

import java.util.Base64;

public class ImageDecoder {
    public static byte[] decodeBase64Image(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }
        String imageData = base64Image.split(",")[1];
        return Base64.getDecoder().decode(imageData);
    }
}

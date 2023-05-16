package com.example.btl_web.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Map;

public class FileUtils {
    public static String saveImageToServer(Part filePart, Long blogId)
    {
        String urlImage = null;

        // Tạo đối tượng Cloudinary
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dwlv4qogn",
                "api_key", "617883759449822",
                "api_secret", "b7Y5H4s1xPLJIaMHBr0AxFa9iQc"));

        // Lưu ảnh vào Cloudinary

        try
        {
            Map params = ObjectUtils.asMap(
                    "folder", "images/blog/",
                    "public_id", blogId + "_title",
                    "overwrite", true
            );
            byte[] imageBytes = filePart.getInputStream().readAllBytes();

            cloudinary.uploader().upload(imageBytes, params);

            // Lấy URL của ảnh từ Cloudinary

            String publicId = "images/blog/" + blogId + "_title";
            urlImage = cloudinary.url().generate(publicId);
            System.out.println(urlImage);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return urlImage;
    }
}
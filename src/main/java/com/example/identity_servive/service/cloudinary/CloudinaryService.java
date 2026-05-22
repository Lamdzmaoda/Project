package com.example.identity_servive.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor  // Tự tạo constructor cho các field final (giúp inject cloudinary)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {
    Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String folder) {
        try {
            // ObjectUtils.as() là helper của Cloudinary, tương tự Map.of()
            Map param = ObjectUtils.asMap(
                    "folder", folder,        // Lưu vào folder nào
                    "resource_type", "image" // Đây là file ảnh
            );
            // Upload file (file.getBytes() đọc nội dung file thành mảng byte)
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(),param);

            // "secure_url" là URL https của ảnh trên Cloudinary (có cả "url" là http)
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }
}

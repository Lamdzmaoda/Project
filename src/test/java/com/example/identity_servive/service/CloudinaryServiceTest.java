/* (C)2026 */
package com.example.identity_servive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.service.cloudinary.CloudinaryService;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class CloudinaryServiceTest {

  private CloudinaryService cloudinaryService;
  private Cloudinary cloudinary;
  private Uploader uploader;
  private MultipartFile file;

  @BeforeEach
  void initData() throws IOException {
    cloudinary = mock(Cloudinary.class);
    uploader = mock(Uploader.class);
    cloudinaryService = new CloudinaryService(cloudinary);
    file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test-image-content".getBytes());
  }

  @Test
  void uploadFile_success() throws IOException {
    when(cloudinary.uploader()).thenReturn(uploader);
    when(uploader.upload(any(), any(Map.class)))
        .thenReturn(Map.of("secure_url", "https://res.cloudinary.com/test.jpg"));

    var result = cloudinaryService.uploadFile(file, "avatars");

    assertThat(result).isEqualTo("https://res.cloudinary.com/test.jpg");
  }

  @Test
  void uploadFile_ioException_fail() throws IOException {
    when(cloudinary.uploader()).thenReturn(uploader);
    when(uploader.upload(any(), any(Map.class))).thenThrow(new IOException("Upload failed"));

    var exception =
        assertThrows(AppException.class, () -> cloudinaryService.uploadFile(file, "avatars"));

    assertThat(exception.getErrorCode().getCode()).isEqualTo(1026);
  }
}

package com.ensa.pfs.service.Impl;

import com.cloudinary.Cloudinary;
import com.ensa.pfs.dto.PredictionResponse;
import com.ensa.pfs.entity.Result;
import com.ensa.pfs.repository.ResultRepository;
import com.ensa.pfs.repository.UserRepository;
import com.ensa.pfs.service.FileUpload;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadImpl implements FileUpload {

    @Value("${api.url}")
    private String apiUrl;

    private final Cloudinary cloudinary;
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;


    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String url = cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        resultRepository.save(Result.builder()
                .imageUrl(url)
                .user(userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("User not Found")))
                .build()
        );

        return url;
    }

    @Override
    public String predictPlantDisease(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            body.add("file", new FileSystemResource(convert(file)));
        } catch (IOException e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null; // or throw an exception
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return responseEntity.getBody();
    }

    private File convert(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File convFile = new File(fileName);
        Files.copy(file.getInputStream(), convFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return convFile;
    }

}

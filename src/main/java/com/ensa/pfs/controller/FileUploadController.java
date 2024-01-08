package com.ensa.pfs.controller;

import com.ensa.pfs.dto.PredictionResponse;
import com.ensa.pfs.service.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
public class FileUploadController {

    private final FileUpload fileUpload;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(fileUpload.uploadFile(multipartFile));
    }

    @PostMapping("/predict")
    public String predictPlantDisease(@RequestParam("file") MultipartFile file) throws IOException {
        return fileUpload.predictPlantDisease(file);
    }
}

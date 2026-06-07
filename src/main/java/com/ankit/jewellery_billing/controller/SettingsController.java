package com.ankit.jewellery_billing.controller;

import com.ankit.jewellery_billing.dto.ApiResponse;
import com.ankit.jewellery_billing.dto.ShopSettingsRequest;
import com.ankit.jewellery_billing.dto.ShopSettingsResponse;
import com.ankit.jewellery_billing.service.ShopSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final ShopSettingsService shopSettingsService;

    @GetMapping
    public ResponseEntity<ApiResponse<ShopSettingsResponse>> getSettings() {
        return ResponseEntity.ok(ApiResponse.success(shopSettingsService.getSettings()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ShopSettingsResponse>> updateSettings(
            @Valid @RequestBody ShopSettingsRequest request) {
        ShopSettingsResponse settings = shopSettingsService.updateSettings(request);
        return ResponseEntity.ok(ApiResponse.success("Settings updated successfully", settings));
    }

    @PostMapping(value = "/signature", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ShopSettingsResponse>> uploadSignature(
            @RequestParam("file") MultipartFile file) throws IOException {
        ShopSettingsResponse settings = shopSettingsService.uploadSignatureImage(file);
        return ResponseEntity.ok(ApiResponse.success("Signature image uploaded successfully", settings));
    }

    @GetMapping("/signature")
    public ResponseEntity<byte[]> getSignatureImage() {
        byte[] image = shopSettingsService.getSignatureImage();
        String contentType = shopSettingsService.getSignatureImageType();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=signature")
                .contentType(MediaType.parseMediaType(contentType))
                .body(image);
    }

    @DeleteMapping("/signature")
    public ResponseEntity<ApiResponse<ShopSettingsResponse>> deleteSignature() {
        ShopSettingsResponse settings = shopSettingsService.deleteSignatureImage();
        return ResponseEntity.ok(ApiResponse.success("Signature image removed", settings));
    }
}

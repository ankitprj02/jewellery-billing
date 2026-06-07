package com.ankit.jewellery_billing.service;

import com.ankit.jewellery_billing.dto.ShopSettingsRequest;
import com.ankit.jewellery_billing.dto.ShopSettingsResponse;
import com.ankit.jewellery_billing.entity.ShopSettings;
import com.ankit.jewellery_billing.exception.BadRequestException;
import com.ankit.jewellery_billing.exception.ResourceNotFoundException;
import com.ankit.jewellery_billing.repository.ShopSettingsRepository;
import com.ankit.jewellery_billing.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShopSettingsService {

    private static final long MAX_SIGNATURE_SIZE = 2 * 1024 * 1024; // 2MB
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/png", "image/jpeg", "image/jpg", "image/webp"
    );

    private final ShopSettingsRepository shopSettingsRepository;

    public ShopSettingsResponse getSettings() {
        ShopSettings settings = getOrCreateSettings();
        return EntityMapper.toShopSettingsResponse(settings);
    }

    @Transactional
    public ShopSettingsResponse updateSettings(ShopSettingsRequest request) {
        ShopSettings settings = getOrCreateSettings();

        settings.setShopName(request.getShopName());
        settings.setAddress(request.getAddress());
        settings.setPhone(request.getPhone());
        settings.setEmail(request.getEmail());
        settings.setGstin(request.getGstin());
        settings.setSignatureText(request.getSignatureText());

        return EntityMapper.toShopSettingsResponse(shopSettingsRepository.save(settings));
    }

    @Transactional
    public ShopSettingsResponse uploadSignatureImage(MultipartFile file) throws IOException {
        validateSignatureFile(file);

        ShopSettings settings = getOrCreateSettings();
        settings.setSignatureImage(file.getBytes());
        settings.setSignatureImageType(file.getContentType());

        return EntityMapper.toShopSettingsResponse(shopSettingsRepository.save(settings));
    }

    @Transactional
    public ShopSettingsResponse deleteSignatureImage() {
        ShopSettings settings = getOrCreateSettings();
        settings.setSignatureImage(null);
        settings.setSignatureImageType(null);
        return EntityMapper.toShopSettingsResponse(shopSettingsRepository.save(settings));
    }

    public byte[] getSignatureImage() {
        ShopSettings settings = getOrCreateSettings();
        if (settings.getSignatureImage() == null || settings.getSignatureImage().length == 0) {
            throw new ResourceNotFoundException("Signature image not found");
        }
        return settings.getSignatureImage();
    }

    public String getSignatureImageType() {
        ShopSettings settings = getOrCreateSettings();
        return settings.getSignatureImageType() != null ? settings.getSignatureImageType() : "image/png";
    }

    public ShopSettings getSettingsEntity() {
        return getOrCreateSettings();
    }

    private ShopSettings getOrCreateSettings() {
        return shopSettingsRepository.findAll().stream().findFirst()
                .orElseGet(() -> shopSettingsRepository.save(ShopSettings.builder()
                        .shopName("Golden Jewellers")
                        .build()));
    }

    private void validateSignatureFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Please select a signature image");
        }
        if (file.getSize() > MAX_SIGNATURE_SIZE) {
            throw new BadRequestException("Signature image must be smaller than 2MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException("Only PNG, JPG, JPEG, and WEBP images are allowed");
        }
    }
}

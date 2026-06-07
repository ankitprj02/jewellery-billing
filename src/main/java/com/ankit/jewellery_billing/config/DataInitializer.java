package com.ankit.jewellery_billing.config;

import com.ankit.jewellery_billing.entity.ShopSettings;
import com.ankit.jewellery_billing.entity.User;
import com.ankit.jewellery_billing.repository.ShopSettingsRepository;
import com.ankit.jewellery_billing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ShopSettingsRepository shopSettingsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .enabled(true)
                    .build());
        }

        if (shopSettingsRepository.count() == 0) {
            shopSettingsRepository.save(ShopSettings.builder()
                    .shopName("Golden Jewellers")
                    .address("123 Main Street, Mumbai, Maharashtra - 400001")
                    .phone("+91 98765 43210")
                    .email("info@goldenjewellers.com")
                    .gstin("27AAAAA0000A1Z5")
                    .signatureText("Authorized Signatory")
                    .build());
        }
    }
}

package com.ankit.jewellery_billing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shop_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String shopName;

    @Column(length = 500)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String gstin;

    @Column(length = 200)
    private String signatureText;

    @Lob
    private byte[] signatureImage;

    @Column(length = 50)
    private String signatureImageType;
}

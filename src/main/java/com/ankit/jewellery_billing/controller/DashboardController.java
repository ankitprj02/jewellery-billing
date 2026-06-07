package com.ankit.jewellery_billing.controller;

import com.ankit.jewellery_billing.dto.ApiResponse;
import com.ankit.jewellery_billing.dto.DashboardStatsResponse;
import com.ankit.jewellery_billing.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getDashboardStats()));
    }
}

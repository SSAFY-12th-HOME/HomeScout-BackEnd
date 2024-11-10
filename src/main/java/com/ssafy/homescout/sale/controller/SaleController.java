package com.ssafy.homescout.sale.controller;

import com.ssafy.homescout.sale.dto.SaleRequestDto;
import com.ssafy.homescout.sale.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
@Validated
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<?> registerSale(
            @Valid @RequestBody SaleRequestDto saleRequestDto) {
        return ResponseEntity.ok(saleService.registerSale(saleRequestDto));
    }

}

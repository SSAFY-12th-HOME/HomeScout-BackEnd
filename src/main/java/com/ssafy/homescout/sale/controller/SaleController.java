package com.ssafy.homescout.sale.controller;

import com.ssafy.homescout.sale.dto.SaleRequestDto;
import com.ssafy.homescout.sale.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
@Validated
public class SaleController {

    private final SaleService saleService;

    // 내가 등록한 매물 리스트 조회
    @GetMapping
    public ResponseEntity<?> getMySaleList() {
        return ResponseEntity.ok(saleService.getMySaleList());
    }

    // 매물 등록
    @PostMapping
    public ResponseEntity<?> registerSale(
            @Valid @RequestBody SaleRequestDto saleRequestDto) {
        return ResponseEntity.ok(saleService.registerSale(saleRequestDto));
    }

}

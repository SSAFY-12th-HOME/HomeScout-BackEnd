package com.ssafy.homescout.sale.controller;

import com.ssafy.homescout.sale.dto.SaleEditRequestDto;
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

    // 매물 수정
    @PutMapping("/{saleId}")
    public ResponseEntity<?> editSale(@PathVariable("saleId") Long saleId,
                                      @RequestBody SaleEditRequestDto saleEditRequestDto) {
        return ResponseEntity.ok(saleService.editSale(saleId, saleEditRequestDto));
    }

    // 매물 삭제
    @DeleteMapping("/{saleId}")
    public ResponseEntity<?> removeSale(@PathVariable("saleId") Long saleId) {
        saleService.removeSale(saleId);
        return ResponseEntity.ok().build();
    }

}

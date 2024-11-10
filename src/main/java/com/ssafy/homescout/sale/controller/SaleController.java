package com.ssafy.homescout.sale.controller;

import com.ssafy.homescout.sale.dto.SaleEditRequestDto;
import com.ssafy.homescout.sale.dto.SaleRequestDto;
import com.ssafy.homescout.sale.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    // 관심 매물 리스트 조회
    @GetMapping("/wish")
    public ResponseEntity<?> getMyWishSaleList() {
        return ResponseEntity.ok(saleService.getMyWishSaleList());
    }

    // 관심 매물 등록
    @PostMapping("/{saleId}/wish")
    public ResponseEntity<?> registerWishSale(@PathVariable("saleId") Long saleId) {
        try {
            saleService.registerWishSale(saleId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 매물입니다.");
        }
        return ResponseEntity.ok().build();
    }

    // 관심 매물 삭제
    @DeleteMapping("/{saleId}/wish/{wishSaleId}")
    public ResponseEntity<?> removeWishSale(@PathVariable("saleId") Long saleId,
                                            @PathVariable("wishSaleId") Long wishSaleId) {
        saleService.removeWishSale(saleId, wishSaleId);
        return ResponseEntity.ok().build();
    }

}

package com.ssafy.homescout.sale.controller;

import com.ssafy.homescout.annotation.Auth;
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
    public ResponseEntity<?> getMySaleList(@Auth Long userId) {
        return ResponseEntity.ok(saleService.getMySaleList(userId));
    }

    // 매물 등록
    @PostMapping
    public ResponseEntity<?> registerSale(@Auth Long userId,
                                          @Valid @RequestBody SaleRequestDto saleRequestDto) {
        return ResponseEntity.ok(saleService.registerSale(userId, saleRequestDto));
    }

    // 매물 수정
    @PutMapping("/{saleId}")
    public ResponseEntity<?> editSale(@PathVariable("saleId") Long saleId,
                                      @Auth Long userId,
                                      @RequestBody SaleEditRequestDto saleEditRequestDto) {
        return ResponseEntity.ok(saleService.editSale(saleId, userId, saleEditRequestDto));
    }

    // 매물 삭제
    @DeleteMapping("/{saleId}")
    public ResponseEntity<?> removeSale(@PathVariable("saleId") Long saleId,
                                        @Auth Long userId) {
        saleService.removeSale(saleId, userId);
        return ResponseEntity.ok().build();
    }

    // 관심 매물 리스트 조회
    @GetMapping("/wish")
    public ResponseEntity<?> getMyWishSaleList() {
        return ResponseEntity.ok(saleService.getMyWishSaleList());
    }

    // 관심 매물 등록
    @PostMapping("/{saleId}/wish")
    public ResponseEntity<?> registerWishSale(@PathVariable("saleId") Long saleId,
                                              @Auth Long userId) {
        saleService.registerWishSale(saleId, userId);
        return ResponseEntity.ok().build();
    }

    // 관심 매물 삭제
    @DeleteMapping("/{saleId}/wish")
    public ResponseEntity<?> removeWishSale(@PathVariable("saleId") Long saleId,
                                            @Auth Long userId) {
        saleService.removeWishSale(saleId, userId);
        return ResponseEntity.ok().build();
    }

}

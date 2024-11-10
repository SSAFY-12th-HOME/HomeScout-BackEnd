package com.ssafy.homescout.sale.service;

import com.ssafy.homescout.entity.Sale;
import com.ssafy.homescout.entity.WishSale;
import com.ssafy.homescout.sale.dto.SaleEditRequestDto;
import com.ssafy.homescout.sale.dto.SaleRequestDto;
import com.ssafy.homescout.sale.dto.SaleResponseDto;
import com.ssafy.homescout.sale.dto.WishSaleResponseDto;
import com.ssafy.homescout.sale.mapper.SaleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleService {

    private final SaleMapper saleMapper;

    public Sale registerSale(SaleRequestDto saleRequestDto) {
        Long userId = 1L;// TODO userId 수정

        Sale sale = Sale.builder()
                .aptId(saleRequestDto.getAptId())
                .userId(userId)
                .type(saleRequestDto.getType())
                .price(saleRequestDto.getPrice())
                .deposit(saleRequestDto.getDeposit())
                .rentalFee(saleRequestDto.getRentalFee())
                .dong(saleRequestDto.getDong())
                .floor(saleRequestDto.getFloor())
                .area(saleRequestDto.getArea())
                .tag1(saleRequestDto.getTag1())
                .tag2(saleRequestDto.getTag2())
                .tag3(saleRequestDto.getTag3())
                .build();

        saleMapper.insertSale(sale);

        return sale;
    }

    public List<SaleResponseDto> getMySaleList() {
        Long userId = 1L; // TODO userId 수정

        return saleMapper.selectSalesByUserId(userId);
    }

    public Sale editSale(Long saleId, SaleEditRequestDto saleEditRequestDto) {
        Long userId = 1L; // TODO userId 수정

        Sale sale = saleMapper.selectSaleBySaleId(saleId);
        if(sale == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 매물입니다.");
        }
        if(!sale.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인이 등록하지 않은 매물입니다.");
        }

        sale.setType(saleEditRequestDto.getType());
        sale.setPrice(saleEditRequestDto.getPrice());
        sale.setDeposit(saleEditRequestDto.getDeposit());
        sale.setRentalFee(saleEditRequestDto.getRentalFee());
        sale.setDong(saleEditRequestDto.getDong());
        sale.setFloor(saleEditRequestDto.getFloor());
        sale.setArea(saleEditRequestDto.getArea());
        sale.setTag1(saleEditRequestDto.getTag1());
        sale.setTag2(saleEditRequestDto.getTag2());
        sale.setTag3(saleEditRequestDto.getTag3());

        saleMapper.updateSale(sale);
        return sale;
    }

    public void removeSale(Long saleId) {
        Long userId = 1L; // TODO userId 수정

        Sale sale = saleMapper.selectSaleBySaleId(saleId);
        if(sale == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 매물입니다.");
        }
        if(!sale.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인이 등록하지 않은 매물입니다.");
        }

        saleMapper.deleteSale(saleId);
    }

    public void registerWishSale(Long saleId) {
        Long userId = 1L; // TODO userId 수정

        saleMapper.insertWishSale(saleId, userId);
    }

    public void removeWishSale(Long saleId, Long wishSaleId) {
        Long userId = 1L;

        WishSale wishSale = saleMapper.selectWishSaleById(wishSaleId);
        if(wishSale == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 관심 매물입니다.");
        }
        if(!wishSale.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인이 등록하지 않은 관심 매물입니다.");
        }
        if(!wishSale.getSaleId().equals(saleId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "매물 등록 번호와 일치하지 않습니다.");
        }

        saleMapper.deleteWishSale(wishSaleId);
    }

    public List<WishSaleResponseDto> getMyWishSaleList() {
        Long userId = 1L;

        return saleMapper.selectWishSalesByUserId(userId);
    }
}

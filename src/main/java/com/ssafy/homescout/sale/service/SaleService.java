package com.ssafy.homescout.sale.service;

import com.ssafy.homescout.entity.Sale;
import com.ssafy.homescout.sale.dto.SaleRequestDto;
import com.ssafy.homescout.sale.mapper.SaleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

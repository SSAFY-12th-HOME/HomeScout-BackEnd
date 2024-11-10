package com.ssafy.homescout.sale.mapper;

import com.ssafy.homescout.entity.Sale;
import com.ssafy.homescout.sale.dto.SaleResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SaleMapper {

    void insertSale(Sale sale);

    List<SaleResponseDto> selectSalesByUserId(Long userId);

    Sale selectSaleBySaleId(Long saleId);

    void updateSale(Sale sale);

    void deleteSale(Long saleId);
}

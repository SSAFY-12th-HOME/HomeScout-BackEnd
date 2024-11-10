package com.ssafy.homescout.sale.mapper;

import com.ssafy.homescout.entity.Sale;
import com.ssafy.homescout.entity.WishSale;
import com.ssafy.homescout.sale.dto.SaleResponseDto;
import com.ssafy.homescout.sale.dto.WishSaleResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SaleMapper {

    void insertSale(Sale sale);

    List<SaleResponseDto> selectSalesByUserId(Long userId);

    Sale selectSaleBySaleId(Long saleId);

    void updateSale(Sale sale);

    void deleteSale(Long saleId);

    void insertWishSale(@Param("saleId") Long saleId, @Param("userId") Long userId);

    void deleteWishSale(Long wishSaleId);

    WishSale selectWishSaleById(Long wishSaleId);

    List<WishSaleResponseDto> selectWishSalesByUserId(Long userId);
}

package com.ssafy.homescout.sale.mapper;

import com.ssafy.homescout.entity.Sale;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SaleMapper {

    void insertSale(Sale sale);

}

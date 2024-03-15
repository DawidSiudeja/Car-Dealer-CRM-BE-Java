package com.example.cardealer.translator;

import com.example.cardealer.entity.car.CarForSale;
import com.example.cardealer.entity.car.CarForSaleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public abstract class CarForSaleDtoToCarForSale {

    public CarForSale toCarForSale(CarForSaleDTO carForSaleDTO) {
        return translateToCarForSale(carForSaleDTO);
    }

    @Mappings({
            @Mapping(target = "imageFile", ignore = true)
    })
    protected abstract CarForSale translateToCarForSale(CarForSaleDTO carForSaleDTO);
}

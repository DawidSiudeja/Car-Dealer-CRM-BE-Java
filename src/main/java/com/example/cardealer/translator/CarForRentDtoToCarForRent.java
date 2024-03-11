package com.example.cardealer.translator;

import com.example.cardealer.entity.car.CarForRent;
import com.example.cardealer.entity.car.CarForRentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper
public abstract class CarForRentDtoToCarForRent {

    public CarForRent toCarForRent(CarForRentDTO carForRentDTO) {
        return translateToCarForRent(carForRentDTO);
    }

    @Mappings({})
    protected abstract CarForRent translateToCarForRent(CarForRentDTO carForRentDTO);
}

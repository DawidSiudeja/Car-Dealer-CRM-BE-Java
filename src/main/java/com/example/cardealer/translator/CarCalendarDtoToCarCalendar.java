package com.example.cardealer.translator;

import com.example.cardealer.entity.car.CarCalendar;
import com.example.cardealer.entity.car.CarCalendarDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper
public abstract class CarCalendarDtoToCarCalendar {

    public CarCalendar toCarCalendar(CarCalendarDTO carCalendarDTO) {
        return translateToCarCalendar(carCalendarDTO);
    }

    @Mappings({})
    protected abstract CarCalendar translateToCarCalendar(CarCalendarDTO carCalendarDTO);
}

package com.example.cardealer.repository;

import com.example.cardealer.entity.car.CarCalendar;
import com.example.cardealer.entity.car.CarForRent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarCalendarRepository extends JpaRepository<CarCalendar, Long> {

    List<CarCalendar> findByCarForRent(CarForRent carForRent);

}

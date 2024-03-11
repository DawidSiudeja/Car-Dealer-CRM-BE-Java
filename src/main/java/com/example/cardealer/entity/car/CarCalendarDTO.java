package com.example.cardealer.entity.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarCalendarDTO {
    private LocalDate startRent;
    private LocalDate endRent;
    private Integer carId;
}

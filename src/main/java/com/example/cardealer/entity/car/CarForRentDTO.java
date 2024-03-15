package com.example.cardealer.entity.car;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CarForRentDTO {
    private String brand;
    private String model;
    private Integer car_year;
    private String color;
    private String engine;
    private Integer mileage;
    private String type;
    private String gearbox;
    private Integer doors;
    private Integer seats;
    private String descHtml;
    private Integer seller;
    private float pricePerHour;
    private float pricePerDay;
    private float pricePerWeek;
    private float pricePerMonth;
    private List<String> imageFile;
}


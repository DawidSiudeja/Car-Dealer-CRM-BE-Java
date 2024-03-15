package com.example.cardealer.entity.car;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Car {
    private String uuid;
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
    private LocalDate createAt;
    private Integer seller;
    private List<String> imageFile;
}

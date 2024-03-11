package com.example.cardealer.entity.car;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "car_for_rent")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class CarForRent extends Car {
    @Id
    @GeneratedValue(generator = "car_for_rent_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "car_for_rent_id_seq", sequenceName = "car_for_rent_id_seq", allocationSize = 1)
    private long id;
    private float pricePerHour;
    private float pricePerDay;
    private float pricePerWeek;
    private float pricePerMonth;

    public CarForRent(
            long id,
            String uuid,
            String brand,
            String model,
            Integer car_year,
            String color,
            String engine,
            Integer mileage,
            String type,
            String gearbox,
            Integer doors,
            Integer seats,
            String desc,
            LocalDate createAt,
            Integer seller,
            float pricePerHour,
            float pricePerDay,
            float pricePerWeek,
            float pricePerMonth
    ) {
        super(uuid, brand, model, car_year, color, engine, mileage, type, gearbox, doors, seats, desc, createAt, seller);
        this.pricePerHour = pricePerHour;
        this.pricePerDay = pricePerDay;
        this.pricePerWeek = pricePerWeek;
        this.pricePerMonth = pricePerMonth;
        this.id = id;
    }
}

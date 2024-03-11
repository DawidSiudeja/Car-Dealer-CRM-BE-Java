package com.example.cardealer.entity.car;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "car_for_sale")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class CarForSale extends Car {
    @Id
    @GeneratedValue(generator = "car_for_sale_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "car_for_sale_id_seq", sequenceName = "car_for_sale_id_seq", allocationSize = 1)
    private long id;
    private float price;

    public CarForSale(
             long id,
             String uuid,
             String brand,
             String model,
             Integer year,
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
             float price
    ) {
        super(uuid, brand, model, year, color, engine, mileage, type, gearbox, doors, seats, desc, createAt, seller);
        this.price = price;
        this.id = id;
    }

}

package com.example.cardealer.entity.car;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "car_calendar")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class CarCalendar {
    @Id
    @GeneratedValue(generator = "car_calendar_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "car_calendar_id_seq", sequenceName = "car_calendar_id_seq", allocationSize = 1)
    private long id;
    private String uuid;
    private LocalDate startRent;
    private LocalDate endRent;
    @ManyToOne
    @JoinColumn(name = "car_for_rent")
    private CarForRent carForRent;
}

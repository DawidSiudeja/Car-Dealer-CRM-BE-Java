package com.example.cardealer.repository;

import com.example.cardealer.entity.car.CarForRent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarForRentRepository extends JpaRepository<CarForRent, Long> {
    List<CarForRent> findBySeller(Integer seller);

    Optional<CarForRent> findByUuid(String uuid);

    Optional<CarForRent> findById(Integer id);
}

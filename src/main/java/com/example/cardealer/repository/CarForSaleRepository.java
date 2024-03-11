package com.example.cardealer.repository;

import com.example.cardealer.entity.car.CarForSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CarForSaleRepository extends JpaRepository<CarForSale, Long> {

    List<CarForSale> findBySeller(long seller);

    Optional<CarForSale> findByUuid(String uuid);

}

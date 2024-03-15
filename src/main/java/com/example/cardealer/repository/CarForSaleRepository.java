package com.example.cardealer.repository;

import com.example.cardealer.entity.car.CarForSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CarForSaleRepository extends JpaRepository<CarForSale, Long> {

    List<CarForSale> findBySeller(long seller);

    Optional<CarForSale> findByUuid(String uuid);

    @Query(value = "SELECT nextval('car_for_sale_id_seq')", nativeQuery = true)
    Long getNextId();

}

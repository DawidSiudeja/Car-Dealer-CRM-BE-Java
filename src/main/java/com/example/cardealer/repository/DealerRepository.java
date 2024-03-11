package com.example.cardealer.repository;

import com.example.cardealer.entity.auth.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {

    Optional<Dealer> findDealerByNip(String nip);

    Optional<Dealer> findDealerById(Integer id);

    Optional<Dealer> findDealerByUuid(String uuid);

}

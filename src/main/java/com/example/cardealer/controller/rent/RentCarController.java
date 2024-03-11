package com.example.cardealer.controller.rent;

import com.example.cardealer.entity.car.CarCalendarDTO;
import com.example.cardealer.entity.car.CarForRent;
import com.example.cardealer.entity.car.CarForRentDTO;
import com.example.cardealer.service.RentCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/rent")
@RequiredArgsConstructor
public class RentCarController {

    private final RentCarService rentCarService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getCarsForRent(@RequestParam String seller) {
        return rentCarService.getCarsForRent(seller);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createCarForSale(@RequestBody CarForRentDTO carForRentDTO) {
        return rentCarService.createCarForRent(carForRentDTO);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> editCarForRent(@RequestBody CarForRent carForRent) {
        return rentCarService.editCarForRent(carForRent);
    }

    @RequestMapping(path = "/order", method = RequestMethod.POST)
    public ResponseEntity<?> rentCar(@RequestBody CarCalendarDTO carCalendarDTO) {
        return rentCarService.rentCar(carCalendarDTO);
    }
}

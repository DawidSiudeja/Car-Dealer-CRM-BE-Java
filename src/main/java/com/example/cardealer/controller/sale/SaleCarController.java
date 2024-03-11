package com.example.cardealer.controller.sale;

import com.example.cardealer.entity.car.CarForSale;
import com.example.cardealer.entity.car.CarForSaleDTO;
import com.example.cardealer.entity.car.SendCarOfferDTO;
import com.example.cardealer.entity.contract.SaleContractDTO;
import com.example.cardealer.service.EmailService;
import com.example.cardealer.service.SaleCarService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/sale")
@RequiredArgsConstructor
public class SaleCarController {
    private final SaleCarService saleCarService;
    private final EmailService emailService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CarForSale>> getCarsForSale(HttpServletRequest request, HttpServletResponse response) {
        return saleCarService.getCarsForeSale(request, response);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createCarForSale(@RequestBody CarForSaleDTO carForSaleDTO) {
        return saleCarService.createCarForSale(carForSaleDTO);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> editCarForSale(@RequestBody CarForSale carForSale) {
        return saleCarService.editCarForSale(carForSale);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/generate/contract")
    public ResponseEntity<?> generateSaleContract(@RequestBody SaleContractDTO saleContractDto) throws Exception {
        return saleCarService.generateSaleContract(saleContractDto);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/offer")
    public ResponseEntity<?> sendCarOffer(@RequestBody SendCarOfferDTO sendCarOfferDTO) {
        return emailService.sendCarOffer(sendCarOfferDTO);
    }
}

package com.example.cardealer.controller.sale;

import com.example.cardealer.entity.car.*;
import com.example.cardealer.entity.contract.SaleContractDTO;
import com.example.cardealer.service.EmailService;
import com.example.cardealer.service.ImageService;
import com.example.cardealer.service.SaleCarService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/sale")
@RequiredArgsConstructor
public class SaleCarController {
    private final SaleCarService saleCarService;
    private final EmailService emailService;
    private final ImageService imageService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CarForSale>> getCarsForSale(HttpServletRequest request, HttpServletResponse response) {
        return saleCarService.getCarsForeSale(request, response);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createCarForSale(@ModelAttribute CarForSaleDTO carForSaleDTO) {
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

    @RequestMapping(method = RequestMethod.PUT, path = "/sold")
    public ResponseEntity<?> setCarSold(@RequestBody CarSoldDTO carSoldDTO) {
        return saleCarService.setCarSold(carSoldDTO);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/image")
    public ResponseEntity<?> deleteImage(@RequestBody CarImageDTO carImageDTO) {
        return imageService.deleteImage(carImageDTO);
    }
}

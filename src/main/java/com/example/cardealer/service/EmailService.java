package com.example.cardealer.service;

import com.example.cardealer.configuration.EmailConfiguration;
import com.example.cardealer.entity.auth.Dealer;
import com.example.cardealer.entity.car.CarForSale;
import com.example.cardealer.entity.car.SendCarOfferDTO;
import com.example.cardealer.repository.CarForSaleRepository;
import com.example.cardealer.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailConfiguration emailConfiguration;
    private final CarForSaleRepository carForSaleRepository;
    private final DealerRepository dealerRepository;

    @Value("classpath:static/offer-car-mail.html")
    private Resource offerCarMail;

    public ResponseEntity<?> sendCarOffer(SendCarOfferDTO sendCarOfferDTO) {

        AtomicReference<CarForSale> carForSale = new AtomicReference<>(new CarForSale());
        carForSaleRepository.findByUuid(sendCarOfferDTO.getCarUuid()).ifPresent( value -> {
            carForSale.set(value);
        });

        AtomicReference<Dealer> dealer = new AtomicReference<>(new Dealer());
        dealerRepository.findDealerByUuid(sendCarOfferDTO.getDealerUuid()).ifPresent( value -> {
            dealer.set(value);
        });

        String newPrice = sendCarOfferDTO.getNewPrice();
        String clientEmail = sendCarOfferDTO.getClientEmail();

        try {
            InputStream inputStream = offerCarMail.getInputStream();
            String html = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            html = html.replace("%CAR_MODEL%", carForSale.get().getModel());
            html = html.replace("%CAR_BRAND%", carForSale.get().getBrand());
            html = html.replace("%CAR_YEAR%", String.valueOf(carForSale.get().getCar_year()));
            html = html.replace("%CAR_MILEAGE%", String.valueOf(carForSale.get().getMileage()));
            html = html.replace("%CAR_PRICE%", String.valueOf(carForSale.get().getPrice()));
            html = html.replace("%NEW_PRICE%", newPrice);
            html = html.replace("%DEALER_PHONE%", dealer.get().getTelephone1());
            html = html.replace("%DEALER_NIP%", dealer.get().getNip());
            html = html.replace("%DEALER_NAME%", dealer.get().getCompanyName());
            html = html.replace("%DEALER_ADDRESS%", dealer.get().getAddress());

            String subject =
                    carForSale.get().getBrand() + " " +
                    carForSale.get().getModel() + " " +
                    carForSale.get().getCar_year() + "r. " +
                    newPrice + " " +
                    dealer.get().getCompanyName();

            emailConfiguration.sendEmail(clientEmail, html, subject, true);

            return ResponseEntity.ok().body("Poprawnie wysłano maila");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

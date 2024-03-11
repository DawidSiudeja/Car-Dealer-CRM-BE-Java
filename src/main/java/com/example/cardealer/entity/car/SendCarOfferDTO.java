package com.example.cardealer.entity.car;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendCarOfferDTO {

    private String carUuid;
    private String newPrice;
    private String clientEmail;
    private String dealerUuid;

}

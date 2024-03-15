package com.example.cardealer.entity.car;

import com.example.cardealer.entity.auth.Dealer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarImageDTO {

    private String dealerUuid;
    private String carUuid;
    private Integer photoNumber;

}

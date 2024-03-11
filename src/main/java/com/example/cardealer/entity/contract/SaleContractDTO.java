package com.example.cardealer.entity.contract;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleContractDTO {
    private String dealerUuid;
    private String carUuid;
}

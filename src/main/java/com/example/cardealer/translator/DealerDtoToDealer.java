package com.example.cardealer.translator;

import com.example.cardealer.entity.auth.Dealer;
import com.example.cardealer.entity.auth.DealerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper
public abstract class DealerDtoToDealer {

    public Dealer toDealer(DealerDTO dealerDTO) {
        return translateToDealer(dealerDTO);
    }

    @Mappings({})
    protected abstract Dealer translateToDealer(DealerDTO dealerDTO);

}

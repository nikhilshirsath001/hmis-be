package com.suma.hmis_service.config;

import com.suma.hmis_service.entities.Billing;
import com.suma.hmis_service.feature.infra.bed.Bed;
import com.suma.hmis_service.feature.infra.bed.BedResponse;
import com.suma.hmis_service.models.billing.BillingDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig  {
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper =  new ModelMapper();
        // Only BillingDto -> Billing
        modelMapper.typeMap(BillingDto.class, Billing.class)
                .addMappings(mapper -> {
                    mapper.skip(Billing::setId);
                });

        modelMapper.createTypeMap(Bed.class, BedResponse.class)
                .addMapping(src -> src.getRoom().getId(), BedResponse::setRoomId)
                .addMapping(src -> src.getRoom().getCode(), BedResponse::setRoomCode)
                .addMapping(src -> src.getRoom().getName(), BedResponse::setRoomName)
                .addMapping(src -> src.getRoom().getWard().getId(), BedResponse::setWardId)
                .addMapping(src -> src.getRoom().getWard().getCode(), BedResponse::setWardCode)
                .addMapping(src -> src.getRoom().getWard().getName(), BedResponse::setWardName);

        return modelMapper;
    }


}

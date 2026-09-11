package com.suma.hmis_service.config;

import com.suma.hmis_service.entities.Billing;
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

        return modelMapper;
    }


}

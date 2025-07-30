package com.example.appointmentservice.feign;

import com.example.appointmentservice.model.dto.businessDto.BusinessDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "business-service", url = "${business-service.url}")
public interface BusinessServiceClient {

    @GetMapping("/api/businesses/{id}/exists")
    ResponseEntity<Boolean> doesBusinessExistById(@PathVariable Long id);


    @GetMapping("/api/businesses/{id}")
    ResponseEntity<BusinessDto> getBusinessById(@PathVariable Long id);
}

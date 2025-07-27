package com.example.appointmentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "business-auth-service", url = "${business-auth-service.url}")
public interface CustomerFeign {
    @GetMapping("/{id}")
    ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id);
}

package com.example.appointmentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${customer-service.url}")
public interface CustomerServiceClient {

    @GetMapping("/api/customers/exists/{id}")
    ResponseEntity<Boolean> doesCustomerExistByCustomerId(@PathVariable Long id);
}

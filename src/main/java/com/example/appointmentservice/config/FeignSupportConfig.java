//package com.example.appointmentservice.config;
//
//import feign.codec.Decoder;
//import feign.optionals.OptionalDecoder;
//import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
//import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
//import org.springframework.cloud.openfeign.support.SpringDecoder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
////import org.springframework.http.converter.HttpMessageConverters;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//
//@Configuration
//public class FeignSupportConfig {
//
//    @Bean
//    public Decoder feignDecoder() {
//        return new OptionalDecoder(
//                new ResponseEntityDecoder(
//                        new SpringDecoder(() ->
//                                new HttpMessageConverters(new MappingJackson2HttpMessageConverter()))
//                )
//        );
//    }
//}

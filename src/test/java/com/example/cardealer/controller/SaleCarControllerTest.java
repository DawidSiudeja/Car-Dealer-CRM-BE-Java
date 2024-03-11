package com.example.cardealer.controller;

import com.example.cardealer.CarDealerManagmentSystemApplication;
import com.example.cardealer.controller.helper.HttpHelper;
import com.example.cardealer.entity.Code;
import com.example.cardealer.entity.auth.AuthResponse;
import com.example.cardealer.entity.auth.User;
import com.example.cardealer.entity.car.CarForSale;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes = CarDealerManagmentSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SaleCarControllerTest {

    private static final String CAR_SALE = "http://localhost:8080/api/v1/sale";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test_1_should_response_dealer_car_list() {
        //Given
        String url = String.format(CAR_SALE);
        HttpHeaders carHeaders = new HttpHeaders();
        carHeaders.put(HttpHeaders.COOKIE, Arrays.asList("Dealer=4a61186e-b423-41e1-b936-218156f15c65"));
        HttpEntity<String> request = new HttpEntity<>(carHeaders);
        //When
        ParameterizedTypeReference<List<CarForSale>> responseType = new ParameterizedTypeReference<List<CarForSale>>() {};
        ResponseEntity<List<CarForSale>> response = testRestTemplate.exchange(url, HttpMethod.GET, request, responseType);
        //Then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().get(0).getSeller(), 1);
    }
}

package com.example.cardealer.controller;

import com.example.cardealer.CarDealerManagmentSystemApplication;
import com.example.cardealer.controller.helper.HttpHelper;
import com.example.cardealer.entity.Code;
import com.example.cardealer.entity.auth.AuthResponse;
import com.example.cardealer.entity.auth.User;
import com.example.cardealer.entity.auth.UserRegisterDTO;
import com.example.cardealer.service.UserService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes = CarDealerManagmentSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    private static final String USER_LOGIN = "http://localhost:8080/api/v1/auth/login";
    private static final String USER_LOGOUT = "http://localhost:8080/api/v1/auth/logout";
    private static final String USER_VALIDATE = "http://localhost:8080/api/v1/auth/validate";
    private static final String DATA_USER_LOGIN = "xmaster";
    private static final String DATA_USER_PASSWORD = "dupadupa";

    @Mock
    private UserService userService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_1_should_login_user() {
        //Given
        String url = String.format(USER_LOGIN);
        User createRequest = new User();
        createRequest.setLogin(DATA_USER_LOGIN);
        createRequest.setPassword(DATA_USER_PASSWORD);
        HttpEntity<User> request = HttpHelper.getHttpEntity(createRequest);
        //When
        ResponseEntity<AuthResponse> response = testRestTemplate.exchange(url, HttpMethod.POST, request, AuthResponse.class);
        //Then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getMessage(), Code.SUCCESS.label);
        assertEquals(response.getBody().getCode(), Code.SUCCESS);
    }

    @Test
    public void test_2_should_not_login_user_wrong_data() {
        //Given
        String url = String.format(USER_LOGIN);
        User createRequest = new User();
        createRequest.setLogin("wrong_login");
        createRequest.setPassword("wrong_password");
        HttpEntity<User> request = HttpHelper.getHttpEntity(createRequest);
        //When
        ResponseEntity<AuthResponse> response = testRestTemplate.exchange(url, HttpMethod.POST, request, AuthResponse.class);
        //Then
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getMessage(), Code.A2.label);
        assertEquals(response.getBody().getCode(), Code.A2);
    }

    @Test
    public void test_3_should_user_logout() {
        // Given - Login user
        String loginUrl = String.format(USER_LOGIN);
        User loginUser = new User();
        loginUser.setLogin(DATA_USER_LOGIN);
        loginUser.setPassword(DATA_USER_PASSWORD);
        HttpEntity<User> loginRequest = HttpHelper.getHttpEntity(loginUser);

        ResponseEntity<AuthResponse> loginResponse = testRestTemplate.exchange(loginUrl, HttpMethod.POST, loginRequest, AuthResponse.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        assertEquals(Code.SUCCESS.label, loginResponse.getBody().getMessage());
        assertEquals(Code.SUCCESS, loginResponse.getBody().getCode());

        // Get cookies from the response
        HttpHeaders headers = loginResponse.getHeaders();
        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);

        // Ensure cookies are present
        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());

        // Logout user using cookies
        String logoutUrl = String.format(USER_LOGOUT);
        HttpHeaders logoutHeaders = new HttpHeaders();
        logoutHeaders.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<String> logoutRequest = new HttpEntity<>(logoutHeaders);

        ResponseEntity<AuthResponse> logoutResponse = testRestTemplate.exchange(logoutUrl, HttpMethod.GET, logoutRequest, AuthResponse.class);

        // Then - Assert logout response
        assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());
        assertNotNull(logoutResponse.getBody());
        assertEquals(logoutResponse.getBody().getMessage(), Code.SUCCESS.label);
        assertEquals(logoutResponse.getBody().getCode(), Code.SUCCESS);
    }


    @Test
    public void test_4_user_register()  {
        // Given
        UserRegisterDTO registrationRequest = new UserRegisterDTO();
        registrationRequest.setLogin("exampleLogin");
        registrationRequest.setEmail("example@example.com");
        registrationRequest.setPassword("examplePassword");
        registrationRequest.setDealer(1);

        when(userService.register(any(UserRegisterDTO.class)))
                .thenReturn(ResponseEntity.ok(new AuthResponse(Code.SUCCESS)));

        // when
        ResponseEntity<AuthResponse> mockResponse = userService.register(registrationRequest);

        // Then
        assertEquals(mockResponse.getBody().getMessage(), Code.SUCCESS.label);
        assertEquals(mockResponse.getBody().getCode(), Code.SUCCESS);

    }

    @Test
    public void test_5_check_user_is_logged() {
        //Given
        String validateUrl = String.format(USER_VALIDATE);
        HttpHeaders logoutHeaders = new HttpHeaders();
        HttpEntity<String> logoutRequest = new HttpEntity<>(logoutHeaders);
        //When
        ResponseEntity<AuthResponse> response = testRestTemplate.exchange(validateUrl, HttpMethod.GET, logoutRequest, AuthResponse.class);
        //Then
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getMessage(), Code.A3.label);
        assertEquals(response.getBody().getCode(), Code.A3);

        //Login
        String loginUrl = String.format(USER_LOGIN);
        User loginUser = new User();
        loginUser.setLogin(DATA_USER_LOGIN);
        loginUser.setPassword(DATA_USER_PASSWORD);
        HttpEntity<User> loginRequest = HttpHelper.getHttpEntity(loginUser);

        ResponseEntity<AuthResponse> loginResponse = testRestTemplate.exchange(loginUrl, HttpMethod.POST, loginRequest, AuthResponse.class);

        List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        HttpHeaders validateHeaders = new HttpHeaders();
        validateHeaders.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<User> validateRequest = new HttpEntity<>(validateHeaders);

        ResponseEntity<AuthResponse> validateResponse = testRestTemplate.exchange(validateUrl, HttpMethod.GET, validateRequest, AuthResponse.class);

        //Then
        assertEquals(validateResponse.getStatusCode(), HttpStatus.OK);
        assertNotNull(validateResponse.getBody());
        assertEquals(validateResponse.getBody().getMessage(), Code.PERMIT.label);
        assertEquals(validateResponse.getBody().getCode(), Code.PERMIT);
    }

}

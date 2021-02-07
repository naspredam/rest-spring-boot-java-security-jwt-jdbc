package com.example.service.simple.jwt.authentication.infrastructure.controller;

import com.example.service.simple.jwt.authentication.infrastructure.controller.dto.LoginRequestBodyDto;
import com.example.service.simple.jwt.authentication.infrastructure.controller.dto.LoginResponseDto;
import com.example.service.simple.jwt.authentication.infrastructure.repository.UserRepository;
import com.example.service.simple.jwt.authentication.infrastructure.repository.data.UserData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static com.example.service.simple.jwt.authentication.util.DataFaker.fakeNewUserData;
import static com.example.service.simple.jwt.authentication.util.DataFaker.fakePassword;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserSecurityControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    public void shouldReturnUnauthorized_whenUserIsNotRegistered() throws Exception {
        LoginRequestBodyDto bodyDto = LoginRequestBodyDto.of("user", "pwd");

        mvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(bodyDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnToken_forGivenLoginUser_Registered() throws Exception {
        String password = fakePassword();
        UserData userData = fakeNewUserData(password);
        userRepository.save(userData);

        LoginRequestBodyDto bodyDto = LoginRequestBodyDto.of(userData.getUsername(), password);
        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(bodyDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("token", is(not(empty()))));
    }

    @Test
    public void shouldReturnUnauthorized_whenNoTokenIsProvided() throws Exception {
        mvc.perform(get("/logout"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldLogoutUser_RequiringToken() throws Exception {
        String password = fakePassword();
        UserData userData = fakeNewUserData(password);
        userRepository.save(userData);

        LoginRequestBodyDto bodyDto = LoginRequestBodyDto.of(userData.getUsername(), password);
        MvcResult mvcResult = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(bodyDto)))
                .andReturn();
        LoginResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), LoginResponseDto.class);
        String token = responseDto.getToken();

        mvc.perform(get("/logout")
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNoContent());

    }
}
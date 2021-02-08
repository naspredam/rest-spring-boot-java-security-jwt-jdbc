package com.example.service.simple.jwt.authentication.infrastructure.controller;

import com.example.service.simple.jwt.authentication.infrastructure.controller.dto.CreateUserBodyDto;
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

import static com.example.service.simple.jwt.authentication.util.DataFaker.fakeCreateUser;
import static com.example.service.simple.jwt.authentication.util.DataFaker.fakeNewUserData;
import static com.example.service.simple.jwt.authentication.util.DataFaker.fakePassword;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    public void shouldSaveNewUser() throws Exception {
        CreateUserBodyDto bodyDto = fakeCreateUser();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(bodyDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(not(nullValue()))))
                .andExpect(jsonPath("username", is(bodyDto.getUsername())))
                .andExpect(jsonPath("nationalId", is(bodyDto.getNationalId())))
                .andExpect(jsonPath("firstName", is(bodyDto.getFirstName())))
                .andExpect(jsonPath("lastName", is(bodyDto.getLastName())))
                .andExpect(jsonPath("phone", is(bodyDto.getPhone())));
    }

    @Test
    public void shouldReturnLoggedUserInformation() throws Exception {
        String password = fakePassword();
        UserData userData = fakeNewUserData(password);
        UserData savedUserData = userRepository.save(userData);

        LoginRequestBodyDto bodyDto = LoginRequestBodyDto.of(userData.getUsername(), password);
        MvcResult mvcResult = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(bodyDto)))
                .andReturn();
        LoginResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), LoginResponseDto.class);
        String token = responseDto.getToken();

        mvc.perform(get("/me")
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(savedUserData.getId().intValue())))
                .andExpect(jsonPath("username", is(savedUserData.getUsername())))
                .andExpect(jsonPath("nationalId", is(savedUserData.getNationalId())))
                .andExpect(jsonPath("firstName", is(savedUserData.getFirstName())))
                .andExpect(jsonPath("lastName", is(savedUserData.getLastName())))
                .andExpect(jsonPath("phone", is(savedUserData.getPhone())));
    }
}

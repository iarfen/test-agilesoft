package com.agilesoftTest.AgilesoftTest;

import com.agilesoftTest.dao.UsersDAO;
import com.agilesoftTest.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersDAO usersDAO;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @WithAnonymousUser
    public void givenUserObject_whenCreateUser_thenReturnSavedUser() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("thomas1")
                .password("pass1")
                .name("Thomas")
                .build();
        when(usersDAO.save(user)).thenReturn(user);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = { "USER" })
    public void givenUserId_whenGetUserById_thenReturnUserObject() throws Exception {
        Long userId = 1L;
        User user = User.builder()
                .id(1L)
                .username("thomas1")
                .password("pass1")
                .name("Thomas")
                .build();
        when(usersDAO.findById(userId)).thenReturn(Optional.of(user));

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("userId", 1L);
        ResultActions response = mockMvc.perform(get("/current-user", userId).session(mockHttpSession));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.name", is(user.getName())));
    }
}

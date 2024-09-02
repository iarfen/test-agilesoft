package com.agilesoftTest.AgilesoftTest;

import com.agilesoftTest.dao.UsersDAO;
import com.agilesoftTest.dto.SessionDTO;
import com.agilesoftTest.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class SessionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersDAO usersDAO;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpSession httpSession;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @WithAnonymousUser
    public void givenUserList_whenLogin_thenStoreUserId() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("thomas1")
                .password("pass1")
                .name("Thomas")
                .build();
        List<User> usersList = (List<User>) Arrays.asList(user);
        when(usersDAO.findAll()).thenReturn(usersList);

        SessionDTO sessionDTO = SessionDTO.builder()
                .username("thomas1")
                .password("pass1")
                .build();

        MockHttpSession mockHttpSession = new MockHttpSession();
        ResultActions response = mockMvc.perform(post("/login", sessionDTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDTO))
                .session(mockHttpSession));

        response.andDo(print())
                .andExpect(status().isOk());

        Assert.assertEquals(1L, mockHttpSession.getAttribute("userId"));
    }
}

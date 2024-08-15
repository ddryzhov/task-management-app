package mate.academy.taskmanagement.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;
import mate.academy.taskmanagement.dto.user.UpdateUserProfileRequestDto;
import mate.academy.taskmanagement.dto.user.UpdateUserRoleRequestDto;
import mate.academy.taskmanagement.dto.user.UserProfileResponseDto;
import mate.academy.taskmanagement.dto.user.UserResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setup(@Autowired DataSource dataSource) throws Exception {
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/users/add-users.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/roles/add-users-roles.sql"
        );
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws Exception {
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/roles/remove-users-roles.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/users/remove-users.sql"
        );
    }

    private void executeSqlScript(DataSource dataSource, String scriptPath) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(scriptPath));
        }
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Get the profile of the currently logged-in user")
    public void getMyProfile_ShouldReturnUserProfile() throws Exception {
        UserProfileResponseDto expected = new UserProfileResponseDto(
                1L,
                "user",
                "user@example.com",
                "First",
                "Last",
                new HashSet<>(Set.of("ROLE_USER"))
        );

        MvcResult result = mockMvc.perform(
                get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserProfileResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), UserProfileResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    @Test
    @DisplayName("Update user role with valid input")
    public void updateUserRole_ValidRequestDto_ShouldReturnUpdatedUser() throws Exception {
        UpdateUserRoleRequestDto requestDto = new UpdateUserRoleRequestDto()
                .setRoleName("ROLE_USER");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/users/1/role")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserProfileResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), UserProfileResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("ROLE_USER", actual.roles().iterator().next());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Update the profile of the currently logged-in user with valid input")
    public void updateMyProfile_ValidRequestDto_ShouldReturnUpdatedUser() throws Exception {
        UpdateUserProfileRequestDto requestDto = new UpdateUserProfileRequestDto(
                "UpdatedFirst",
                "UpdatedLast",
                "updateduser@example.com"
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/users/me")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), UserResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(requestDto.firstName(), actual.firstName());
        Assertions.assertEquals(requestDto.lastName(), actual.lastName());
        Assertions.assertEquals(requestDto.login(), actual.login());
    }
}

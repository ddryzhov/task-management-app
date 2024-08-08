package mate.academy.taskmanagement.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import javax.sql.DataSource;
import mate.academy.taskmanagement.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagement.dto.label.LabelResponseDto;
import mate.academy.taskmanagement.dto.label.UpdateLabelRequestDto;
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
public class LabelControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSource dataSource;

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
    void setup() throws Exception {
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/users/add-users.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/projects/add-projects.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/tasks/add-tasks.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/labels/add-labels.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/labels/add-task-labels.sql"
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/labels/remove-task-labels.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/labels/remove-labels.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/tasks/remove-tasks.sql"
        );
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/projects/remove-projects.sql"
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
    @DisplayName("Create a new label")
    public void createLabel_ValidRequestDto_ShouldReturnCreatedLabel() throws Exception {
        CreateLabelRequestDto requestDto = new CreateLabelRequestDto("New Label", "blue", 2L);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/labels")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        LabelResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                LabelResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("New Label", actual.name());
        Assertions.assertEquals("blue", actual.color());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Retrieve all labels")
    public void getAllLabels_ValidRequest_ShouldReturnLabelList() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/labels")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        LabelResponseDto[] labels = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), LabelResponseDto[].class);
        Assertions.assertTrue(labels.length > 0);
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Retrieve a label by its ID")
    public void getLabelById_ValidLabelId_ShouldReturnLabel() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/labels/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        LabelResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                LabelResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Existing Label", actual.name());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Update an existing label")
    public void updateLabel_ValidRequestDto_ShouldReturnUpdatedLabel() throws Exception {
        UpdateLabelRequestDto requestDto = new UpdateLabelRequestDto()
                .setName("Updated Label")
                .setColor("red");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/labels/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        LabelResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                LabelResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Updated Label", actual.name());
        Assertions.assertEquals("red", actual.color());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Delete a label by its ID")
    public void deleteLabel_ValidLabelId_ShouldReturnNoContent() throws Exception {
        executeSqlScript(dataSource,
                "mate/academy/taskmanagement/database/labels/remove-task-labels.sql");
        mockMvc.perform(
                        delete("/labels/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }
}

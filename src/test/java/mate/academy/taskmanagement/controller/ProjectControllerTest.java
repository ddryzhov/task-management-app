package mate.academy.taskmanagement.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.time.LocalDate;
import javax.sql.DataSource;
import mate.academy.taskmanagement.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagement.dto.project.ProjectResponseDto;
import mate.academy.taskmanagement.dto.project.UpdateProjectRequestDto;
import mate.academy.taskmanagement.model.Project;
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
public class ProjectControllerTest {
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
                "mate/academy/taskmanagement/database/projects/add-projects.sql"
        );
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws Exception {
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
    @DisplayName("Create a new project")
    public void createProject_ValidRequestDto_ShouldReturnCreatedProject() throws Exception {
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto()
                .setName("New Project")
                .setDescription("New Project Description")
                .setStartDate(LocalDate.of(2024, 4, 1))
                .setEndDate(LocalDate.of(2024, 7, 1))
                .setStatus(Project.Status.INITIATED);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/projects")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ProjectResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), ProjectResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("New Project", actual.name());
        Assertions.assertEquals("New Project Description", actual.description());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Retrieve all projects for the authenticated user")
    public void getUserProjects_ValidRequest_ShouldReturnProjectList() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/projects")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ProjectResponseDto[] projects = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), ProjectResponseDto[].class);
        Assertions.assertTrue(projects.length > 0);
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Retrieve a project by its ID")
    public void getProjectById_ValidProjectId_ShouldReturnProject() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/projects/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ProjectResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), ProjectResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Project Alpha", actual.name());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Update an existing project")
    public void updateProject_ValidRequestDto_ShouldReturnUpdatedProject() throws Exception {
        UpdateProjectRequestDto requestDto = new UpdateProjectRequestDto()
                .setName("Updated Project")
                .setDescription("Updated Project Description")
                .setStartDate(LocalDate.of(2024, 2, 1))
                .setEndDate(LocalDate.of(2024, 9, 1))
                .setStatus(Project.Status.COMPLETED);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/projects/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ProjectResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), ProjectResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Updated Project", actual.name());
        Assertions.assertEquals("Updated Project Description", actual.description());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Delete a project by its ID")
    public void deleteProject_ValidProjectId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(
                        delete("/projects/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }
}

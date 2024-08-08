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
import mate.academy.taskmanagement.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagement.dto.task.TaskResponseDto;
import mate.academy.taskmanagement.dto.task.UpdateTaskRequestDto;
import mate.academy.taskmanagement.model.Task;
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
public class TaskControllerTest {
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
        executeSqlScript(
                dataSource,
                "mate/academy/taskmanagement/database/tasks/add-tasks.sql"
        );
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws Exception {
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
    @DisplayName("Create a new task")
    public void createTask_ValidRequestDto_ShouldReturnCreatedTask() throws Exception {
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto()
                .setName("New Task")
                .setDescription("New Description")
                .setPriority(Task.Priority.MEDIUM)
                .setStatus(Task.Status.NOT_STARTED)
                .setDueDate(LocalDate.of(2024, 4, 1))
                .setProjectId(1L)
                .setAssigneeId(1L);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/tasks")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        TaskResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                TaskResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("New Task", actual.name());
        Assertions.assertEquals("New Description", actual.description());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Retrieve tasks for a project")
    public void getTasksForProject_ValidProjectId_ShouldReturnTaskList() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/tasks")
                                .param("projectId", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        TaskResponseDto[] tasks = objectMapper.readValue(result.getResponse().getContentAsString(),
                TaskResponseDto[].class);
        Assertions.assertTrue(tasks.length > 0);
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Retrieve a task by its ID")
    public void getTaskById_ValidTaskId_ShouldReturnTask() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        TaskResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                TaskResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Existing Task", actual.name());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Update an existing task")
    public void updateTask_ValidRequestDto_ShouldReturnUpdatedTask() throws Exception {
        UpdateTaskRequestDto requestDto = new UpdateTaskRequestDto();
        requestDto.setName("Updated Task");
        requestDto.setDescription("Updated description");
        requestDto.setPriority(Task.Priority.HIGH);
        requestDto.setStatus(Task.Status.IN_PROGRESS);
        requestDto.setDueDate(LocalDate.of(2024, 5, 1));
        requestDto.setAssigneeId(1L);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/tasks/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        TaskResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                TaskResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Updated Task", actual.name());
        Assertions.assertEquals("Updated description", actual.description());
    }

    @WithMockUser(username = "user@example.com")
    @Test
    @DisplayName("Delete a task by its ID")
    public void deleteTask_ValidTaskId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(
                        delete("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }
}

package com.amin.taskmanagement;

import com.amin.taskmanagement.model.dto.TaskRequest;
import com.amin.taskmanagement.model.dto.TaskResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long createdTaskId;

    private TaskRequest createTaskRequest(String title) {
        TaskRequest request = new TaskRequest();
        request.setTitle(title);
        request.setDescription("This is a test task");
        request.setStatus("PENDING");
        return request;
    }

    @AfterEach
    void cleanup() {
        if (createdTaskId != null) {
            restTemplate.delete("/api/tasks/" + createdTaskId);
            createdTaskId = null;
        }
    }

    @Test
    void testCreateAndGetTask() {
        TaskRequest request = createTaskRequest("Test Task " + Instant.now().toEpochMilli());

        ResponseEntity<TaskResponse> createResponse = restTemplate.postForEntity(
                "/api/tasks",
                request,
                TaskResponse.class
        );

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TaskResponse createdTask = createResponse.getBody();
        assertThat(createdTask).isNotNull();
        createdTaskId = createdTask.getId();

        ResponseEntity<TaskResponse> getResponse = restTemplate.getForEntity(
                "/api/tasks/" + createdTaskId,
                TaskResponse.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getTitle()).isEqualTo(request.getTitle());
    }

    @Test
    void testGetAllTasks() {
        TaskRequest request = createTaskRequest("List Task " + Instant.now().toEpochMilli());
        TaskResponse createdTask = restTemplate.postForEntity("/api/tasks", request, TaskResponse.class).getBody();
        createdTaskId = createdTask.getId();

        ResponseEntity<TaskResponse[]> response = restTemplate.getForEntity("/api/tasks", TaskResponse[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testUpdateTask() {
        TaskRequest request = createTaskRequest("Update Task " + Instant.now().toEpochMilli());
        TaskResponse createdTask = restTemplate.postForEntity("/api/tasks", request, TaskResponse.class).getBody();
        createdTaskId = createdTask.getId();

        TaskRequest updateRequest = new TaskRequest();
        updateRequest.setTitle("Updated Task");
        updateRequest.setDescription("Updated description");
        updateRequest.setStatus("COMPLETED");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskRequest> requestEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<TaskResponse> updateResponse = restTemplate.exchange(
                "/api/tasks/" + createdTaskId,
                HttpMethod.PUT,
                requestEntity,
                TaskResponse.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().getTitle()).isEqualTo("Updated Task");
        assertThat(updateResponse.getBody().getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void testDeleteTask() {
        TaskRequest request = createTaskRequest("Delete Task " + Instant.now().toEpochMilli());
        TaskResponse createdTask = restTemplate.postForEntity("/api/tasks", request, TaskResponse.class).getBody();

        restTemplate.delete("/api/tasks/" + createdTask.getId());

        ResponseEntity<TaskResponse> getResponse = restTemplate.getForEntity(
                "/api/tasks/" + createdTask.getId(),
                TaskResponse.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

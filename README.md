# 💼 Task Management APP: Your Ultimate Online Task Manager 
___
Welcome to Task Management App! This tool helps you keep your projects and tasks organized. Manage your work easily, track progress, and integrate with Dropbox for file storage. Whether you’re leading a team or just organizing your own tasks, Task Management App is here to make your job easier.
___
## 🚀 Technologies and Tools
This project is built using a robust stack of technologies designed to ensure reliability, scalability, and security. Here's a glimpse of what powers Task Management:

- **Spring Boot:** Simplifies the development and deployment process.
- **Spring Security:** Provides comprehensive security for your application.
- **Spring Data JPA:** Manages and accesses data with ease.
- **JWT:** Handles secure authentication and authorization.
- **Lombok:** Reduces boilerplate code with annotations.
- **MapStruct:** Automates the mapping of Java beans.
- **Swagger:** Enhances API documentation and testing.
- **MySQL:** Reliable relational database management system.
- **Liquibase:** Manages database schema changes.
- **Docker:** Streamlines development and deployment in isolated environments.
- **Docker Testcontainers:** Facilitates integration testing with a MySQL container.
- **DropBox API:** Integrates with Dropbox for file storage and management.
- **Email Service:** Enables sending of automated emails for notifications.
___
## 🛠️ Project Endpoints
### **Here's a detailed look at the available endpoints:**
## 🔐 Authentication
## Register a New User
### POST
`/api/auth/register` - Registering a new user available without any role.

**Example Request:**
```json
{
    "email": "john.doe@example.com",
    "login": "john",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
}
```
**Response:**
```json
{
    "id": 1,
    "login": "john",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe"
}
```
## User Login
### POST
`/api/auth/login` - Log in as a registered user and get a JWT token.

**Example Request:**
```json
{
    "email": "john.doe@example.com",
    "password": "password123"
}
```
**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjcwMDA3MzAwLCJleHBpIjoxNjcwMDEwOTAwfQ.s5cR4FiqF4FLO2D-QWq8EdJHrg7MPZbD_eS4Ff93C9k"
}
```

## 🧑‍💼 User
## Get the Profile info
### GET
`/api/users/me` - Retrieves the profile of the currently logged-in user accessible for User, Admin roles.

**Response:**
```json
{
    "id": 1,
    "login": "john",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["ROLE_USER"]
}

```
## Update a User's Role
### PUT
`/api/users/{id}/role` - Updates the role of a user with the specified ID available for Admin role.

**Example Request:**
```json
{
    "roleName": "ROLE_ADMIN"
}
```
**Response:**
```json
{
    "id": 1,
    "login": "john",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["ROLE_USER", "ROLE_ADMIN"]
}
```

## Update the Profile
### PUT
`/api/users/me` - Updates the profile of the currently logged-in user available for User, Admin roles.

**Example Request:**

```json
{
    "login": "ddryzhov",
    "firstName": "John",
    "lastName": "DoeDoe",
    "email": "us32@example.com"
}
```
**Response:**
```json
{
    "id": 1,
    "login": "ddryzhov",
    "email": "us32@example.com",
    "firstName": "John",
    "lastName": "DoeDoe"
}

```

## 📁 Projects
## Create a New Project
### POST
`/api/projects` - Creates a new project with the provided details available for User, Admin roles.

**Example Request:**
```json
{
    "name": "Project Name2",
    "description": "Project Description",
    "startDate": "2024-01-01",
    "endDate": "2024-12-31",
    "status": "INITIATED"
}

```
**Response:**
```json
{
    "id": 1,
    "name": "Project Name2",
    "description": "Project Description",
    "startDate": "2024-01-01",
    "endDate": "2024-12-31",
    "tasks": [1, 2],
    "status": "INITIATED"
}
```

## Retrieve All Projects
### GET
`/api/projects` - Retrieves all projects associated with the currently authenticated user available for User, Admin roles.

**Response:**
```json
[
    {
        "id": 1,
        "name": "Project Name2",
        "description": "Project Description",
        "startDate": "2024-01-01",
        "endDate": "2024-12-31",
        "tasks": [1, 2],
        "status": "INITIATED"
    }
]
```

## Retrieve a Project by Its ID
### GET
`/api/projects/{id}` - Retrieves the project with the specified ID available for User, Admin roles.

**Response:**
```json
{
    "id": 1,
    "name": "Project Name2",
    "description": "Project Description",
    "startDate": "2024-01-01",
    "endDate": "2024-12-31",
    "tasks": [1, 2],
    "status": "INITIATED"
}
```

## Update an Existing Project
### PUT
`/api/projects/{id}` - Updates the project with the specified ID using the provided details available for User, Admin roles.

**Example Request:**
```json
{
    "name": "Done",
    "description": "Project Description",
    "startDate": "2024-01-01",
    "endDate": "2024-12-31",
    "status": "COMPLETED"
}

```
**Response:**
```json
{
    "id": 1,
    "name": "Done",
    "description": "Project Description",
    "startDate": "2024-01-01",
    "endDate": "2024-12-31",
    "tasks": [1, 2],
    "status": "COMPLETED"
}
```

## Delete a Project by Its ID
### DELETE
`/api/projects/{id}` - Soft-deletes the project with the specified ID available for User, Admin roles.

## ✅ Tasks
## Create a New Task
### POST
`/api/tasks` - Creates a new task with the provided details available for User, Admin roles.

**Example Request:**
```json
{
    "name": "Implement1",
    "description": "Develop",
    "priority": "LOW",
    "status": "NOT_STARTED",
    "dueDate": "2024-09-30",
    "projectId": 1,
    "assigneeId": 1
}
```
**Response:**
```json
{
    "id": 1,
    "name": "Implement1",
    "description": "Develop",
    "priority": "LOW",
    "status": "NOT_STARTED",
    "dueDate": "2024-09-30",
    "projectId": 1,
    "assigneeId": 1
}
```

## Retrieve Tasks for a Project
### GET
`/api/tasks?projectId={id}&page=0&size=10` - Retrieves all tasks for the specified project available for User, Admin roles.

**Response:**
```json
[
    {
        "id": 1,
        "name": "Implement1",
        "description": "Develop",
        "priority": "LOW",
        "status": "NOT_STARTED",
        "dueDate": "2024-09-30",
        "projectId": 1,
        "assigneeId": 1
    }
]
```

## Retrieve a Task by ID
### GET
`/api/tasks/{id}` - Retrieves a task with the specified ID available for User, Admin roles.

**Response:**
```json
{
    "id": 1,
    "name": "Implement1",
    "description": "Develop",
    "priority": "LOW",
    "status": "NOT_STARTED",
    "dueDate": "2024-09-30",
    "projectId": 1,
    "assigneeId": 1
}
```

## Update an Existing Task
### PUT
`/api/tasks/{id}` - Updates the task with the specified ID using the provided details available for User, Admin roles.

**Example Request:**
```json
{
    "name": "Update Task",
    "description": "Detailed description",
    "priority": "MEDIUM",
    "status": "COMPLETED",
    "dueDate": "2024-09-30",
    "assigneeId": 1
}

```
**Response:**
```json
{
    "id": 1,
    "name": "Update Task",
    "description": "Detailed description",
    "priority": "MEDIUM",
    "status": "COMPLETED",
    "dueDate": "2024-09-30",
    "projectId": 1,
    "assigneeId": 1
}
```

## Delete a Task by ID
### DELETE
`/api/tasks/{id}` - Soft-deletes the task with the specified ID available for User, Admin roles.

## 🏷️ Labels
## Create a New Label
### POST
`/api/labels` - Creates a new label with the provided details available for User, Admin roles.

**Example Request:**
```json
{
    "name": "feture",
    "color": "white",
    "taskId": 1
}
```
**Response:**
```json
{
    "id": 1,
    "name": "feture",
    "color": "white"
}
```

## Update an Existing Label
### PUT
`/api/labels/{id}` - Updates the label with the specified ID using the provided details available for User, Admin roles.

**Example Request:**
```json
{
    "name": "vasya",
    "color": "red"
}
```
**Response:**
```json
{
    "id": 1,
    "name": "vasya",
    "color": "red"
}
```

## Retrieve a Label by ID
### GET
`/api/labels/{id}` - Retrieves a label with the specified ID available for User, Admin roles.

**Response:**
```json
{
    "id": 1,
    "name": "vasya",
    "color": "red"
}
```

## Retrieve All Labels
### GET
`/api/labels` - Retrieves all labels available for User, Admin roles.
**Response:**
```json
[
    {
        "id": 1,
        "name": "vasya",
        "color": "red"
    }
]
```

## Delete a Label by ID
### DELETE
`/api/labels/{id}` - Deletes the label with the specified ID available for User, Admin roles.

## 💬 Comments
## Add a New Comment to a Task
### POST
`/api/comments` - Adds a new comment to a task using the provided details available for User, Admin roles.

**Example Request:**
```json
{
    "taskId": 1,
    "text": "good job!"
}
```
**Response:**
```json
{
    "id": 1,
    "taskId": 1,
    "text": "good job!",
    "timestamp": "2024-08-07T14:30:00"
}
```

## Retrieve Comments for a Specific Task
### GET
`/api/comments/{id}` - Retrieves comments associated with a specific task available for User, Admin roles.

**Response:**
```json
[
    {
        "id": 1,
        "taskId": 1,
        "text": "good job!",
        "timestamp": "2024-08-07T14:30:00"
    }
]
```

## 📎 Attachments
## Upload an Attachment to a Task
### POST
`/api/attachments` - Uploads an attachment file for a specific task available for User, Admin roles.

**Example Request:**
```form-data
{
    "file": postman-cloud:///1ef53277-046e-48e0-8467-de0a9472d7fa,
    "taskId": 1
}
```
**Response:**
```json
{
    "id": 1,
    "taskId": 1,
    "dropboxFileId": "abc123xyz",
    "filename": "example_attachment.txt",
    "uploadDate": "2024-08-07T14:30:00"
}
```

## Retrieve Attachments by Task ID
### GET
`/api/attachments?taskId={id}` - Retrieves all attachments associated with a specific task available for User, Admin roles.

**Response:**
```json
[
    {
        "id": 1,
        "taskId": 1,
        "dropboxFileId": "abc123xyz",
        "filename": "example_attachment.txt",
        "uploadDate": "2024-08-07T14:30:00"
    }
]
```

## Download an Attachment
### GET
`/api/attachments/download?fileId={id}` - Downloads a specific attachment by its ID available for User, Admin roles.

**Response:**
```Resource
Hello
```

## Delete an Attachment
### DELETE
`api/attachments/{id}` - Deletes a specific attachment by its ID available for User, Admin roles.

___
## 🌟 Challenges and Solutions
A key challenge was to unify different components into a single, efficient system. Achieving smooth interaction between diverse technologies demanded meticulous planning and thorough testing. By leveraging a structured methodology and my technical skills, I successfully addressed these issues and developed a reliable and functional platform.
___
## 💡 Future Improvements
Here are some ideas for enhancing Task Management:

- Enhanced User Experience: Continuously improve the interface and functionality based on user feedback.
- Mobile Optimization: Ensure full responsiveness across all devices.
- Collaboration Features: Introduce features like team chat or shared calendars to improve team collaboration and productivity.
___
## 🎥 Video Demonstration
Watch a 2-4 minute demo of BookStore in action.

[![Watch the video](http://img.youtube.com/vi/pcF8WYblcwk/0.jpg)](https://www.youtube.com/watch?v=pcF8WYblcwk)
___
## 📦 Postman Collection
You can explore our API using this Postman Collection. It includes all the endpoints described above with example requests and responses.
[Explore TaskManagement API on Postman](https://www.postman.com/science-geoscientist-94639784/workspace/task-management/collection/26879185-7b2a816e-d079-47df-9a81-5126fcbb1a44?action=share&creator=26879185)
___
Thank you for checking out Task Management! We hope our platform helps streamline your project management and task organization. Feel free to contribute or share feedback to help us make it even better.

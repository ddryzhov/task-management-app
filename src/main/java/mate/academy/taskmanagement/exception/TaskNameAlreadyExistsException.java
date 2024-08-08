package mate.academy.taskmanagement.exception;

public class TaskNameAlreadyExistsException extends RuntimeException {
    public TaskNameAlreadyExistsException(String message) {
        super(message);
    }
}

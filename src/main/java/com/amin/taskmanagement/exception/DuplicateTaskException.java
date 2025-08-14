package com.amin.taskmanagement.exception;


public class DuplicateTaskException extends RuntimeException {
    public DuplicateTaskException(String title) {
        super("Task with title '" + title + "' already exists");
    }
}

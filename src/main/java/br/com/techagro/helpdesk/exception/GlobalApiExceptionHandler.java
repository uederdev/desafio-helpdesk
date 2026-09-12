package br.com.techagro.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalApiExceptionHandler {


    @ExceptionHandler(UserDuplicadoException.class)
    public ResponseEntity<MessageApi> handleUserDuplicadoException(UserDuplicadoException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        MessageApi messageApi = new MessageApi(ex.getMessage(), LocalDateTime.now(), status.value(), status.getReasonPhrase());
        return ResponseEntity.status(status).body(messageApi);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageApi> handleException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MessageApi messageApi = new MessageApi(ex.getMessage(), LocalDateTime.now(), status.value(), status.getReasonPhrase());
        return ResponseEntity.status(status).body(messageApi);
    }
}

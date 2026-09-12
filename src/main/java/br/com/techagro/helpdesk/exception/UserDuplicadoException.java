package br.com.techagro.helpdesk.exception;

public class UserDuplicadoException extends RuntimeException{
    public UserDuplicadoException() {
        super("Usuário já cadastrado");
    }

    public UserDuplicadoException(String message) {
        super("Usuário já cadastrado. " + message);
    }
}

package br.com.techagro.helpdesk.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException() {
        super("Registro não encontrado");
    }

    public ObjectNotFoundException(String message) {
        super("Registro não encontrado. " + message);
    }
}

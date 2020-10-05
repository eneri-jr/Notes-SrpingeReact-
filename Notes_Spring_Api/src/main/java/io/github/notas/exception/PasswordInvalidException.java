package io.github.notas.exception;

public class PasswordInvalidException extends RuntimeException{
    public PasswordInvalidException(){
        super("Senha inválida");
    }
}

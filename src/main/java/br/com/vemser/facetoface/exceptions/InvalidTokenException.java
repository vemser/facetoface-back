package br.com.vemser.facetoface.exceptions;

public class InvalidTokenException extends RegraDeNegocioException {
    public InvalidTokenException(String message) {
        super(message);
    }
}

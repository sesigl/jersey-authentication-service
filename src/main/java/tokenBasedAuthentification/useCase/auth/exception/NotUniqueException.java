package tokenBasedAuthentification.useCase.auth.exception;

public class NotUniqueException extends RuntimeException {
    public NotUniqueException(String message) {
        super(message);
    }
}

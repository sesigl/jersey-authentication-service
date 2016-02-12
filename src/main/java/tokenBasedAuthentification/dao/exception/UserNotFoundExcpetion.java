package tokenBasedAuthentification.dao.exception;

public class UserNotFoundExcpetion extends RuntimeException {
    public UserNotFoundExcpetion(String message) {
        super(message);
    }
}

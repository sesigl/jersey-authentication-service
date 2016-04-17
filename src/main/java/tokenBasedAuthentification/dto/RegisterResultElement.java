package tokenBasedAuthentification.dto;

public class RegisterResultElement {
    public String message;
    public String email;
    public String activationKey;

    public RegisterResultElement() {
    }

    public RegisterResultElement(String message, String email, String activationKey) {
        this.message = message;
        this.email = email;
        this.activationKey = activationKey;
    }
}

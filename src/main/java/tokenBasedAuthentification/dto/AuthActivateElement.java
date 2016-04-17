package tokenBasedAuthentification.dto;

public class AuthActivateElement {
    public String email;
    public String activationKey;

    public AuthActivateElement() {
    }

    public AuthActivateElement(String email, String activationKey) {
        this.email = email;
        this.activationKey = activationKey;
    }
}

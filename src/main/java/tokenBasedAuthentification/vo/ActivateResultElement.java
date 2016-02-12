package tokenBasedAuthentification.vo;

public class ActivateResultElement {
    public String message;
    public String email;

    public ActivateResultElement() {
    }

    public ActivateResultElement(String message, String email) {
        this.message = message;
        this.email = email;
    }
}

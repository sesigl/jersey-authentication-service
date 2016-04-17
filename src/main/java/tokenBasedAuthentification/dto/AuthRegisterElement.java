package tokenBasedAuthentification.dto;

public class AuthRegisterElement {

    public String name;

    public String password;

    public String email;

    public AuthRegisterElement() {
    }

    public AuthRegisterElement(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
package tokenBasedAuthentification.vo;

public class AuthAccessElement {

    private String authId;
    private String authToken;
    private String authPermission;

    public AuthAccessElement() {
    }

    public AuthAccessElement(String authId, String authToken, String authPermission) {
        this.authId = authId;
        this.authToken = authToken;
        this.authPermission = authPermission;
    }

    public String getAuthId() {
        return authId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthPermission() {
        return authPermission;
    }

    public void setAuthPermission(String authPermission) {
        this.authPermission = authPermission;
    }

    // Getters and setters
}
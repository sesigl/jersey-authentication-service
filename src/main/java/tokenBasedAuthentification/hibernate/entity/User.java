package tokenBasedAuthentification.hibernate.entity;


import javax.persistence.*;

@Entity
public class User {

    public User() {
    }

    public User(String name, String email, String authToken, String role, String password) {
        this.name = name;
        this.email = email;
        this.authToken = authToken;
        this.role = role;
        this.password = password;
    }

    @Id
    @GeneratedValue
    public int id;
    public String name;

    @Column(unique = true)
    public String email;

    public String authToken;
    public String role;
    public String password;
    public byte[] salt;
    public boolean activated = false;
    public String activationKey;

    @Override
    public boolean equals(Object obj) {
        User otherUser = (User) obj;

        if (
                this.name.equals(otherUser.name) &&
                        this.authToken.equals(otherUser.authToken) &&
                        this.email.equals(otherUser.email) &&
                        this.role.equals(otherUser.role)) {

            return true;
        } else {

            return false;
        }
    }
}

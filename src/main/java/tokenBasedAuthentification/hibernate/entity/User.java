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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id && (email != null ? email.equals(user.email) : user.email == null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

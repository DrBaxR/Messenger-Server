package messenger.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PasswordReset {
    @Id
    @GeneratedValue
    private String id;

    private String email;

    public PasswordReset() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PasswordReset(String email) {
        this.email = email;
    }
}

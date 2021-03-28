package payroll.controllers.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginForm {

    @NotBlank
    @Size(min=3, max = 60)
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

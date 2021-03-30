package messenger.controllers.forms;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.*;

public class SignUpForm {


    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    @NotBlank
    private String username;


    private Set<String> roles;



    @NotBlank
    @Size(min = 6, max = 40)
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set getRole() {
        return roles;
    }

    public void setRole(Set role) {
        this.roles = role;
    }

    public List<String> getGroups(){return new ArrayList<String>();}




}
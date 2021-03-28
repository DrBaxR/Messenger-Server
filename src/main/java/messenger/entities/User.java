package messenger.entities;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public
class User {

    private @Id @GeneratedValue String id;

    private String username;

    private String email;

    private String password;

    @ElementCollection
    private List<String> groups = new ArrayList();

    public User(){ }

    public User(String id, String username, String email, String password, List<String> groups) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.groups = groups;
    }

    public void addGroup(String groupId){
        this.groups.add(groupId);
    }

    public void removeGroup(String group){
        this.groups.remove(group);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.email, this.username, this.password, this.groups);
    }

    //TODO: verify about groups
    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return Objects.equals(this.id, user.id) && Objects.equals(this.username, user.username)
                && Objects.equals(this.email, user.email) && Objects.equals(this.password, user.password);

    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + this.id + '\''
                +", username='" + this.username + '\''
                + ", email='" + this.email + '\''
                +", password='" +  this.password + '\''
                + ", groups='" + this.groups + '\''
                + '}';
    }
}

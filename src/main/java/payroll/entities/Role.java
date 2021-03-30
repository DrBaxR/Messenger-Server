package payroll.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;


@Document(collection = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private RoleName name;

    public Role() {}

    public Role(RoleName name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}
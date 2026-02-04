package deim.urv.cat.homework2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String fullName; // Agregado para JSON
    private String email;
    private String username;

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("lastName")
    public String getFirstName() {
        return fixNull(this.firstName);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return fixNull(this.lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return fixNull(this.email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return fixNull(this.username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String fixNull(String in) {
        return (in == null) ? "" : in;
    }
    
        // Getters y setters para fullName
    public String getFullName() {
        return fullName != null ? fullName : (firstName + " " + lastName).trim();
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    @Override //metodos para el distinc(para no tener autoter duplicados)
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(fullName, user.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName);
    }
}


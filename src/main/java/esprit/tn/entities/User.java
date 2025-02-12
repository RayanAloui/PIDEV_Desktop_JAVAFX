package esprit.tn.entities;

import java.util.Objects;

public class User {
    private int id;
    private String name;
    private String surname;
    private String telephone;
    private String email;
    private String password;
    private String role;
    private int isBlocked;
    private int isConfirmed;
    private int numberVerification;
    private int token;

    public User() {}

    public User(String name, String surname, String telephone, String email, String password, String role, int isBlocked, int isConfirmed, int numberVerification,int token) {
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isBlocked = isBlocked;
        this.isConfirmed = isConfirmed;
        this.numberVerification = numberVerification;
        this.token=token;
    }

    public User(int id, String name, String surname, String telephone, String email, String password, String role, int isBlocked, int isConfirmed, int numberVerification,int token) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isBlocked = isBlocked;
        this.isConfirmed = isConfirmed;
        this.numberVerification = numberVerification;
        this.token = token;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int isBlocked() { return isBlocked; }
    public void setBlocked(int blocked) { isBlocked = blocked; }

    public int isConfirmed() { return isConfirmed; }
    public void setConfirmed(int confirmed) { isConfirmed = confirmed; }

    public int getNumberVerification() { return numberVerification; }
    public void setNumberVerification(int numberVerification) { this.numberVerification = numberVerification; }

    public int getToken() { return token; }
    public void setToken(int token) { this.token = token; }

    public int getIsBlocked() { return isBlocked; }
    public void setIsBlocked(int isBlocked) { this.isBlocked = isBlocked; }

    public int getIsConfirmed() { return isConfirmed; }
    public void setIsConfirmed(int isConfirmed) { this.isConfirmed = isConfirmed; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id == user.id &&
                isBlocked == user.isBlocked &&
                isConfirmed == user.isConfirmed &&
                numberVerification == user.numberVerification &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(telephone, user.telephone) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(role, user.role);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, telephone, email, password, role, isBlocked, isConfirmed, numberVerification , token);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", isBlocked=" + isBlocked +
                ", isConfirmed=" + isConfirmed +
                ", numberVerification=" + numberVerification +
                ", token='" + token + '\'' +
                '}';
    }
}

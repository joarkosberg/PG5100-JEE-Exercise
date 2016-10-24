package no.joarkosberg.exam.backend.entity;

import no.joarkosberg.exam.backend.validation.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = User.COUNT_ALL_USERS, query =
                "select count(*) " +
                        "from User u")
})

@Entity
public class User {
    public static final String COUNT_ALL_USERS = "COUNT_ALL_USERS";

    @Id
    @Pattern(regexp = "[A-Za-z0-9]{2,32}")
    private String userId;

    @NotEmpty
    @Size(min = 2, max = 32)
    private String firstName;

    @Size(max = 32)
    private String middleName;

    @NotEmpty
    @Size(min = 2, max = 32)
    private String lastName;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationTime;

    @NotEmpty
    private String hash;

    @NotEmpty
    @Size(max = 26)
    private String salt;

    public User(){
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}

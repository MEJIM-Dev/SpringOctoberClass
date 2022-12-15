package First.Spring.model;

import First.Spring.model.enumeration.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String email;
    @Column(name = "firstname",nullable = false,length = 50)
    private String firstname;
    private String lastname;

    private Gender gender;
    private String phone;

    public User(){
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\''+
                "email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    public User(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

}

package First.Spring.dto.response;

import First.Spring.model.User;
import First.Spring.model.enumeration.Gender;
import lombok.Data;

import java.util.Collection;

@Data
public class UserResponseDTO {
    private long id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private Gender gender;
    private Collection<?> roles;

    public UserResponseDTO (User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.phone = user.getPhone();
        this.gender = user.getGender();
        this.roles = user.getAuthorities();
    }
}

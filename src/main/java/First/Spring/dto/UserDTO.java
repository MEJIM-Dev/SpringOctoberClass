package First.Spring.dto;

import First.Spring.annotation.NigerianNumber;
import First.Spring.model.User;
import First.Spring.model.enumeration.Gender;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserDTO {

    private long id;
    @Size(min=11) @Email
    private String email;
    @Size(max=20, min = 3)
    @NotNull
    private String firstname;
    @Size(min = 3,max = 20)
    @NotBlank
    private String lastname;
    @NigerianNumber(message = "FAILED")
    private String phone;
    @NotNull
    private Gender gender;

    public User getUser(){
        User user = new User();
        user.setEmail(this.email);
        user.setLastname(this.lastname);
        user.setFirstname(this.firstname);
        user.setGender(this.gender);
        user.setPhone(this.phone);
        return user;
    }

}

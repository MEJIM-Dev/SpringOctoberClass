package First.Spring.dto;

import First.Spring.model.User;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserDTO {

    private long id;
    @Size(min=11) @Email(message = "Failed Email Validation")
    private String email;
    @Size(max=20, min = 3)
    @NotNull
    private String firstname;
    @Size(min = 3,max = 20)
    @NotBlank
    private String lastname;
    private long phone;
    private String countryCode;
    String[] asdas = {"Asassa","ASasa"};

    public User getUser(){
        User user = new User();
        user.setEmail(this.email);
        user.setFirstname(this.firstname);
        user.setId(this.id);
        user.setLastname(this.lastname);
        return user;
    }

}

package test.restapi.cities.restAssured.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthPojo {
    private String username;
    private String password;

}

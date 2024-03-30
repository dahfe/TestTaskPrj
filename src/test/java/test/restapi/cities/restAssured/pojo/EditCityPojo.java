package test.restapi.cities.restAssured.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EditCityPojo {
    private Long id;
    private String name;
    private List<ImagePojo> images;
}

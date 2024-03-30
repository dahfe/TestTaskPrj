package test.restapi.cities.restAssured.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CityPojo {
    private String name;
    private List<ImagePojo> images;
}

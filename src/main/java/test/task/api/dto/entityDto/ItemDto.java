package test.task.api.dto.entityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    @NotNull
    private String itemName;

    @NotNull
    private String itemGroup;

    @NotNull
    private String unitOfMeasurement;

    @NotNull
    private int quantity;

    @NotNull
    private BigDecimal priceWithoutVAT;

    private String storageLocation;

    private String contactPerson;

    private String photo;
}

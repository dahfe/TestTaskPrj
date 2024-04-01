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
public class TMARequestDto {

    @NotNull
    private String itemName;

    @NotNull
    private int quantity;

    @NotNull
    private BigDecimal priceWithoutVAT;

    private String comment;
}

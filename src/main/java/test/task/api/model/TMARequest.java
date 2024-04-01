package test.task.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TMA_requests")
public class TMARequest {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "unit_of_measurement_id", referencedColumnName = "id")
    private UnitOfMeasurement unitOfMeasurement;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price_without_VAT", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal priceWithoutVAT;

    @Column(name = "comment", length = 500, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "status")
    private String status;

    @Column(name = "request_row_id", nullable = false)
    private String requestRowId;
}

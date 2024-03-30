package test.task.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_group", nullable = false)
    private String itemGroup;

    @Column(name = "unit_of_measurement", nullable = false)
    private String unitOfMeasurement;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price_without_VAT", nullable = false)
    private double UAH;

    @Column(name = "status", nullable = false, columnDefinition = "TEXT")
    private String status;

    @Column(name = "storage_location", columnDefinition = "TEXT")
    private String storageLocation;

    @Column(name = "contact_person", length = 500, columnDefinition = "TEXT")
    private String contactPerson;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

}

package test.task.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.task.api.model.UnitOfMeasurement;

import java.util.Optional;

@Repository
public interface UnitOfMeasurementRepository extends JpaRepository<UnitOfMeasurement, Long> {
    Optional<UnitOfMeasurement> findByUnit(String type);
}

package test.task.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.task.api.model.TMARequest;

import java.util.UUID;

@Repository
public interface TMARequestRepository extends JpaRepository<TMARequest, UUID> {
}

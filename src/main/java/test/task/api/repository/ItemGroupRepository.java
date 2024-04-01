package test.task.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.task.api.model.ItemGroup;

import java.util.Optional;

@Repository
public interface ItemGroupRepository extends JpaRepository<ItemGroup, Long> {
    Optional<ItemGroup> findByType(String type);
}

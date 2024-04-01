package test.task.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.task.api.model.ItemGroup;


@Repository
public interface ItemGroupRepository extends JpaRepository<ItemGroup, Long> {
    ItemGroup findByType(String type);
}

package test.task.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.task.api.model.Role;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByName(String name);
}

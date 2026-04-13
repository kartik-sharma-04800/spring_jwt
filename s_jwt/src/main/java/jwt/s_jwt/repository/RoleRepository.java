package jwt.s_jwt.repository;

import jwt.s_jwt.entity.ERole;
import jwt.s_jwt.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}

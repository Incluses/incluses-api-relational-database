package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.PermissaoVaga;

import java.util.UUID;

public interface PermissaoVagaRepository extends JpaRepository<PermissaoVaga, UUID> {
}

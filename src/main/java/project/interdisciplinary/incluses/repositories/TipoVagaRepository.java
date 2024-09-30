package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.TipoVaga;

import java.util.UUID;

public interface TipoVagaRepository extends JpaRepository<TipoVaga, UUID> {
}

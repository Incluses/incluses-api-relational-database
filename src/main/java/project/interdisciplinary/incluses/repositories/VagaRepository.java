package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.Vaga;

import java.util.UUID;

public interface VagaRepository extends JpaRepository<Vaga, UUID> {
}

package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.Setor;

import java.util.UUID;

public interface SetorRepository extends JpaRepository<Setor, UUID> {
}

package project.interdisciplinary.incluses.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.interdisciplinary.incluses.models.Arquivo;

import java.util.UUID;

public interface ArquivoRepository extends JpaRepository<Arquivo, UUID> {
}

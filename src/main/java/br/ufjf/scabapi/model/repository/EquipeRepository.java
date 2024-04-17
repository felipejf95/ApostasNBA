package br.ufjf.scabapi.model.repository;

import br.ufjf.scabapi.model.entity.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {
}

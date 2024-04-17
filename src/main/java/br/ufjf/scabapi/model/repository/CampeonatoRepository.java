package br.ufjf.scabapi.model.repository;

import br.ufjf.scabapi.model.entity.Campeonato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampeonatoRepository extends JpaRepository<Campeonato, Long> {
}

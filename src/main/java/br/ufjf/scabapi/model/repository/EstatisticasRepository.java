package br.ufjf.scabapi.model.repository;

import br.ufjf.scabapi.model.entity.Estatisticas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstatisticasRepository extends JpaRepository<Estatisticas, Long> {
}

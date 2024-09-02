package br.ufjf.scabapi.model.repository;


import br.ufjf.scabapi.model.entity.ApostaCampeonato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApostaCampeonatoRepository extends JpaRepository<ApostaCampeonato, Long> {
    List<ApostaCampeonato> findByJogadorId(Long jogadorId);
}

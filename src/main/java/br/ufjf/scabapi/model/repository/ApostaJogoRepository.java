package br.ufjf.scabapi.model.repository;

import br.ufjf.scabapi.model.entity.ApostaJogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApostaJogoRepository extends JpaRepository<ApostaJogo, Long> {
    List<ApostaJogo> findByJogadorId(Long jogadorId);
}

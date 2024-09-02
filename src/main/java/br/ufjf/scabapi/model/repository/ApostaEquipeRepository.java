package br.ufjf.scabapi.model.repository;


import br.ufjf.scabapi.model.entity.ApostaEquipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApostaEquipeRepository extends JpaRepository<ApostaEquipe, Long> {
    List<ApostaEquipe> findByJogadorId(Long jogadorId);
}

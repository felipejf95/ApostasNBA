package br.ufjf.scabapi.model.repository;

import br.ufjf.scabapi.model.entity.Aposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApostaRepository extends JpaRepository<Aposta, Long> {
    // Aqui você pode definir métodos que são comuns a todas as apostas
    List<Aposta> findByJogadorId(Long jogadorId);
}

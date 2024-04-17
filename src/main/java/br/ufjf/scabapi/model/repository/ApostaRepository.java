package br.ufjf.scabapi.model.repository;

import br.ufjf.scabapi.model.entity.Aposta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApostaRepository extends JpaRepository<Aposta, Long> {
}

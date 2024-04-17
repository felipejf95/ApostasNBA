package br.ufjf.scabapi.model.repository;

import br.ufjf.scabapi.model.entity.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {
}

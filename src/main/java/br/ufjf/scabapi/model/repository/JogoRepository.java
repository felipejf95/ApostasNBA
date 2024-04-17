package br.ufjf.scabapi.model.repository;

import br.ufjf.scabapi.model.entity.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

}

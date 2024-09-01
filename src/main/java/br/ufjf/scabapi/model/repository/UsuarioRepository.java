package br.ufjf.scabapi.model.repository;

import br.ufjf.scabapi.model.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Pessoa, Long> {
}

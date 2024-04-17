package br.ufjf.scabapi.model.repository;

import br.ufjf.scabapi.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}

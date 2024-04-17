package br.ufjf.scabapi.service;

import br.ufjf.scabapi.model.entity.Usuario;
import br.ufjf.scabapi.model.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }

    public List<Usuario> getUsuarios(){
        return  repository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id){
        return  repository.findById(id);
    }
}

package br.ufjf.scabapi.service;

import br.ufjf.scabapi.model.entity.Pessoa;
import br.ufjf.scabapi.model.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }

    public List<Pessoa> getUsuarios(){
        return  repository.findAll();
    }

    public Optional<Pessoa> getUsuarioById(Long id){
        return  repository.findById(id);
    }
}

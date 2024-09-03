package br.ufjf.scabapi.service;

import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.Jogador;
import br.ufjf.scabapi.model.repository.JogadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JogadorService {

    private JogadorRepository repository;

    public JogadorService(JogadorRepository repository){
        this.repository = repository;
    }

    public List<Jogador> getJogadores(){
        return repository.findAll();
    }

    public Optional<Jogador> getJogadorById (Long id){
        return repository.findById(id);
    }

    @Transactional
    public Jogador salvar(Jogador jogador){
        validar(jogador);
        return repository.save(jogador);
    }

    @Transactional
    public void excluir(Jogador jogador){
        Objects.requireNonNull(jogador.getId());
        repository.delete(jogador);
    }

    public void validar(Jogador jogador){
        if (jogador.getNome() == null){
            throw new RegraNegocioException("O jogador deve ter um nome");
        }
    }
}

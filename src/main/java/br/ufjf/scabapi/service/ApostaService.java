package br.ufjf.scabapi.service;

import br.ufjf.scabapi.exception.RegraNegocioException;
import br.ufjf.scabapi.model.entity.Aposta;
import br.ufjf.scabapi.model.repository.ApostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApostaService {

    @Autowired
    private ApostaRepository repository;

    public ApostaService(ApostaRepository repository){
        this.repository = repository;
    }

    public List<Aposta> getApostas(){
        return repository.findAll();
    }

    public Optional<Aposta> getApostaById(Long id){
        return repository.findById(id);
    }

    @Transactional
    public Aposta salvar(Aposta aposta){
        validar(aposta);
        return repository.save(aposta);
    }

    @Transactional
    public void excluir(Aposta aposta) {
        Objects.requireNonNull(aposta.getId());
        repository.delete(aposta);
    }


    public void validar(Aposta aposta) {
        if (aposta.getValor() == 0 || aposta.getOdd() == 0 ){
            throw new RegraNegocioException("O valor da aposta nao pode ser 0");
        }
    }

}

package br.ufjf.scabapi.model.entity;


import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor


public class ApostaCampeonato extends Aposta {

    private String campeonato;

    @Override
    public void validar() {
        // Implementação da lógica de validação para uma aposta de campeonato
    }

    @Override
    public Double calcularRetorno(Double valor) {
        // Implementação do cálculo do retorno da aposta
        return valor * this.getOdd(); // Exemplo simplificado
    }
}

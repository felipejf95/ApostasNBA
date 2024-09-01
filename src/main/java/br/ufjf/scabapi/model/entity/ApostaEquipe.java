package br.ufjf.scabapi.model.entity;


import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor


public class ApostaEquipe extends Aposta {

    private String equipe;

    @Override
    public void validar() {
        // Implementação da lógica de validação para uma aposta de equipe
    }

    @Override
    public Double calcularRetorno() {
        // Implementação do cálculo do retorno da aposta
        return this.getValor() * this.getOdd(); // Exemplo simplificado
    }
}

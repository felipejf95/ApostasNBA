package br.ufjf.scabapi.api.dto;

import br.ufjf.scabapi.model.entity.Jogador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class JogadorDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;

    public static JogadorDTO create(Jogador jogador){
        ModelMapper modelMapper = new ModelMapper();
        JogadorDTO dto = modelMapper.map(jogador, JogadorDTO.class);
        return dto;
    }
}

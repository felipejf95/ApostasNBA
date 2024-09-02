package br.ufjf.scabapi.api.dto;

import br.ufjf.scabapi.model.entity.Jogo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class JogoDTO {

    private Long id;
    private String equipeA;
    private String equipeB;

    public static JogoDTO create(Jogo jogo) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(jogo, JogoDTO.class);
    }
}

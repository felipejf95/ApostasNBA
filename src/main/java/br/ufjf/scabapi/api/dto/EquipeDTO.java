package br.ufjf.scabapi.api.dto;

import br.ufjf.scabapi.model.entity.Equipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EquipeDTO {

    private Long id;
    private String nome;
    private String Conferencia;

    public static EquipeDTO create(Equipe equipe) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(equipe, EquipeDTO.class);
    }
}

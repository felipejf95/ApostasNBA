package br.ufjf.scabapi.api.dto;

import br.ufjf.scabapi.model.entity.ApostaEquipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ApostaEquipeDTO extends ApostaDTO{

    private Long id;
    private double odd;
    private double valor;
    private Long jogadorId;

    public static ApostaEquipeDTO create(ApostaEquipe apostaEquipe) {
        ModelMapper modelMapper = new ModelMapper();
        ApostaEquipeDTO dto = modelMapper.map(apostaEquipe, ApostaEquipeDTO.class);
        if (apostaEquipe.getJogador() != null){
            dto.setJogadorId(apostaEquipe.getJogador().getId());
        }
        return dto;
    }
}

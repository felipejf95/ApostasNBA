package br.ufjf.scabapi.api.dto;

import br.ufjf.scabapi.model.entity.ApostaJogo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ApostaJogoDTO {

    private Long id;
    private double odd;
    private double valor;
    private Long jogadorId;

    public static ApostaJogoDTO create(ApostaJogo apostaJogo) {
        ModelMapper modelMapper = new ModelMapper();
        ApostaJogoDTO dto = modelMapper.map(apostaJogo, ApostaJogoDTO.class);
        if (apostaJogo.getJogador() != null){
            dto.setJogadorId(apostaJogo.getJogador().getId());
        }
        return dto;
    }
}

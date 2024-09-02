package br.ufjf.scabapi.api.dto;

import br.ufjf.scabapi.model.entity.ApostaCampeonato;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ApostaCampeonatoDTO {

    private Long id;
    private double odd;
    private double valor;
    private Long jogadorId;

    public static ApostaCampeonatoDTO create(ApostaCampeonato apostaCampeonato) {
        ModelMapper modelMapper = new ModelMapper();
        ApostaCampeonatoDTO dto = modelMapper.map(apostaCampeonato, ApostaCampeonatoDTO.class);
        if (apostaCampeonato.getJogador() != null){
            dto.setJogadorId(apostaCampeonato.getJogador().getId());
        }
        return dto;
    }
}

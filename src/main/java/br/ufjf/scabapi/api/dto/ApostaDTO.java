package br.ufjf.scabapi.api.dto;


import br.ufjf.scabapi.model.entity.Aposta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ApostaDTO {

    private Long id;
    private double odd;
    private double valor;
    private Long jogadorId;

    public static ApostaDTO create(Aposta aposta) {
        ModelMapper modelMapper = new ModelMapper();
        ApostaDTO dto = modelMapper.map(aposta, ApostaDTO.class);
        dto.jogadorId = aposta.getJogador().getId();
        return dto;
    }
}

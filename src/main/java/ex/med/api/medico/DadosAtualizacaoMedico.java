package ex.med.api.medico;

import ex.med.api.endereco.DadosEndereco;
import ex.med.api.endereco.Endereco;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoMedico(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco
) {

}

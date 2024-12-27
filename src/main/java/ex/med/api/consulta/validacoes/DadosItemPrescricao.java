package ex.med.api.consulta.validacoes;

import jakarta.validation.constraints.NotNull;

public record DadosItemPrescricao(
        @NotNull(message = "O medicamente não pode ser nulo.")
        String medicamento,
        String dosagem,
        String frequencia
) {
}

package ex.med.api.consulta.feedbacks;

import jakarta.validation.constraints.NotNull;

public record DadosFeedback(
        @NotNull
        Long idConsulta,
        String comentario,
        Integer nota
) {
}

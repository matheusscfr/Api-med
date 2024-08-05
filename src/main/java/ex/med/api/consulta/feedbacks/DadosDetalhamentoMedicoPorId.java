package ex.med.api.consulta.feedbacks;

import ex.med.api.domain.FeedbackDomain;
import ex.med.api.domain.MedicoDomain;


public record DadosDetalhamentoMedicoPorId(
        Long id,
        Long idConsulta,
        String comentario,
        Integer nota,
        String nome
) {
    public DadosDetalhamentoMedicoPorId(FeedbackDomain feedbackDomain, MedicoDomain medicoDomain) {
        this(feedbackDomain.getId(),feedbackDomain.getConsulta().getId(), feedbackDomain.getComentario(),feedbackDomain.getNota(), medicoDomain.getNome());
    }
}

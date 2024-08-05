package ex.med.api.consulta.feedbacks;

import ex.med.api.domain.FeedbackDomain;

public record DadosDetalhamentoFeedback(
        Long id,
        Long idConsulta,
        String comentario,
        Integer nota
) {

    public DadosDetalhamentoFeedback (FeedbackDomain feedbackDomain) {
        this(feedbackDomain.getId(),feedbackDomain.getConsulta().getId(),feedbackDomain.getComentario(),feedbackDomain.getNota());
    }
}

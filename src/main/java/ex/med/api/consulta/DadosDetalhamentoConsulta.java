package ex.med.api.consulta;

import ex.med.api.domain.ConsultaDomain;

import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(
        Long id,
        Long idMedico,
        Long idPaciente,
        LocalDateTime data
) {
    public DadosDetalhamentoConsulta(ConsultaDomain consulta) {
        this(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente().getId(), consulta.getData());
    }
}

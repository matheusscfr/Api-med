package ex.med.api.consulta;


import ex.med.api.medico.Especialidade;
import java.time.LocalDateTime;

public record DadosAtualizacaoConsulta(
        Long idMedico,
        Long idPaciente,
        LocalDateTime data,
        Especialidade especialidade
) {
}

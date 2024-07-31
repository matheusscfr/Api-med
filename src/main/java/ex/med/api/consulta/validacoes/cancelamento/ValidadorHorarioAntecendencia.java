package ex.med.api.consulta.validacoes.cancelamento;

import ex.med.api.Error.ValidacaoException;
import ex.med.api.consulta.DadosCancelamentoConsulta;
import ex.med.api.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;

public class ValidadorHorarioAntecendencia implements ValidadorCancelamentoConsulta {
    @Autowired
    private ConsultaRepository repository;

    @Override
    public void validar(DadosCancelamentoConsulta dados){
        var consulta = repository.getReferenceById(dados.idConsulta());
        var agora = LocalDateTime.now();
        var diferencaEmHoras = Duration.between(agora,consulta.getData()).toHours();

        if(diferencaEmHoras < 24){
            throw new ValidacaoException("Consulta só pode ser cancelada com antecedência mínima de 24h!");
        }
    }
}

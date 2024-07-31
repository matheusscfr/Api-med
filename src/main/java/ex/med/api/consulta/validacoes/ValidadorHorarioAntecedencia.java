package ex.med.api.consulta.validacoes;

import ex.med.api.Error.ValidacaoException;
import ex.med.api.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoDeConsultas {
    public void validar(DadosAgendamentoConsulta dados){
        var dataConsulta = dados.data();
        var agora = LocalDateTime.now();

        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

        if(diferencaEmMinutos < 30){
        throw new ValidacaoException("Consulta deve ser agendada com antecedencia de 30 minutos.");
        }
    }



}

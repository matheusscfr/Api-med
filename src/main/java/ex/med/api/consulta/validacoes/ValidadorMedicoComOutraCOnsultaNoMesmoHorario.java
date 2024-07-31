package ex.med.api.consulta.validacoes;

import ex.med.api.Error.ValidacaoException;
import ex.med.api.consulta.DadosAgendamentoConsulta;
import ex.med.api.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoComOutraCOnsultaNoMesmoHorario implements ValidadorAgendamentoDeConsultas {
    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados){

        var medicoPossuiOutraConsulta = repository.existsByMedicoIdAndData(dados.idMedico(), dados.data());

        if(medicoPossuiOutraConsulta){
            throw new ValidacaoException("Medico já possui outra consulta agendada nesse mesmo horário.");
        }
    }
}

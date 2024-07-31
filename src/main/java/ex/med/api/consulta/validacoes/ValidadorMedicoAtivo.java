package ex.med.api.consulta.validacoes;

import ex.med.api.consulta.DadosAgendamentoConsulta;
import ex.med.api.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsultas {

    @Autowired
    private MedicoRepository repository;
    public void validar(DadosAgendamentoConsulta dados) {
        if(dados.idMedico() == null){
            return ;
        }

        var medicoEstaAtivo = repository.findAtivoById(dados.idMedico());
    }

}

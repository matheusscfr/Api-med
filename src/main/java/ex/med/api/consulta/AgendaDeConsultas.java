package ex.med.api.consulta;
import java.util.List;
import ex.med.api.Error.ValidacaoException;
import ex.med.api.consulta.validacoes.ValidadorAgendamentoDeConsultas;
import ex.med.api.consulta.validacoes.cancelamento.ValidadorCancelamentoConsulta;
import ex.med.api.domain.ConsultaDomain;
import ex.med.api.domain.MedicoDomain;
import ex.med.api.repository.ConsultaRepository;
import ex.med.api.repository.MedicoRepository;
import ex.med.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AgendaDeConsultas {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoDeConsultas> validadores;



    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados){

        if(!pacienteRepository.existsById(dados.idPaciente()) || (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico()))){
            throw new ValidacaoException("Id não encontrado");
        }

        validadores.forEach(v -> v.validar(dados));

        var medico = escolherMedico(dados);
        if(medico == null){
            throw new ValidacaoException("Não existe médico disponível.");
        }

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());

        var consulta = new ConsultaDomain(null, medico, paciente, dados.data(), null);

        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }

    private MedicoDomain escolherMedico(DadosAgendamentoConsulta dados) {
        if(dados.idMedico() != null  ){
            return medicoRepository.getReferenceById(dados.idMedico());
        } else if (dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido.");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }

    public void cancelar(DadosCancelamentoConsulta dados){
        if(!consultaRepository.existsById(dados.idConsulta())){
            throw new ValidacaoException("Id da consulta informado não existe!");
        }


        var consulta = consultaRepository.getReferenceById(dados.idConsulta());

        consulta.cancelar(dados.motivo());
    }


}
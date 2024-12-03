package ex.med.api.ControllerTest;

import ex.med.api.Error.ValidacaoException;
import ex.med.api.consulta.AgendaDeConsultas;
import ex.med.api.consulta.DadosAgendamentoConsulta;
import ex.med.api.consulta.DadosDetalhamentoConsulta;
import ex.med.api.consulta.validacoes.ValidadorAgendamentoDeConsultas;
import ex.med.api.controller.ConsultaControllerr;
import ex.med.api.domain.ConsultaDomain;
import ex.med.api.domain.MedicoDomain;
import ex.med.api.domain.PacienteDomain;
import ex.med.api.medico.Especialidade;
import ex.med.api.repository.ConsultaRepository;
import ex.med.api.repository.MedicoRepository;
import ex.med.api.repository.PacienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsultaControllerTest {


    @Mock
    private PacienteRepository pacienteRepository;  // Mock do repositório de pacientes

    @Mock
    private MedicoRepository medicoRepository;  // Mock do repositório de médicos

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private AgendaDeConsultas agenda;

    @Mock
    private List<ValidadorAgendamentoDeConsultas> validadores;

    @InjectMocks
    private ConsultaControllerr controller;  // A classe que está sendo testada
    @Test
    void deveAgendarConsultaComSucesso() {

        // Dados para o agendamento
        DadosAgendamentoConsulta dadosAgendamento = new DadosAgendamentoConsulta(
                1L,  // idMedico
                1L,  // idPaciente
                LocalDateTime.now().plusDays(1),
                Especialidade.cardiologia
        );

        // Criando o PacienteDomain com id e outros dados necessários
        PacienteDomain paciente = new PacienteDomain();
        paciente.setId(dadosAgendamento.idPaciente());
        paciente.setNome("Paciente Teste");
        paciente.setEmail("paciente@teste.com");
        paciente.setTelefone("123456789");
        paciente.setCpf("12345678901");

        // Criando o MedicoDomain com id e outros dados necessários
        MedicoDomain medico = new MedicoDomain();
        medico.setId(dadosAgendamento.idMedico());
        medico.setNome("Dr. Teste");
        medico.setEmail("medico@teste.com");
        medico.setCrm("123456");
        medico.setEspecialidade(Especialidade.cardiologia);


        lenient().when(pacienteRepository.save(paciente)).thenReturn(paciente);
        lenient().when(medicoRepository.save(medico)).thenReturn(medico);


        ConsultaDomain consulta = new ConsultaDomain(1L, medico, paciente, dadosAgendamento.data(), null);
        lenient().when(consultaRepository.save(any(ConsultaDomain.class))).thenReturn(consulta);


        DadosDetalhamentoConsulta detalhamentoConsulta = new DadosDetalhamentoConsulta(consulta);
        lenient().when(agenda.agendar(dadosAgendamento)).thenReturn(detalhamentoConsulta);  // Simulando a chamada ao método de agendamento

        ResponseEntity<DadosDetalhamentoConsulta> response = controller.agendar(dadosAgendamento);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals(dadosAgendamento.idPaciente(), response.getBody().idPaciente());
        assertEquals(dadosAgendamento.idMedico(), response.getBody().idMedico());
        assertEquals(dadosAgendamento.data(), response.getBody().data());

    }


//    @Test
//    void deveLancarExcecaoQuandoConsultaForMenosDe30MinutosNoFuturo() {
//        // Configuração: Agendamento para menos de 30 minutos no futuro
//        DadosAgendamentoConsulta dados = new DadosAgendamentoConsulta(
//                1L, 1L, LocalDateTime.now().plusMinutes(10), Especialidade.cardiologia
//        );
//
//        // Execução e verificação
//        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
//            // Passando pelos validadores
//            validadores.forEach(v -> v.validar(dados));
//        });
//
//        // Verificando a mensagem da exceção
//        assertEquals("Consulta deve ser agendada com antecedencia de 30 minutos.", exception.getMessage());
//    }

}

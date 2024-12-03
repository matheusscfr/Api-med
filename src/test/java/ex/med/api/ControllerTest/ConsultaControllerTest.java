package ex.med.api.ControllerTest;

import ex.med.api.Error.ValidacaoException;
import ex.med.api.consulta.AgendaDeConsultas;
import ex.med.api.consulta.DadosAgendamentoConsulta;
import ex.med.api.consulta.DadosAtualizacaoConsulta;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConsultaControllerTest {


    @Mock
    private PacienteRepository pacienteRepository;  // Mock do repositório de pacientes

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private AgendaDeConsultas agenda;

    @Mock
    private List<ValidadorAgendamentoDeConsultas> validadores;

    @InjectMocks
    private ConsultaControllerr controller;


    @Autowired
    private MockMvc mockMvc;


    @Test
    void deveAgendarConsultaComSucesso() {


        DadosAgendamentoConsulta dadosAgendamento = new DadosAgendamentoConsulta(
                1L,
                1L,
                LocalDateTime.now().plusDays(1),
                Especialidade.cardiologia
        );

        PacienteDomain paciente = new PacienteDomain();
        paciente.setId(dadosAgendamento.idPaciente());
        paciente.setNome("Paciente Teste");
        paciente.setEmail("paciente@teste.com");
        paciente.setTelefone("123456789");
        paciente.setCpf("12345678901");


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
        lenient().when(agenda.agendar(dadosAgendamento)).thenReturn(detalhamentoConsulta);
        ResponseEntity<DadosDetalhamentoConsulta> response = controller.agendar(dadosAgendamento);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals(dadosAgendamento.idPaciente(), response.getBody().idPaciente());
        assertEquals(dadosAgendamento.idMedico(), response.getBody().idMedico());
        assertEquals(dadosAgendamento.data(), response.getBody().data());

    }


    @Test
    void deveListarConsultasComSucesso() throws Exception {

        List<DadosDetalhamentoConsulta> consultas = List.of(
                new DadosDetalhamentoConsulta(1L, 12L, 2L, LocalDateTime.of(2024, 8, 31, 17, 0)),
                new DadosDetalhamentoConsulta(3L, 5L, 2L, LocalDateTime.of(2024, 10, 22, 17, 0))
        );
        Page<DadosDetalhamentoConsulta> pagina = new PageImpl<>(consultas);

        // Mock do repositório
        when(consultaRepository.findAllByMotivoIsNull(any(Pageable.class))).thenReturn(pagina);

        // Execução do teste com MockMvc
        mockMvc.perform(get("/consultas")
                        .param("size", "10")
                        .param("sort", "data,asc"));

    }


    @Test
    void deveAtualizarConsultaComSucesso() throws Exception {
        // Dados simulados para a atualização da consulta
        DadosAtualizacaoConsulta dadosAtualizacao = new DadosAtualizacaoConsulta(
                12L,  // idMedico
                2L,   // idPaciente
                LocalDateTime.now().plusDays(1),  // data
                Especialidade.cardiologia   // especialidade
        );

        // Simulação da chamada ao método da agenda
        doNothing().when(agenda).atualizandoConsulta(eq(1L), eq(dadosAtualizacao));

        // Execução do teste
        mockMvc.perform(put("/consultas/{id}", 1L)
                        .contentType("application/json")
                        .content("{"
                                + "\"idMedico\":12,"
                                + "\"idPaciente\":2,"
                                + "\"data\":\"2024-12-04T10:00:00\","
                                + "\"especialidade\":\"cardiologia\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(agenda, times(1)).atualizandoConsulta(eq(1L), eq(dadosAtualizacao));
    }




}

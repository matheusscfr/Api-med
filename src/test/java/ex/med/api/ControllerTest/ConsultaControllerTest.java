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
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


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

        PageRequest paginacao = PageRequest.of(0, 10);

        List<DadosDetalhamentoConsulta> consultas = List.of(
                new DadosDetalhamentoConsulta(1L, 12L, 2L, LocalDateTime.of(2024, 8, 31, 17, 0)),
                new DadosDetalhamentoConsulta(3L, 5L, 2L, LocalDateTime.of(2024, 10, 22, 17, 0))
        );
        Page<DadosDetalhamentoConsulta> consultaPage = new PageImpl<>(consultas, paginacao, consultas.size());


        when(consultaRepository.findAllByMotivoIsNull(paginacao)).thenReturn(consultaPage);

        ResponseEntity<Page<DadosDetalhamentoConsulta>> response = controller.listarMedicos(paginacao);


        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());


        DadosDetalhamentoConsulta primeiraConsulta = response.getBody().getContent().get(0);
        assertEquals(1L, primeiraConsulta.id());
        assertEquals(12L, primeiraConsulta.idMedico());
        assertEquals(2L, primeiraConsulta.idPaciente());
        assertEquals(LocalDateTime.of(2024, 8, 31, 17, 0), primeiraConsulta.data());


        verify(consultaRepository, times(1)).findAllByMotivoIsNull(paginacao);
    }

    @Test
    void deveAtualizarConsultaComSucesso() throws Exception {

        MedicoDomain medico = MedicoDomain.builder()
                .id(12L)
                .nome("Dr. House")
                .email("dr.house@example.com")
                .crm("12345")
                .telefone("123456789")
                .especialidade(Especialidade.cardiologia)
                .ativo(true)
                .build();

        PacienteDomain paciente = PacienteDomain.builder()
                .id(2L)
                .nome("John Doe")
                .email("johndoe@example.com")
                .telefone("987654321")
                .cpf("12345678900")
                .ativo(true)
                .build();


        DadosAtualizacaoConsulta dadosAtualizacao = new DadosAtualizacaoConsulta(
                12L,
                2L,
                LocalDateTime.now().plusDays(1),
                Especialidade.cardiologia
        );

        ConsultaDomain consulta = ConsultaDomain.builder()
                .id(1L)
                .medico(medico)
                .paciente(paciente)
                .data(LocalDateTime.now().plusDays(1))
                .build();


        mockMvc.perform(
                        MockMvcRequestBuilders.put("/consultas/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{"
                                        + "\"idMedico\":12,"
                                        + "\"idPaciente\":2,"
                                        + "\"data\":\"2024-12-04T10:00:00\","
                                        + "\"especialidade\":\"cardiologia\""
                                        + "}")
                )
                .andExpect(status().isOk());

        verify(agenda, times(1)).atualizandoConsulta(eq(1L), any(DadosAtualizacaoConsulta.class));
    }

}

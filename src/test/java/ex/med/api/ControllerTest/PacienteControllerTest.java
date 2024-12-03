package ex.med.api.ControllerTest;

import ex.med.api.controller.MedicoController;
import ex.med.api.controller.PacienteController;
import ex.med.api.domain.PacienteDomain;
import ex.med.api.endereco.DadosEndereco;
import ex.med.api.endereco.Endereco;
import ex.med.api.paciente.DadosAtualizacaoPaciente;
import ex.med.api.paciente.DadosCadastroPaciente;
import ex.med.api.paciente.DadosListagemPaciente;
import ex.med.api.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PacienteControllerTest {


    @Mock
    private PacienteRepository pacienteRepository; // Mock do repositório

    @InjectMocks
    private PacienteController controller; // Controlador a ser testado

    @Test
    void deveCadastrarPacienteComSucesso() {

        DadosCadastroPaciente dadosCadastro = new DadosCadastroPaciente(
                "Maria Silva",
                "maria@gmail.com",
                "999888777",
                "12345678901",
                new DadosEndereco("Rua A", "Bairro B", "12345678", "Cidade C", "UF", "123", "Apto 1")
        );

        PacienteDomain paciente = new PacienteDomain();
        paciente.setId(1L); // Simula o ID gerado
        paciente.setNome(dadosCadastro.nome());
        paciente.setEmail(dadosCadastro.email());
        paciente.setTelefone(dadosCadastro.telefone());
        paciente.setCpf(dadosCadastro.cpf());
        paciente.setEndereco(new Endereco(
                dadosCadastro.endereco().logradouro(),
                dadosCadastro.endereco().bairro(),
                dadosCadastro.endereco().cep(),
                dadosCadastro.endereco().cidade(),
                dadosCadastro.endereco().uf(),
                dadosCadastro.endereco().numero(),
                dadosCadastro.endereco().complemento()
        ));

        when(pacienteRepository.save(any(PacienteDomain.class))).thenAnswer(invocation -> {
            PacienteDomain pacienteSalvo = invocation.getArgument(0);
            pacienteSalvo.setId(1L); // Simula o ID gerado
            return pacienteSalvo;
        });

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        ResponseEntity<?> response = controller.cadastrarPaciente(dadosCadastro, uriBuilder);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof PacienteDomain);

        PacienteDomain pacienteResponse = (PacienteDomain) response.getBody();
        assertEquals(1L, pacienteResponse.getId());
        assertEquals("Maria Silva", pacienteResponse.getNome());
        assertEquals("maria@gmail.com", pacienteResponse.getEmail());
        assertEquals("999888777", pacienteResponse.getTelefone());
        assertEquals("12345678901", pacienteResponse.getCpf());
        assertEquals("Rua A", pacienteResponse.getEndereco().getLogradouro());


        verify(pacienteRepository, times(1)).save(any(PacienteDomain.class));
    }



    @Test
    void deveBuscarPacientePorIdComSucesso() {
        Long pacienteId = 1L;
        PacienteDomain paciente = new PacienteDomain();
        paciente.setId(pacienteId);
        paciente.setNome("Carlos Silva");

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));

        ResponseEntity<?> response = controller.buscarPacientePorId(pacienteId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Optional);

        Optional<PacienteDomain> pacienteResponse = (Optional<PacienteDomain>) response.getBody();
        assertTrue(pacienteResponse.isPresent());
        assertEquals("Carlos Silva", pacienteResponse.get().getNome());

        verify(pacienteRepository, times(1)).findById(pacienteId);
    }

    @Test
    void deveExcluirPacienteComSucesso() {
        Long pacienteId = 1L;
        PacienteDomain paciente = new PacienteDomain();
        paciente.setId(pacienteId);
        paciente.setNome("Maria Oliveira");

        when(pacienteRepository.getReferenceById(pacienteId)).thenReturn(paciente);

        ResponseEntity<String> response = controller.excluirPaciente(pacienteId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Paciente Maria Oliveira deletado com sucesso!", response.getBody());

        verify(pacienteRepository, times(1)).getReferenceById(pacienteId);
    }

    @Test
    void deveAtualizarPacienteComSucesso() {
        DadosAtualizacaoPaciente dadosAtualizacao = new DadosAtualizacaoPaciente(
                1L,
                "Carlos Souza",
                "999887766",
                new DadosEndereco("Rua Nova", "Bairro Novo", "12345678", "Cidade Nova", "UF", "321", "Casa"));

        PacienteDomain paciente = new PacienteDomain();
        paciente.setId(dadosAtualizacao.id());
        paciente.setNome("Carlos Silva");
        // Inicializa o endereço antes de chamar o método
        paciente.setEndereco(
                new Endereco("Rua Antiga", "Bairro Antigo", "98765432", "Cidade Velha", "UF", "123", "Casa"));

        // Simula o retorno do paciente
        when(pacienteRepository.getReferenceById(dadosAtualizacao.id())).thenReturn(paciente);

        // Chama o método para testar
        ResponseEntity<String> response = controller.atualizarMedico(dadosAtualizacao);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Paciente Carlos Souza atualizado com sucesso!", response.getBody());

        // Verifica que o método de atualização do domínio foi chamado
        assertEquals("Carlos Souza", paciente.getNome());
        assertEquals("999887766", paciente.getTelefone());
        assertNotNull(paciente.getEndereco());
        assertEquals("Rua Nova", paciente.getEndereco().getLogradouro());

        verify(pacienteRepository, times(1)).getReferenceById(dadosAtualizacao.id());
    }

    @Test
    void deveListarPacientesComSucesso() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));
        Page<DadosListagemPaciente> page = new PageImpl<>(List.of(
                new DadosListagemPaciente("João Silva", "joao@gmail.com", "11111111111"),
                new DadosListagemPaciente("Ana Souza", "ana@gmail.com", "22222222222")));

        when(pacienteRepository.findAllByAtivoTrue(pageable)).thenReturn(page);

        ResponseEntity<Page<DadosListagemPaciente>> response = controller.listarPacientes(pageable);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        verify(pacienteRepository, times(1)).findAllByAtivoTrue(pageable);
    }
}

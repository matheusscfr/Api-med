package ex.med.api.ControllerTest;

import ex.med.api.controller.MedicoController;
import ex.med.api.controller.PacienteController;
import ex.med.api.domain.PacienteDomain;
import ex.med.api.endereco.DadosEndereco;
import ex.med.api.endereco.Endereco;
import ex.med.api.paciente.DadosCadastroPaciente;
import ex.med.api.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

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
}

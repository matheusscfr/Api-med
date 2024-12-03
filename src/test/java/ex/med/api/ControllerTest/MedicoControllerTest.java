package ex.med.api.ControllerTest;

import ex.med.api.controller.MedicoController;
import ex.med.api.domain.MedicoDomain;
import ex.med.api.endereco.DadosEndereco;
import ex.med.api.endereco.Endereco;
import ex.med.api.medico.*;
import ex.med.api.repository.MedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.print.Pageable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MedicoControllerTest {


    @Mock
    private MedicoRepository repository;

    @InjectMocks
    private MedicoController controller;

    public MedicoControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarMedicoComSucesso() {

        DadosEndereco endereco = new DadosEndereco( "Rua das Flores",
                "Centro",
                "12345678",
                "São Paulo",
                "SP",
                "123",
                "Apto 101" );

        DadosCadastroMedico dados = new DadosCadastroMedico(
                "Dr. João",
                "joao@gmail.com",
                "123456",
                "12345678",
                Especialidade.cardiologia,
                endereco
        );

        MedicoDomain medico = new MedicoDomain(dados);
        medico.setId(1L);

        when(repository.save(any(MedicoDomain.class))).thenReturn(medico);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        ResponseEntity<?> response = controller.cadastrarMedico(dados, uriBuilder);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(repository, times(1)).save(any(MedicoDomain.class));
    }


    @Test
    void deveListarMedicosComSucesso() {
        // Configuração
        PageRequest paginacao =  PageRequest.of(0, 10);
        List<DadosListagemMedicos> medicos = List.of(
                new DadosListagemMedicos(1L, "Dr. João", "joao@gmail.com", "123456", Especialidade.cardiologia),
                new DadosListagemMedicos(2L, "Dra. Maria", "maria@gmail.com", "654321", Especialidade.dermatologia)
        );
        Page<DadosListagemMedicos> medicoPage = new PageImpl<>(medicos, (org.springframework.data.domain.Pageable) paginacao, medicos.size());

        // Mock do repositório
        when(repository.findAllByAtivoTrue((org.springframework.data.domain.Pageable) paginacao)).thenReturn(medicoPage);

        // Execução
        ResponseEntity<Page<DadosListagemMedicos>> response = controller.listarMedicos((org.springframework.data.domain.Pageable) paginacao);

        // Verificações
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());

        // Verificar os dados do primeiro médico
        DadosListagemMedicos primeiroMedico = response.getBody().getContent().get(0);
        assertEquals("Dr. João", primeiroMedico.nome());
        assertEquals("joao@gmail.com", primeiroMedico.email());
        assertEquals("123456", primeiroMedico.crm());
        assertEquals(Especialidade.cardiologia, primeiroMedico.especialidade());

        verify(repository, times(1)).findAllByAtivoTrue((org.springframework.data.domain.Pageable) paginacao);
    }


    @Test
    void deveListarUmMedicoComSucesso() {
        // Configuração
        Long idMedico = 1L;
        MedicoDomain medico = new MedicoDomain();
        medico.setId(idMedico);
        medico.setNome("Dr. João");
        medico.setEmail("joao@gmail.com");
        medico.setCrm("123456");
        medico.setEspecialidade(Especialidade.cardiologia);
        medico.setEndereco(new Endereco("Rua A", "Centro", "12345678", "São Paulo", "SP", "123", null));

        when(repository.getReferenceById(idMedico)).thenReturn(medico);

        ResponseEntity<?> response = controller.ListarUmMedico(idMedico);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof DadosDetalhamentoMedico);
        DadosDetalhamentoMedico detalhes = (DadosDetalhamentoMedico) response.getBody();
        assertEquals("Dr. João", detalhes.nome());
        assertEquals("123456", detalhes.crm());
        verify(repository, times(1)).getReferenceById(idMedico);
    }


    @Test
    void deveRetornarErroAoBuscarMedicoInexistente() {

        Long idMedico = 1L;
        when(repository.getReferenceById(idMedico)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> controller.ListarUmMedico(idMedico));
        verify(repository, times(1)).getReferenceById(idMedico);
    }

    @Test
    void deveAtualizarMedicoComSucesso() {
        Long idMedico = 1L;
        DadosAtualizacaoMedico dados = new DadosAtualizacaoMedico(
                idMedico,
                "Dr. João Atualizado",
                "999999999",
                new DadosEndereco("Rua Nova", "Centro", "12345678", "Cidade X", "UF", "123", "Apto 1")
        );

        MedicoDomain medico = new MedicoDomain();
        medico.setId(idMedico);
        medico.setNome("Dr. João");
        medico.setTelefone("888888888");
        medico.setEndereco(new Endereco("Rua Antiga", "Bairro Antigo", "87654321", "Cidade Y", "UF", "321", null));

        when(repository.getReferenceById(idMedico)).thenReturn(medico);

        ResponseEntity<String> response = controller.atualizarMedico(dados);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Medico Dr. João Atualizado atualizado com sucesso.", response.getBody());
        assertEquals("Dr. João Atualizado", medico.getNome());
        assertEquals("999999999", medico.getTelefone());
        assertEquals("Rua Nova", medico.getEndereco().getLogradouro());

        verify(repository, times(1)).getReferenceById(idMedico);
    }

    @Test
    void deveDeletarMedicoComSucesso() {

        Long idMedico = 1L;
        MedicoDomain medico = new MedicoDomain();
        medico.setId(idMedico);
        medico.setNome("Dr. João");
        medico.setAtivo(true);

        when(repository.getReferenceById(idMedico)).thenReturn(medico);

        ResponseEntity<String> response = controller.deletarMedico(idMedico);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Médico Dr. João deletado com sucesso.", response.getBody());
        assertFalse(medico.getAtivo());

        verify(repository, times(1)).getReferenceById(idMedico);
    }




}

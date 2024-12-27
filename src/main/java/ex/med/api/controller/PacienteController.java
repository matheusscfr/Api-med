package ex.med.api.controller;

import ex.med.api.domain.PacienteDomain;
import ex.med.api.paciente.DadosAtualizacaoPaciente;
import ex.med.api.paciente.DadosCadastroPaciente;
import ex.med.api.paciente.DadosListagemPaciente;
import ex.med.api.repository.PacienteRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {


    private final PacienteRepository pacienteRepository;

    public PacienteController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarPaciente (@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder){
        var paciente = new PacienteDomain(dados);
        pacienteRepository.save(paciente);

        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(paciente);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> listarPacientes(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        var page = pacienteRepository.findAllByAtivoTrue(paginacao);

        return ResponseEntity.ok(page);

    }

    @GetMapping("/{id}")
    public ResponseEntity buscarPacientePorId(@PathVariable Long id){
        var paciente = pacienteRepository.findById(id);
        return ResponseEntity.ok(paciente);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> atualizarPaciente(@RequestBody @Valid DadosAtualizacaoPaciente dados){
        var paciente = pacienteRepository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);
        return ResponseEntity.ok("Paciente " + paciente.getNome() + " atualizado com sucesso!");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluirPaciente(@PathVariable Long id){
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.excluir();
        return ResponseEntity.ok("Paciente " + paciente.getNome() + " deletado com sucesso!");
    }


}

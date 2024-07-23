package ex.med.api.controller;

import ex.med.api.domain.MedicoDomain;
import ex.med.api.medico.DadosAtualizacaoMedico;
import ex.med.api.medico.DadosCadastroMedico;
import ex.med.api.medico.DadosDetalhamentoMedico;
import ex.med.api.medico.DadosListagemMedicos;
import ex.med.api.repository.MedicoRepository;
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
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarMedico(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        var medico = new MedicoDomain(dados);
        repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedicos>> listarMedicos(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        var page =   repository.findAllByAtivoTrue(paginacao);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/{id}")
    public ResponseEntity ListarUmMedico(@PathVariable Long id){
        var page = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(page));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> atualizarMedico(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        return ResponseEntity.ok("Medico "+ medico.getNome() +  " atualizado com sucesso.");
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deletarMedico(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();
        return ResponseEntity.ok("Médico " + medico.getNome() + " deletado com sucesso.");

    }
}

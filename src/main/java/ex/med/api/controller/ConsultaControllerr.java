package ex.med.api.controller;

import ex.med.api.Error.ValidacaoException;
import ex.med.api.consulta.*;
import ex.med.api.repository.ConsultaRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaControllerr {

    @Autowired
    private AgendaDeConsultas agenda;

    @Autowired
    private ConsultaRepository consultaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados){
        var dto = agenda.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoConsulta>> listarMedicos(@PageableDefault(size = 10, sort = {"data"})Pageable paginacao){
        var page = consultaRepository.findAllByMotivoIsNull(paginacao);
        System.out.println(page);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizarConsulta(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoConsulta dados) {
        agenda.atulizandoConsulta(id, dados);
        return ResponseEntity.ok().build();
    }




    @DeleteMapping
    @Transactional
    public ResponseEntity remover(@RequestBody @Valid DadosCancelamentoConsulta dados){
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}

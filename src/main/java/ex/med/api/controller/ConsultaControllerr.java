package ex.med.api.controller;

import ex.med.api.consulta.AgendaDeConsultas;
import ex.med.api.consulta.DadosAgendamentoConsulta;
import ex.med.api.consulta.DadosCancelamentoConsulta;
import ex.med.api.consulta.DadosDetalhamentoConsulta;
import ex.med.api.repository.ConsultaRepository;
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

    @DeleteMapping
    @Transactional
    public ResponseEntity remover(@RequestBody @Valid DadosCancelamentoConsulta dados){
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}

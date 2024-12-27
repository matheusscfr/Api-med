package ex.med.api.controller;


import ex.med.api.consulta.*;
import ex.med.api.consulta.prescricao.Prescricao;
import ex.med.api.domain.ConsultaDomain;
import ex.med.api.repository.ConsultaRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaControllerr {


    private final AgendaDeConsultas agenda;

    private final Prescricao prescricao;

    private final ConsultaRepository consultaRepository;

    private final PlanilhaService planilhaService;

    public ConsultaControllerr(AgendaDeConsultas agenda, ConsultaRepository consultaRepository, PlanilhaService planilhaService, Prescricao prescricao) {
        this.agenda = agenda;
        this.prescricao = prescricao;
        this.consultaRepository = consultaRepository;
        this.planilhaService = planilhaService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados){
        var dto = agenda.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "prescricao")
    @Transactional
    public ResponseEntity cadastrarPrescricao(@RequestBody @Valid DadosPrescricaoConsulta dados){
        prescricao.salvarPrescricao(dados);
        return ResponseEntity.ok("Prescricao cadastrado com sucesso");
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoConsulta>> listarMedicos(@PageableDefault(size = 10, sort = {"data"})Pageable paginacao){
        var page = consultaRepository.findAllByMotivoIsNull(paginacao);
        return ResponseEntity.ok(page);
    }


    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarConsultas() throws IOException {
        List<ConsultaDomain> consultas = consultaRepository.findAll();
        ByteArrayInputStream in = planilhaService.gerarPlanilha(consultas);

        byte[] bytes = in.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=consultas.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizarConsulta(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoConsulta dados) {
        agenda.atualizandoConsulta(id, dados);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping
    @Transactional
    public ResponseEntity remover(@RequestBody @Valid DadosCancelamentoConsulta dados){
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}

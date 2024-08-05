package ex.med.api.controller;

import ex.med.api.consulta.feedbacks.DadosDetalhamentoFeedback;
import ex.med.api.consulta.feedbacks.DadosDetalhamentoMedicoPorId;
import ex.med.api.consulta.feedbacks.DadosFeedback;
import ex.med.api.consulta.feedbacks.FeedbackService;
import ex.med.api.domain.FeedbackDomain;
import ex.med.api.repository.FeedbackRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("consultas/feedback")
@SecurityRequirement(name = "bearer-key")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;


    @PostMapping
    @Transactional
    public ResponseEntity criarFeedback (@RequestBody @Valid DadosFeedback dados){
        var feedback = feedbackService.criarFeedback(dados);

        return ResponseEntity.ok(feedback);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoFeedback>> listarFeedback (@PageableDefault(size = 10) Pageable pageable){
        var page = feedbackRepository.findAllFeedback(pageable);

        return ResponseEntity.ok(page);

    }

    @GetMapping("/{idMedico}")
    public ResponseEntity<Page<DadosDetalhamentoMedicoPorId>> listarporId (@PathVariable Long idMedico,@PageableDefault(size = 10) Pageable pageable){
        var page = feedbackRepository.findByMedicoId(idMedico, pageable);
        return ResponseEntity.ok(page);
    }



    @DeleteMapping("/{id_feedback}")
    @Transactional
    ResponseEntity deletandoFeedback (@PathVariable Long id_feedback){
        feedbackService.apagarFeedback(id_feedback);

        return ResponseEntity.ok("Feedback deletado com sucesso!");

    }


}

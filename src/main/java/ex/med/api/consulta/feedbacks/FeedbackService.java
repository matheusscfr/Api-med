package ex.med.api.consulta.feedbacks;

import ex.med.api.Error.ValidacaoException;
import ex.med.api.domain.FeedbackDomain;
import ex.med.api.repository.ConsultaRepository;
import ex.med.api.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public DadosDetalhamentoFeedback criarFeedback(DadosFeedback dados) {
        var id_consulta =  consultaRepository.getReferenceById(dados.idConsulta());
        var data_agora = LocalDateTime.now();
        var diferencaHora = Duration.between(data_agora, LocalDateTime.now()).toMinutes();

        if(!consultaRepository.existsById(dados.idConsulta())){
            throw new ValidacaoException("Id da consulta não encontrado.");
        }else if(diferencaHora < 30){
            throw new ValidacaoException("Você não participou desta consulta ainda.");
        }

        var new_feedback = new FeedbackDomain(null, id_consulta, dados.comentario(), dados.nota());
        feedbackRepository.save(new_feedback);

        return new DadosDetalhamentoFeedback(new_feedback);
    }

    public void apagarFeedback(Long id_feedback) {
        if(!feedbackRepository.existsById(id_feedback)){
            throw new ValidacaoException("Id do feedback não existe");
        }
        feedbackRepository.deleteById(id_feedback);

    }



}

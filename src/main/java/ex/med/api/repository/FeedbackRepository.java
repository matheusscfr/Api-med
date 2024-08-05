package ex.med.api.repository;


import ex.med.api.consulta.feedbacks.DadosDetalhamentoFeedback;
import ex.med.api.consulta.feedbacks.DadosDetalhamentoMedicoPorId;
import ex.med.api.domain.FeedbackDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<FeedbackDomain, Long> {

    @Query("SELECT new ex.med.api.consulta.feedbacks.DadosDetalhamentoFeedback(c.id, c.consulta.id, c.comentario, c.nota ) FROM feedback c")
    Page<DadosDetalhamentoFeedback> findAllFeedback(Pageable pageable);

    @Query("SELECT new ex.med.api.consulta.feedbacks.DadosDetalhamentoMedicoPorId(f.id , f.consulta.id, f.comentario, f.nota, m.nome) FROM feedback f JOIN consulta c ON f.consulta.id = c.id JOIN  medicos m ON c.medico.id = m.id WHERE c.medico.id = :id")
   Page<DadosDetalhamentoMedicoPorId> findByMedicoId(@Param("id") Long id, Pageable pageable);
}

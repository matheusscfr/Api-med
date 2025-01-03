package ex.med.api.repository;

import ex.med.api.consulta.DadosDetalhamentoConsulta;
import ex.med.api.domain.ConsultaDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;


import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<ConsultaDomain, Long> {
    boolean existsByMedicoIdAndData(Long idMedico, LocalDateTime data);

    boolean existsByPacienteIdAndDataBetween(Long aLong, LocalDateTime primeiroHorario, LocalDateTime ultimoHorario);

    @Query("SELECT new ex.med.api.consulta.DadosDetalhamentoConsulta(c.id, c.medico.id, c.paciente.id, c.data) FROM consulta c WHERE c.motivoCacelamento IS NULL")
    Page<DadosDetalhamentoConsulta> findAllByMotivoIsNull(Pageable paginacao);



}

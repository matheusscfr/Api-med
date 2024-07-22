package ex.med.api.repository;


import ex.med.api.domain.PacienteDomain;
import ex.med.api.paciente.DadosListagemPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<PacienteDomain, Long> {
    Page<DadosListagemPaciente> findAllByAtivoTrue(Pageable paginacao);
}

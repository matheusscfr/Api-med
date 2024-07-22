package ex.med.api.repository;

import ex.med.api.domain.MedicoDomain;
import ex.med.api.medico.DadosListagemMedicos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<MedicoDomain, Long> {
    Page<DadosListagemMedicos> findAllByAtivoTrue(Pageable paginacao);
}

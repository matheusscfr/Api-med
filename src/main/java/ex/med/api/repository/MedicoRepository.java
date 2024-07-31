package ex.med.api.repository;

import ex.med.api.domain.MedicoDomain;
import ex.med.api.medico.DadosListagemMedicos;
import ex.med.api.medico.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface MedicoRepository extends JpaRepository<MedicoDomain, Long> {
    Page<DadosListagemMedicos> findAllByAtivoTrue(Pageable paginacao);

    @Query("select m from medicos m where m.ativo = true and m.especialidade = :especialidade and m.id not in( select c.medico.id from consulta c where c.data = :data) order by random() limit 1")
    MedicoDomain escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);

    @Query("select m.ativo from medicos m where m.id = :id")
    Boolean findAtivoById(Long id);
}

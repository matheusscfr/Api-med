package ex.med.api.repository;


import ex.med.api.domain.PrescricaoDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescricaoRepository  extends JpaRepository<PrescricaoDomain, Long> {
}

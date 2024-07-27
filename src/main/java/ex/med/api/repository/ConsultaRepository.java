package ex.med.api.repository;

import ex.med.api.domain.ConsultaDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<ConsultaDomain, Long> {
}

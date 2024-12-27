package ex.med.api.repository;

import ex.med.api.domain.ItemPrescricaoDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPrescricaoRepository  extends JpaRepository<ItemPrescricaoDomain, Long> {
}

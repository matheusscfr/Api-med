package ex.med.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Prescricao")
@Table(name = "Prescricao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescricaoDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private ConsultaDomain consulta;

    @OneToMany(mappedBy = "prescricao")
    private List<ItemPrescricaoDomain> itensPrescricao;

}

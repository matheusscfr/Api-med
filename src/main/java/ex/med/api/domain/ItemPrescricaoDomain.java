package ex.med.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "ItemPrescricao")
@Entity(name = "ItemPrescricao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemPrescricaoDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicamento;

    private String dosagem;

    private String frequencia;

    @ManyToOne
    @JoinColumn(name = "id_prescricao")
    private PrescricaoDomain prescricao;


}

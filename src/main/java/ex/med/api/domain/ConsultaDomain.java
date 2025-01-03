package ex.med.api.domain;


import ex.med.api.consulta.DadosAtualizacaoConsulta;
import ex.med.api.consulta.DadosDetalhamentoConsulta;
import ex.med.api.consulta.MotivoCacelamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Table(name = "consulta")
@Entity(name = "consulta")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ConsultaDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private MedicoDomain medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private PacienteDomain paciente;

    private LocalDateTime data;

    @Column(name = "motivo_cancelamento")
    @Enumerated(EnumType.STRING)
    private MotivoCacelamento motivoCacelamento;

    public void cancelar(MotivoCacelamento motivo){
        this.motivoCacelamento = motivo;
    }



}

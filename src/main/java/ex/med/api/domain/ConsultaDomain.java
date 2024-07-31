package ex.med.api.domain;


import ex.med.api.consulta.DadosDetalhamentoConsulta;
import ex.med.api.consulta.MotivoCacelamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "consulta")
@Entity(name = "consulta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ConsultaDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
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

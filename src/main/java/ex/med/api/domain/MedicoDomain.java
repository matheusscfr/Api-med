package ex.med.api.domain;


import ex.med.api.endereco.Endereco;
import ex.med.api.medico.DadosAtualizacaoMedico;
import ex.med.api.medico.DadosCadastroMedico;
import ex.med.api.medico.Especialidade;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "medicos")
@Entity(name = "medicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class MedicoDomain {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String crm;
    private String telefone;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;

    private Boolean ativo;

    public MedicoDomain(DadosCadastroMedico dados) {
        this.ativo = true;
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.crm = dados.crm();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
    }

    public void atualizarInformacoes(DadosAtualizacaoMedico dados) {

        if(dados.nome() != null ) {
            this.nome = dados.nome();
        }

        if(dados.telefone() != null ) {
            this.telefone = dados.telefone();
        }

        if(dados.endereco() != null ) {
            this.endereco.atualizarInformacoes(dados.endereco());
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}

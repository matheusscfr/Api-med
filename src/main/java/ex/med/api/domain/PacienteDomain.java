package ex.med.api.domain;

import ex.med.api.endereco.Endereco;
import ex.med.api.paciente.DadosAtualizacaoPaciente;
import ex.med.api.paciente.DadosCadastroPaciente;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "pacientes")
@Entity(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class PacienteDomain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    @Embedded
    private Endereco endereco;

    private boolean ativo;

    public PacienteDomain(DadosCadastroPaciente dados){
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
        this.endereco = new Endereco((dados.endereco()));
        this.ativo = true;
    }

    public void atualizarInformacoes(DadosAtualizacaoPaciente dados){

        if(dados.nome() != null){
            this.nome = dados.nome();
        }

        if(dados.telefone() != null){
            this.telefone = dados.telefone();
        }
        if(dados.endereco() != null){
            this.endereco.atualizarInformacoes(dados.endereco());
        }

    }

    public void excluir(){
        this.ativo = false;
    }
}

package ex.med.api.medico;

import ex.med.api.domain.MedicoDomain;
import ex.med.api.endereco.Endereco;

public record DadosDetalhamentoMedico(Long id, String nome, String email, String crm, Especialidade especialidade, Endereco endereco) {
    public DadosDetalhamentoMedico(MedicoDomain medicoDomain) {
        this(medicoDomain.getId(), medicoDomain.getNome(), medicoDomain.getEmail(), medicoDomain.getCrm(), medicoDomain.getEspecialidade(), medicoDomain.getEndereco());
    }
}

package ex.med.api.medico;

import ex.med.api.domain.MedicoDomain;

public record DadosListagemMedicos(
        Long id,
        String nome,
        String email,
        String crm,
        Especialidade especialidade
) {

    public DadosListagemMedicos(MedicoDomain medicoDomain){
        this(medicoDomain.getId(), medicoDomain.getNome(), medicoDomain.getEmail(), medicoDomain.getCrm(), medicoDomain.getEspecialidade());
    }
}

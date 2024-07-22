package ex.med.api.paciente;

import ex.med.api.domain.PacienteDomain;

public record DadosListagemPaciente(
        String nome,
        String email,
        String cpf
) {
    public DadosListagemPaciente(PacienteDomain paciente) {
        this(paciente.getNome(), paciente.getEmail(), paciente.getCpf());
    }
}

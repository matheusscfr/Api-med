package ex.med.api.consulta;

import ex.med.api.consulta.validacoes.DadosItemPrescricao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DadosPrescricaoConsulta(
        @NotNull(message = "O id da consulta não pode ser nulo.")
         Long idConsulta,
         @Valid
         @NotNull(message = "O itens de prescricao não podem ser nulos.")
         List<DadosItemPrescricao> itensPrescricao

) {
}

package ex.med.api.consulta.prescricao;

import ex.med.api.Error.ValidacaoException;
import ex.med.api.consulta.DadosPrescricaoConsulta;
import ex.med.api.consulta.validacoes.DadosItemPrescricao;
import ex.med.api.domain.ConsultaDomain;
import ex.med.api.domain.ItemPrescricaoDomain;
import ex.med.api.domain.PrescricaoDomain;
import ex.med.api.repository.ConsultaRepository;
import ex.med.api.repository.ItemPrescricaoRepository;
import ex.med.api.repository.PrescricaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Prescricao {

    private final PrescricaoRepository prescricaoRepository;

    private final ItemPrescricaoRepository itemPrescricaoRepository;

    private final ConsultaRepository consultaRepository;

    public Prescricao(PrescricaoRepository prescricaoRepository, ItemPrescricaoRepository itemPrescricaoRepository, ConsultaRepository consultaRepository) {
        this.prescricaoRepository = prescricaoRepository;
        this.itemPrescricaoRepository = itemPrescricaoRepository;
        this.consultaRepository = consultaRepository;
    }

    public void salvarPrescricao(DadosPrescricaoConsulta dadosPrescricaoConsulta) {
       ConsultaDomain procurarConsulta = consultaRepository.findById(dadosPrescricaoConsulta.idConsulta())
               .orElseThrow(() -> new ValidacaoException("Consulta não encontrada"));

        List<ItemPrescricaoDomain> listaItensPrescricao = salvarListaItensPrescricao(dadosPrescricaoConsulta.itensPrescricao());
        PrescricaoDomain novaPrescricao =  PrescricaoDomain.builder()
                .consulta(procurarConsulta)
                .itensPrescricao(listaItensPrescricao)
                .build();

        prescricaoRepository.save(novaPrescricao);

        for (ItemPrescricaoDomain item : listaItensPrescricao) {
            item.setPrescricao(novaPrescricao);
        }

    }

    public List<ItemPrescricaoDomain> salvarListaItensPrescricao(List<DadosItemPrescricao> listaItensPrescricao){

      return  itemPrescricaoRepository.saveAll(listaItensPrescricao.stream()
              .map(dadosItem -> ItemPrescricaoDomain.builder()
                      .medicamento(dadosItem.medicamento())
                      .dosagem(dadosItem.dosagem())
                      .frequencia(dadosItem.frequencia())
                      .build()
              ).collect(Collectors.toList()));
    }
}

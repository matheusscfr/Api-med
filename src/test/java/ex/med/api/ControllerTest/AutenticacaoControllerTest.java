package ex.med.api.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.med.api.usuario.DadosAutenticacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Dados de autenticação para o teste
    private DadosAutenticacao dadosAutenticacao;

    @BeforeEach
    void setUp() {
        dadosAutenticacao = new DadosAutenticacao("matheus.ferreira@gmail.com", "12345678");
    }

    @Test
    void deveRetornarTokenJWTQuandoAutenticacaoForValida() throws Exception {
        mockMvc.perform((org.springframework.test.web.servlet.RequestBuilder) post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.valueOf(objectMapper.writeValueAsString(dadosAutenticacao))))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.token").isNotEmpty());
    }
}

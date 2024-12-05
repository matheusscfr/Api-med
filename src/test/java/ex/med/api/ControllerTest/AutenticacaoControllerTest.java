package ex.med.api.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.med.api.usuario.DadosAutenticacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public AutenticacaoControllerTest() {
        MockitoAnnotations.openMocks(this);
    }


    // Dados de autenticação para o teste
    private DadosAutenticacao dadosAutenticacao;

    @BeforeEach
    void setUp() {
        dadosAutenticacao = new DadosAutenticacao("matheus.ferreira@gmail.com", "12345678");
    }

    @Test
    void deveRetornarTokenJWTQuandoAutenticacaoForValida() throws Exception {
        String payload = """
        {
            "login": "matheus.ferreira@gmail.com",
            "senha": "12345678"
        }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

}

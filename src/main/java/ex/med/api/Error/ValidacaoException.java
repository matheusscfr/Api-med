package ex.med.api.Error;

public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String idNãoEncontrado) {
        super(idNãoEncontrado);
    }
}

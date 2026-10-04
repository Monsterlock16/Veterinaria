package repositorio;

public class RepositorioException extends RuntimeException {

    public RepositorioException(String mensaje) {
        super(mensaje);
    }

    public RepositorioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
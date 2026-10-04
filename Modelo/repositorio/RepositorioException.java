package repositorio;

public class RepositorioException extends RuntimeException {

    public RepositorioException(String mensaje) {
        super(mensaje);
    }

    public RepositorioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public static RepositorioException de(Throwable causa) {
        String mensaje = causa == null ? "Error de acceso a datos." : "Error de acceso a datos: " + causa.getMessage();
        return new RepositorioException(mensaje, causa);
    }
}
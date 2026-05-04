package dominio.excepciones;

/**
 * Excepción personalizada para errores de dominio del juego DOPO.
 *
 * @authors Francisco Gomez, Oscar Lopez
 */
import java.util.logging.Level;
import java.util.logging.Logger;

public class DOPOGameException extends Exception {
    private static final Logger LOG = Logger.getLogger(DOPOGameException.class.getName());

    /**
     * Códigos de error identificados para cada tipo de falla.
     */
    public enum Codigo {
        CONFIG_NO_ENCONTRADA(100),
        CONFIG_FORMATO_INVALIDO(101),
        NIVEL_SIN_ZONA_INICIO(200),
        NIVEL_SIN_ZONA_FIN(201),
        MOVIMIENTO_INVALIDO(300);

        private final int valor;

        /**
         * Construye el código con su valor numérico asociado.
         *
         * @param v valor numérico del código de error
         */
        Codigo(int v) { this.valor = v; }

        /**
         * Retorna el valor numérico del código de error.
         *
         * @return valor entero del código
         */
        public int getValor() { return valor; }
    }

    private final Codigo codigo;

    /**
     * Construye la excepción con un código y mensaje descriptivo.
     *
     * @param codigo  código de error del enumerado {@link Codigo}
     * @param mensaje descripción del error ocurrido
     */
    public DOPOGameException(Codigo codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
        LOG.log(Level.SEVERE, "[DOPO-{0}] {1}", new Object[]{codigo.getValor(), mensaje});
    }

    /**
     * Construye la excepción con un código, mensaje y causa raíz.
     *
     * @param codigo  código de error del enumerado {@link Codigo}
     * @param mensaje descripción del error ocurrido
     * @param causa   excepción original que provocó este error
     */
    public DOPOGameException(Codigo codigo, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigo = codigo;
        LOG.log(Level.SEVERE, "[DOPO-{0}] {1}", new Object[]{codigo.getValor(), mensaje});
    }

    /**
     * Retorna el código de error asociado a esta excepción.
     *
     * @return código de error
     */
    public Codigo getCodigo() { return codigo; }

    /**
     * Retorna una representación textual de la excepción con su código numérico.
     *
     * @return cadena con el código y mensaje de error
     */
    @Override
    public String toString() {
        return "[DOPO-" + codigo.getValor() + "] " + getMessage();
    }
}

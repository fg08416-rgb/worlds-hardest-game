package dominio.objetivos;

/**
 * Clase base abstracta para las monedas que el jugador puede recolectar.
 *
 * @authors Francisco Gomez, Oscar Lopez
 */
public abstract class Moneda {
    protected int fila;
    protected int columna;
    protected boolean recolectada;

    /**
     * Construye una moneda en la posición indicada, marcada como no recolectada.
     *
     * @param fila    fila donde se ubica la moneda en el tablero
     * @param columna columna donde se ubica la moneda en el tablero
     */
    public Moneda(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.recolectada = false;
    }

    /**
     * Marca esta moneda como recolectada.
     */
    public void recolectar() {
        recolectada = true;
    }

    /**
     * Indica si la moneda ya fue recolectada por el jugador.
     *
     * @return true si la moneda fue recogida, false en caso contrario
     */
    public boolean estaRecolectada() { return recolectada; }

    /**
     * Retorna la fila de la moneda en el tablero.
     *
     * @return fila de la moneda
     */
    public int getFila()    { return fila; }

    /**
     * Retorna la columna de la moneda en el tablero.
     *
     * @return columna de la moneda
     */
    public int getColumna() { return columna; }

    /**
     * Retorna el color representativo de la moneda en formato hexadecimal.
     *
     * @return cadena de color hexadecimal (ej. "#EF9F27")
     */
    public abstract String getColor();
}

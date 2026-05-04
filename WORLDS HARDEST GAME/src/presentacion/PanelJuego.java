package presentacion;

/**
 * Panel Swing que dibuja el estado visual del nivel en cada ciclo de juego.
 *
 * @authors Francisco Gomez, Oscar Lopez
 */
import dominio.enemigos.Enemigo;
import dominio.juego.Nivel;
import dominio.juego.Tablero;
import dominio.juego.TipoCelda;
import dominio.modos.ModoPlayer;
import dominio.objetivos.Moneda;
import dominio.personajes.Cuadrado;

import javax.swing.*;
import java.awt.*;

public class PanelJuego extends JPanel {

    /** Tamaño en píxeles de cada celda del tablero. */
    public static final int TAM_CELDA = 40;

    private static final Color COLOR_FONDO        = new Color(175, 185, 225);
    private static final Color COLOR_ZONA_SEGURA  = new Color(150, 210, 150);
    private static final Color COLOR_TABLERO_A    = new Color(230, 230, 230);
    private static final Color COLOR_TABLERO_B    = new Color(200, 200, 215);
    private static final Color COLOR_BORDE_ZONA   = new Color(30,  30,  30);
    private static final Color COLOR_MONEDA       = new Color(230, 190,  30);
    private static final Color COLOR_ENEMIGO      = new Color(50,  100, 200);

    private Nivel nivel;
    private ModoPlayer modo;

    /**
     * Construye el panel de juego ajustando su tamaño preferido al tablero del nivel.
     *
     * @param nivel nivel cuyo estado se renderizará
     * @param modo  modo player que provee acceso al jugador actual
     */
    public PanelJuego(Nivel nivel, ModoPlayer modo) {
        this.nivel = nivel;
        this.modo  = modo;
        Tablero t = nivel.getTablero();
        setPreferredSize(new Dimension(t.getColumnas() * TAM_CELDA, t.getFilas() * TAM_CELDA));
        setBackground(COLOR_FONDO);
    }

    /**
     * Renderiza el tablero, monedas, enemigos y jugador en cada llamada de repintado.
     *
     * @param g contexto gráfico proporcionado por Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Tablero tablero = nivel.getTablero();

        g2.setColor(COLOR_FONDO);
        g2.fillRect(0, 0, getWidth(), getHeight());

        for (int f = 0; f < tablero.getFilas(); f++) {
            for (int c = 0; c < tablero.getColumnas(); c++) {
                TipoCelda tipo = tablero.getCelda(f, c).getTipo();
                int px = c * TAM_CELDA;
                int py = f * TAM_CELDA;
                switch (tipo) {
                    case ZONA_INICIAL, ZONA_FINAL -> {
                        g2.setColor(COLOR_ZONA_SEGURA);
                        g2.fillRect(px, py, TAM_CELDA, TAM_CELDA);
                    }
                    case LIBRE -> {
                        g2.setColor((f + c) % 2 == 0 ? COLOR_TABLERO_A : COLOR_TABLERO_B);
                        g2.fillRect(px, py, TAM_CELDA, TAM_CELDA);
                    }
                }
            }
        }

        g2.setColor(COLOR_BORDE_ZONA);
        g2.setStroke(new BasicStroke(2f));
        for (int f = 0; f < tablero.getFilas(); f++) {
            for (int c = 0; c < tablero.getColumnas(); c++) {
                if (tablero.getCelda(f, c).getTipo() == TipoCelda.PARED) continue;
                int px = c * TAM_CELDA, py = f * TAM_CELDA;
                if (f == 0 || tablero.getCelda(f-1,c).getTipo()==TipoCelda.PARED)
                    g2.drawLine(px, py, px+TAM_CELDA, py);
                if (f==tablero.getFilas()-1 || tablero.getCelda(f+1,c).getTipo()==TipoCelda.PARED)
                    g2.drawLine(px, py+TAM_CELDA, px+TAM_CELDA, py+TAM_CELDA);
                if (c==0 || tablero.getCelda(f,c-1).getTipo()==TipoCelda.PARED)
                    g2.drawLine(px, py, px, py+TAM_CELDA);
                if (c==tablero.getColumnas()-1 || tablero.getCelda(f,c+1).getTipo()==TipoCelda.PARED)
                    g2.drawLine(px+TAM_CELDA, py, px+TAM_CELDA, py+TAM_CELDA);
            }
        }

        for (Moneda m : nivel.getMonedas()) {
            if (!m.estaRecolectada()) {
                int cx = m.getColumna()*TAM_CELDA + TAM_CELDA/2;
                int cy = m.getFila()*TAM_CELDA + TAM_CELDA/2;
                g2.setColor(COLOR_MONEDA);
                g2.fillOval(cx-7, cy-7, 14, 14);
                g2.setColor(COLOR_MONEDA.darker());
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(cx-7, cy-7, 14, 14);
            }
        }

        g2.setStroke(new BasicStroke(1.5f));
        for (Enemigo e : nivel.getEnemigos()) {
            int cx = e.getColumna()*TAM_CELDA + TAM_CELDA/2;
            int cy = e.getFila()*TAM_CELDA + TAM_CELDA/2;
            g2.setColor(COLOR_ENEMIGO);
            g2.fillOval(cx-10, cy-10, 20, 20);
            g2.setColor(COLOR_ENEMIGO.darker());
            g2.drawOval(cx-10, cy-10, 20, 20);
        }

        Cuadrado jugador = modo.getJugador();
        int jx = jugador.getColumna()*TAM_CELDA + 4;
        int jy = jugador.getFila()*TAM_CELDA + 4;
        int tam = TAM_CELDA - 8;
        g2.setColor(Color.decode(jugador.getColor()));
        g2.fillRect(jx, jy, tam, tam);
        g2.setColor(Color.decode(jugador.getColor()).darker());
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(jx, jy, tam, tam);
    }
}

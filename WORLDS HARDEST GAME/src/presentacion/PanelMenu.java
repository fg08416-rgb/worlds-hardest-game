package presentacion;

/**
 * Panel Swing que muestra el menú principal del juego con título y botón de inicio.
 *
 * @authors Francisco Gomez, Oscar Lopez
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelMenu extends JPanel {

    /**
     * Interfaz de callback para notificar eventos del menú al componente padre.
     */
    public interface MenuListener {

        /**
         * Se invoca cuando el jugador presiona el botón para iniciar la partida.
         */
        void onPlayGame();
    }

    private MenuListener listener;

    /**
     * Construye el panel de menú y registra el listener de eventos de menú.
     *
     * @param listener objeto que recibirá las notificaciones de acción del menú
     */
    public PanelMenu(MenuListener listener) {
        this.listener = listener;
        setBackground(new Color(180, 190, 230));
        setLayout(new GridBagLayout());
        construirUI();
    }

    /**
     * Construye y agrega todos los componentes visuales del menú al panel.
     */
    private void construirUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(10, 0, 10, 0);

        JLabel sub = new JLabel("THE WORLD'S...");
        sub.setFont(new Font("Arial Black", Font.BOLD, 18));
        sub.setForeground(new Color(30, 30, 120));
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        add(sub, gbc);

        JLabel titulo = new JLabel("HARDEST GAME");
        titulo.setFont(new Font("Arial Black", Font.BOLD, 52));
        titulo.setForeground(new Color(30, 60, 180));
        titulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 100), 3),
            BorderFactory.createEmptyBorder(2, 10, 2, 10)
        ));
        gbc.gridy = 1;
        add(titulo, gbc);

        JLabel version = new JLabel("VERSION 1.0");
        version.setFont(new Font("Arial Black", Font.BOLD, 14));
        version.setForeground(new Color(30, 30, 120));
        gbc.gridy = 2;
        add(version, gbc);

        gbc.gridy = 3;
        add(Box.createVerticalStrut(20), gbc);

        JButton btnPlay = crearBoton("PLAY GAME", new Color(220, 30, 30));
        btnPlay.addActionListener(e -> listener.onPlayGame());
        gbc.gridy = 4;
        add(btnPlay, gbc);

        JLabel creditos = new JLabel("THE DOPO HARDEST GAME - 2026");
        creditos.setFont(new Font("Arial", Font.PLAIN, 11));
        creditos.setForeground(new Color(60, 60, 100));
        gbc.gridy = 5;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(creditos, gbc);
    }

    /**
     * Crea un botón con estilo personalizado de esquinas redondeadas y color sólido.
     *
     * @param texto texto que mostrará el botón
     * @param color color de fondo base del botón
     * @return botón configurado con el estilo del juego
     */
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(color.darker().darker());
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
            }
        };
        btn.setFont(new Font("Arial Black", Font.BOLD, 28));
        btn.setPreferredSize(new Dimension(260, 60));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

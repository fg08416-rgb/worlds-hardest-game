package presentacion;

/**
 * Ventana principal del juego que gestiona la navegación entre menú y pantalla de juego,
 * así como el game loop basado en temporizadores Swing.
 *
 * @authors Francisco Gomez, Oscar Lopez
 */
import dominio.juego.Nivel;
import dominio.juego.Direccion;
import dominio.modos.ModoPlayer;
import dominio.persistencia.LectorConfiguracion;
import dominio.personajes.CuadradoRojo;
import dominio.excepciones.DOPOGameException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaPrincipal extends JFrame {

    private static final Color HUD_BG   = new Color(30, 30, 30);
    private static final Color HUD_FG   = Color.WHITE;
    private static final Color BAR_BG   = new Color(20, 20, 20);
    private static final Font  HUD_FONT = new Font("Arial Black", Font.BOLD, 13);

    private CardLayout cardLayout;
    private JPanel contenedor;

    private Nivel nivel;
    private ModoPlayer modo;
    private PanelJuego panelJuego;
    private JLabel lblMuertes;
    private Timer timerLogica;
    private Timer timerSegundo;
    private boolean pausado = false;

    /**
     * Construye la ventana principal configurando el CardLayout y mostrando el menú inicial.
     */
    public VentanaPrincipal() {
        setTitle("The DOPO Hardest Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);
        PanelMenu menu = new PanelMenu(this::iniciarJuego);
        contenedor.add(menu, "MENU");
        add(contenedor);
        pack();
        setMinimumSize(new Dimension(640, 400));
        setLocationRelativeTo(null);
        cardLayout.show(contenedor, "MENU");
    }

    /**
     * Carga el nivel, crea el jugador y arranca el game loop.
     * Se invoca al presionar el botón de juego en el menú.
     */
    private void iniciarJuego() {
        try {
            LectorConfiguracion lector = new LectorConfiguracion();
            nivel = lector.cargar("recursos/niveles/nivel1.txt");

            CuadradoRojo jugador = new CuadradoRojo(nivel.getFilaSpawn(), nivel.getColumnaSpawn());
            modo = new ModoPlayer(jugador, nivel);
            modo.iniciar();
            construirPantallaJuego();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int maxW = screenSize.width  - 100;
            int maxH = screenSize.height - 100;
            pack();
            if (getWidth() > maxW || getHeight() > maxH) {
                setSize(Math.min(getWidth(), maxW), Math.min(getHeight(), maxH));
            }

            setLocationRelativeTo(null);
            cardLayout.show(contenedor, "JUEGO");
            panelJuego.requestFocusInWindow();
            iniciarGameLoop();
        } catch (DOPOGameException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Construye el panel de juego con HUD superior, área de juego con scroll y barra inferior,
     * y lo registra en el CardLayout con la clave "JUEGO".
     */
    private void construirPantallaJuego() {
        JPanel pantalla = new JPanel(new BorderLayout());

        JPanel hud = new JPanel(new BorderLayout());
        hud.setBackground(HUD_BG);
        hud.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        JButton btnMenu = new JButton("MENU");
        btnMenu.setFont(HUD_FONT); btnMenu.setForeground(HUD_FG);
        btnMenu.setBackground(HUD_BG); btnMenu.setBorderPainted(false);
        btnMenu.setFocusPainted(false);
        btnMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMenu.addActionListener(e -> volverAlMenu());

        JLabel lblNivel = new JLabel("1/30", SwingConstants.CENTER);
        lblNivel.setForeground(HUD_FG); lblNivel.setFont(HUD_FONT);

        lblMuertes = new JLabel("DEATHS: 0", SwingConstants.RIGHT);
        lblMuertes.setForeground(HUD_FG); lblMuertes.setFont(HUD_FONT);

        hud.add(btnMenu,    BorderLayout.WEST);
        hud.add(lblNivel,   BorderLayout.CENTER);
        hud.add(lblMuertes, BorderLayout.EAST);

        panelJuego = new PanelJuego(nivel, modo);
        JScrollPane scroll = new JScrollPane(panelJuego,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(panelJuego.getBackground());

        JPanel barraInf = new JPanel(new BorderLayout());
        barraInf.setBackground(BAR_BG);
        barraInf.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        JLabel lblIzq = new JLabel("PLAY MORE GAMES");
        JLabel lblDer = new JLabel("SNUBBY LAND");
        lblIzq.setForeground(Color.LIGHT_GRAY); lblIzq.setFont(new Font("Arial", Font.BOLD, 11));
        lblDer.setForeground(Color.LIGHT_GRAY);  lblDer.setFont(new Font("Arial", Font.BOLD, 11));
        barraInf.add(lblIzq, BorderLayout.WEST);
        barraInf.add(lblDer, BorderLayout.EAST);

        pantalla.add(hud,       BorderLayout.NORTH);
        pantalla.add(scroll,    BorderLayout.CENTER);
        pantalla.add(barraInf,  BorderLayout.SOUTH);

        contenedor.add(pantalla, "JUEGO");
        configurarTeclado();
    }

    /**
     * Registra los atajos de teclado de movimiento y pausa en el panel de juego.
     */
    private void configurarTeclado() {
        InputMap im = panelJuego.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panelJuego.getActionMap();
        bindTecla(im, am, KeyEvent.VK_UP,    "N",  Direccion.NORTE);
        bindTecla(im, am, KeyEvent.VK_DOWN,  "S",  Direccion.SUR);
        bindTecla(im, am, KeyEvent.VK_RIGHT, "E",  Direccion.ESTE);
        bindTecla(im, am, KeyEvent.VK_LEFT,  "O",  Direccion.OESTE);
        bindTecla(im, am, KeyEvent.VK_W,     "NW", Direccion.NORTE);
        bindTecla(im, am, KeyEvent.VK_S,     "SW", Direccion.SUR);
        bindTecla(im, am, KeyEvent.VK_D,     "EW", Direccion.ESTE);
        bindTecla(im, am, KeyEvent.VK_A,     "OW", Direccion.OESTE);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "PAUSA");
        am.put("PAUSA", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { togglePausa(); }
        });
    }

    /**
     * Vincula una tecla a una dirección de movimiento del jugador.
     *
     * @param im  mapa de inputs donde se registra el atajo de teclado
     * @param am  mapa de acciones donde se registra la acción correspondiente
     * @param key código de la tecla (KeyEvent.VK_*)
     * @param id  identificador único de la acción
     * @param dir dirección de movimiento que ejecutará esta tecla
     */
    private void bindTecla(InputMap im, ActionMap am, int key, String id, Direccion dir) {
        im.put(KeyStroke.getKeyStroke(key, 0), id);
        am.put(id, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                if (pausado) return;
                modo.getJugador().mover(dir, nivel.getTablero());
                modo.actualizar();
                actualizarHUD();
                verificarEstado();
                panelJuego.repaint();
            }
        });
    }

    /**
     * Arranca los temporizadores del game loop:
     * uno para mover enemigos (cada 250 ms) y otro para contar el tiempo (cada segundo).
     */
    private void iniciarGameLoop() {
        timerLogica = new Timer(250, e -> {
            if (pausado) return;
            nivel.actualizarEnemigos();
            nivel.verificarColisiones(modo.getJugador());
            actualizarHUD();
            verificarEstado();
            panelJuego.repaint();
        });
        timerLogica.start();

        timerSegundo = new Timer(1000, e -> {
            if (pausado) return;
            nivel.avanzarTiempo();
            if (nivel.tiempoAgotado()) {
                timerLogica.stop(); timerSegundo.stop();
                JOptionPane.showMessageDialog(this,
                        "Tiempo agotado!\nMuertes: " + modo.getJugador().getMuertes(),
                        "Game Over", JOptionPane.WARNING_MESSAGE);
                volverAlMenu();
            }
        });
        timerSegundo.start();
    }

    /**
     * Verifica si el jugador ha ganado y, de ser así, detiene los timers y vuelve al menú.
     */
    private void verificarEstado() {
        if (modo.verificarVictoria()) {
            timerLogica.stop(); timerSegundo.stop();
            JOptionPane.showMessageDialog(this,
                    "¡NIVEL COMPLETADO!\nMuertes: " + modo.getJugador().getMuertes(),
                    "Victoria!", JOptionPane.INFORMATION_MESSAGE);
            volverAlMenu();
        }
    }

    /**
     * Actualiza la etiqueta de muertes en el HUD con el contador actual del jugador.
     */
    private void actualizarHUD() {
        lblMuertes.setText("DEATHS: " + modo.getJugador().getMuertes());
    }

    /**
     * Alterna el estado de pausa del juego mostrando un diálogo modal hasta que el jugador continúe.
     */
    private void togglePausa() {
        pausado = true;
        JOptionPane.showMessageDialog(this, "PAUSA - presiona OK para continuar",
                "Pausado", JOptionPane.INFORMATION_MESSAGE);
        pausado = false;
    }

    /**
     * Detiene los timers, elimina la pantalla de juego y regresa al menú principal.
     */
    private void volverAlMenu() {
        if (timerLogica  != null) timerLogica.stop();
        if (timerSegundo != null) timerSegundo.stop();
        contenedor.remove(contenedor.getComponentCount() - 1);
        cardLayout.show(contenedor, "MENU");
        pack();
        setLocationRelativeTo(null);
    }
}

package interfaz;

import dominio.EstadoDelJuego;
import dominio.Jugada;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private final transient ControladorDeJuego controladorDeJuego;
    private final transient PresentadorDeEstado presentadorDeEstado;
    private final JLabel etiquetaDeTitulo;
    private final JLabel etiquetaDeRonda;
    private final JLabel etiquetaDeMarcador;

    public VentanaPrincipal(ControladorDeJuego controladorDeJuego, PresentadorDeEstado presentadorDeEstado) { this.controladorDeJuego = controladorDeJuego; this.presentadorDeEstado = presentadorDeEstado; this.etiquetaDeTitulo = new JLabel("Piedra, Papel o Tijera", SwingConstants.CENTER); this.etiquetaDeRonda = new JLabel("", SwingConstants.CENTER); this.etiquetaDeMarcador = new JLabel("", SwingConstants.CENTER); configurarVentana(); }
    private void configurarVentana() { setTitle("Piedra, Papel o Tijera"); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setSize(760, 420); setMinimumSize(new Dimension(700, 380)); setLocationRelativeTo(null); setContentPane(crearPanelPrincipal()); refrescar(controladorDeJuego.obtenerEstadoInicial()); }
    private JPanel crearPanelPrincipal() { return configurarPanelPrincipal(new JPanel(new BorderLayout(0, 20))); }
    private JPanel configurarPanelPrincipal(JPanel panel) { panel.setBorder(new EmptyBorder(24, 24, 24, 24)); panel.setBackground(new Color(245, 247, 251)); panel.add(crearPanelSuperior(), BorderLayout.NORTH); panel.add(crearPanelCentral(), BorderLayout.CENTER); panel.add(crearPanelInferior(), BorderLayout.SOUTH); return panel; }
    private JPanel crearPanelSuperior() { return agregarComponentesEnColumna(configurarPanelSecundario(new JPanel()), List.of(configurarEtiquetaDeTitulo(), configurarEtiquetaDeRonda())); }
    private JPanel crearPanelCentral() { return configurarPanelDeBotones(configurarPanelDeBotonesBase(new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 10)))); }
    private JPanel crearPanelInferior() { return agregarMarcador(configurarPanelTarjeta(new JPanel(new BorderLayout()))); }
    private JPanel configurarPanelSecundario(JPanel panel) { panel.setOpaque(false); panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); return panel; }
    private JPanel configurarPanelDeBotonesBase(JPanel panel) { panel.setOpaque(false); return panel; }
    private JPanel configurarPanelDeBotones(JPanel panel) { agregarBotones(panel, List.of(crearBoton(Jugada.PIEDRA), crearBoton(Jugada.PAPEL), crearBoton(Jugada.TIJERA))); return panel; }
    private JPanel configurarPanelTarjeta(JPanel panel) { panel.setOpaque(true); panel.setBackground(Color.WHITE); panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 226, 235)), new EmptyBorder(16, 20, 16, 20))); return panel; }
    private JPanel agregarComponentesEnColumna(JPanel panel, List<JLabel> etiquetas) { etiquetas.forEach(panel::add); return panel; }
    private void agregarBotones(JPanel panel, List<JButton> botones) { botones.forEach(panel::add); }
    private JPanel agregarMarcador(JPanel panel) { panel.add(configurarEtiquetaDeMarcador(), BorderLayout.CENTER); return panel; }
    private JLabel configurarEtiquetaDeTitulo() { etiquetaDeTitulo.setFont(new Font("SansSerif", Font.BOLD, 28)); etiquetaDeTitulo.setForeground(new Color(33, 37, 41)); etiquetaDeTitulo.setAlignmentX(CENTER_ALIGNMENT); return etiquetaDeTitulo; }
    private JLabel configurarEtiquetaDeRonda() { etiquetaDeRonda.setFont(new Font("SansSerif", Font.PLAIN, 16)); etiquetaDeRonda.setForeground(new Color(73, 80, 87)); etiquetaDeRonda.setBorder(new EmptyBorder(18, 40, 0, 40)); etiquetaDeRonda.setAlignmentX(CENTER_ALIGNMENT); return etiquetaDeRonda; }
    private JLabel configurarEtiquetaDeMarcador() { etiquetaDeMarcador.setFont(new Font("SansSerif", Font.PLAIN, 18)); etiquetaDeMarcador.setForeground(new Color(33, 37, 41)); return etiquetaDeMarcador; }
    private JButton crearBoton(Jugada jugada) { return configurarBoton(new JButton(jugada.descripcion()), jugada); }
    private JButton configurarBoton(JButton boton, Jugada jugada) { boton.setPreferredSize(new Dimension(150, 48)); boton.setFont(new Font("SansSerif", Font.BOLD, 16)); boton.setFocusPainted(false); boton.setBackground(Color.WHITE); boton.addActionListener(evento -> refrescar(controladorDeJuego.jugar(jugada))); return boton; }
    private void refrescar(EstadoDelJuego estadoDelJuego) { etiquetaDeRonda.setText(presentadorDeEstado.construirTextoDeLaRonda(estadoDelJuego)); etiquetaDeMarcador.setText(presentadorDeEstado.construirTextoDelMarcador(estadoDelJuego)); }
}

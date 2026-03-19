package ar.fiuba.piedrapapeltijera.interfaz;

import ar.fiuba.piedrapapeltijera.dominio.EstadoDelJuego;
import ar.fiuba.piedrapapeltijera.dominio.Jugada;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private final transient ControladorDeJuego controladorDeJuego;
    private final transient PresentadorDeEstado presentadorDeEstado;
    private final JLabel etiquetaDeRonda;
    private final JLabel etiquetaDeMarcador;

    public VentanaPrincipal(ControladorDeJuego controladorDeJuego, PresentadorDeEstado presentadorDeEstado) { this.controladorDeJuego = controladorDeJuego; this.presentadorDeEstado = presentadorDeEstado; this.etiquetaDeRonda = new JLabel("", SwingConstants.CENTER); this.etiquetaDeMarcador = new JLabel("", SwingConstants.CENTER); configurarVentana(); }
    private void configurarVentana() { setTitle("Piedra, Papel o Tijera"); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setSize(640, 280); setLocationRelativeTo(null); setLayout(new BorderLayout()); add(etiquetaDeRonda, BorderLayout.NORTH); add(crearPanelDeBotones(), BorderLayout.CENTER); add(etiquetaDeMarcador, BorderLayout.SOUTH); refrescar(controladorDeJuego.obtenerEstadoInicial()); }
    private JPanel crearPanelDeBotones() { return agregarBotones(new JPanel(new GridLayout(1, 3, 12, 12))); }
    private JPanel agregarBotones(JPanel panel) { return agregarBotones(panel, List.of(crearBoton(Jugada.PIEDRA), crearBoton(Jugada.PAPEL), crearBoton(Jugada.TIJERA))); }
    private JPanel agregarBotones(JPanel panel, List<JButton> botones) { botones.forEach(panel::add); return panel; }
    private JButton crearBoton(Jugada jugada) { return configurarBoton(new JButton(jugada.descripcion()), jugada); }
    private JButton configurarBoton(JButton boton, Jugada jugada) { boton.addActionListener(evento -> refrescar(controladorDeJuego.jugar(jugada))); return boton; }
    private void refrescar(EstadoDelJuego estadoDelJuego) { etiquetaDeRonda.setText(presentadorDeEstado.construirTextoDeLaRonda(estadoDelJuego)); etiquetaDeMarcador.setText(presentadorDeEstado.construirTextoDelMarcador(estadoDelJuego)); }
}

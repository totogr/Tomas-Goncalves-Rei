package interfaz;

import dominio.FabricaDeRondas;
import dominio.JuegoPiedraPapelTijera;
import infraestructura.FuenteAleatoriaDeJugada;

import javax.swing.SwingUtilities;
import java.util.Random;

public class AplicacionPiedraPapelTijera {
    public static void main(String[] args) { SwingUtilities.invokeLater(AplicacionPiedraPapelTijera::iniciarAplicacion); }
    private static void iniciarAplicacion() { crearVentanaPrincipal().setVisible(true); }
    private static VentanaPrincipal crearVentanaPrincipal() { return new VentanaPrincipal(new ControladorDeJuego(new JuegoPiedraPapelTijera(new FuenteAleatoriaDeJugada(new Random()), new FabricaDeRondas())), new PresentadorDeEstado()); }
}
package dominio;

import interfaz.PresentadorDeEstado;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
class PresentadorDeEstadoTest {
    private final PresentadorDeEstado presentadorDeEstado = new PresentadorDeEstado();

    @Test void deberiaMostrarUnMensajeInicialSinRondas() { assertEquals("<html><div style='text-align:center;'><b>¡Bienvenido!</b><br/>Elegí piedra, papel o tijera para comenzar a jugar.</div></html>", presentadorDeEstado.construirTextoDeLaRonda(EstadoDelJuego.inicial())); }
    @Test void deberiaMostrarElMarcadorConSusValores() { assertEquals("<html><div style='text-align:center;'><b>Marcador</b><br/>Ganadas: 2 &nbsp;&nbsp; Perdidas: 1 &nbsp;&nbsp; Empates: 3</div></html>", presentadorDeEstado.construirTextoDelMarcador(new EstadoDelJuego(new Marcador(2, 1, 3), null))); }
}
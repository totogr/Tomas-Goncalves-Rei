package ar.fiuba.piedrapapeltijera.dominio;

import ar.fiuba.piedrapapeltijera.interfaz.PresentadorDeEstado;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PresentadorDeEstadoTest {
    private final PresentadorDeEstado presentadorDeEstado = new PresentadorDeEstado();

    @Test void deberiaMostrarUnMensajeInicialSinRondas() { assertEquals("Elegí una opción para comenzar a jugar", presentadorDeEstado.construirTextoDeLaRonda(EstadoDelJuego.inicial())); }
    @Test void deberiaMostrarElMarcadorConSusValores() { assertEquals("Ganadas: 2 | Perdidas: 1 | Empates: 3", presentadorDeEstado.construirTextoDelMarcador(new EstadoDelJuego(new Marcador(2, 1, 3), null))); }
}

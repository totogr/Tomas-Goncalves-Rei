package integracion;

import dominio.EstadoDelJuego;
import dominio.FabricaDeRondas;
import dominio.JuegoPiedraPapelTijera;
import dominio.Jugada;
import dominio.Marcador;
import dominio.Resultado;
import dominio.Ronda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JuegoPiedraPapelTijeraTest {
    @Test void deberiaActualizarElMarcadorYLaultimaRondaLuegoDeJugar() { assertEquals(estadoEsperado(), crearJuego().jugar(Jugada.PIEDRA)); }

    private JuegoPiedraPapelTijera crearJuego() { return new JuegoPiedraPapelTijera(new FuenteFijaDeJugada(Jugada.TIJERA), new FabricaDeRondas()); }
    private EstadoDelJuego estadoEsperado() { return new EstadoDelJuego(new Marcador(1, 0, 0), new Ronda(Jugada.PIEDRA, Jugada.TIJERA, Resultado.GANADA, Jugada.PIEDRA)); }
}

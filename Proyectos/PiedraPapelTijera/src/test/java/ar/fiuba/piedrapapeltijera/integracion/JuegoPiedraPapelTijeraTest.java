package ar.fiuba.piedrapapeltijera.integracion;

import ar.fiuba.piedrapapeltijera.dominio.EstadoDelJuego;
import ar.fiuba.piedrapapeltijera.dominio.FabricaDeRondas;
import ar.fiuba.piedrapapeltijera.dominio.JuegoPiedraPapelTijera;
import ar.fiuba.piedrapapeltijera.dominio.Jugada;
import ar.fiuba.piedrapapeltijera.dominio.Marcador;
import ar.fiuba.piedrapapeltijera.dominio.Resultado;
import ar.fiuba.piedrapapeltijera.dominio.TablaDeResultados;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JuegoPiedraPapelTijeraTest {
    @Test void deberiaActualizarElMarcadorYLaultimaRondaLuegoDeJugar() { assertEquals(estadoEsperado(), crearJuego().jugar(Jugada.PIEDRA)); }

    private JuegoPiedraPapelTijera crearJuego() { return new JuegoPiedraPapelTijera(new FuenteFijaDeJugada(Jugada.TIJERA), new FabricaDeRondas(new TablaDeResultados())); }
    private EstadoDelJuego estadoEsperado() { return new EstadoDelJuego(new Marcador(1, 0, 0), new ar.fiuba.piedrapapeltijera.dominio.Ronda(Jugada.PIEDRA, Jugada.TIJERA, Resultado.GANADA)); }
}

package ar.fiuba.piedrapapeltijera.interfaz;

import ar.fiuba.piedrapapeltijera.dominio.EstadoDelJuego;
import ar.fiuba.piedrapapeltijera.dominio.Ronda;

public class PresentadorDeEstado {
    public String construirTextoDelMarcador(EstadoDelJuego estadoDelJuego) { return "Ganadas: %d | Perdidas: %d | Empates: %d".formatted(estadoDelJuego.marcador().ganadas(), estadoDelJuego.marcador().perdidas(), estadoDelJuego.marcador().empates()); }
    public String construirTextoDeLaRonda(EstadoDelJuego estadoDelJuego) { return TextoDeRonda.POR_PRESENCIA_DE_RONDA.get(estadoDelJuego.ultimaRonda() != null).apply(estadoDelJuego.ultimaRonda()); }
}

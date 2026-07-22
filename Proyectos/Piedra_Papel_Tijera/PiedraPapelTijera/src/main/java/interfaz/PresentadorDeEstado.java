package interfaz;

import dominio.EstadoDelJuego;
import dominio.Ronda;

import java.util.Optional;

public class PresentadorDeEstado {
    public String construirTextoDelMarcador(EstadoDelJuego estadoDelJuego) { return "<html><div style='text-align:center;'><b>Marcador</b><br/>Ganadas: %d &nbsp;&nbsp; Perdidas: %d &nbsp;&nbsp; Empates: %d</div></html>".formatted(estadoDelJuego.marcador().ganadas(), estadoDelJuego.marcador().perdidas(), estadoDelJuego.marcador().empates()); }
    public String construirTextoDeLaRonda(EstadoDelJuego estadoDelJuego) { return Optional.ofNullable(estadoDelJuego.ultimaRonda()).map(this::construirTextoDeLaRonda).orElse("<html><div style='text-align:center;'><b>¡Bienvenido!</b><br/>Elegí piedra, papel o tijera para comenzar a jugar.</div></html>"); }
    private String construirTextoDeLaRonda(Ronda ronda) { return "<html><div style='text-align:center;'><b>%s</b><br/>Vos jugaste %s, la computadora jugó %s y la jugada ganadora fue %s.</div></html>".formatted(ronda.resultado().descripcion(), ronda.jugadaDelUsuario().descripcion(), ronda.jugadaDeLaComputadora().descripcion(), ronda.jugadaGanadora().descripcion()); }
}

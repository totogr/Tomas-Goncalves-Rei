package org.robocop.modelo.Robot;

import org.robocop.modelo.Posicion;

import java.util.List;

public interface Robot {
    Posicion getPosicion();
    EstadoRobot getEstado();
    void destruir();
    List<Posicion> calcularTrayectoria(Posicion objetivo, List<Posicion> posicionesBloqueadas);
    void moverA(Posicion nuevaPosicion);
    TipoRobot getTipoRobot();
}
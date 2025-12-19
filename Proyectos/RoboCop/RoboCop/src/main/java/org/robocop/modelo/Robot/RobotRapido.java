package org.robocop.modelo.Robot;

import org.robocop.modelo.Posicion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RobotRapido implements Robot {
    private Posicion posicion;
    private EstadoRobot estado;

    public RobotRapido(Posicion posicionInicial) {
        this.posicion = posicionInicial;
        this.estado = EstadoRobot.INTACTO;
    }

    @Override
    public Posicion getPosicion() {
        return posicion;
    }

    @Override
    public EstadoRobot getEstado() {
        return estado;
    }

    @Override
    public void destruir() {
        this.estado = EstadoRobot.INCENDIADO;
    }

    @Override
    public List<Posicion> calcularTrayectoria(Posicion objetivo, List<Posicion> posicionesBloqueadas) {
        if (estado != EstadoRobot.INTACTO) return Collections.emptyList();

        List<Posicion> trayectoria = new ArrayList<>();

        List<Posicion> primerPaso = calcularUnPaso(this.posicion, objetivo, posicionesBloqueadas);

        if (primerPaso.isEmpty()) return trayectoria;

        Posicion paso1 = primerPaso.get(0);
        trayectoria.add(paso1);

        List<Posicion> segundoPaso = calcularUnPaso(paso1, objetivo, posicionesBloqueadas);

        if (!segundoPaso.isEmpty()) {
            trayectoria.add(segundoPaso.get(0));
        }

        return trayectoria;
    }

    private List<Posicion> calcularUnPaso(Posicion desde, Posicion objetivo, List<Posicion> bloqueadas) {
        int deltaFila = Integer.compare(objetivo.getFila(), desde.getFila());
        int deltaColumna = Integer.compare(objetivo.getColumna(), desde.getColumna());

        List<Posicion> opciones = new ArrayList<>();

        opciones.add(desde.mover(deltaFila, deltaColumna));
        opciones.add(desde.mover(deltaFila, deltaColumna - 1));
        opciones.add(desde.mover(deltaFila, deltaColumna + 1));
        opciones.add(desde.mover(deltaFila, 0));
        opciones.add(desde.mover(0, deltaColumna));

        for (Posicion paso : opciones) {
            if (!bloqueadas.contains(paso)) {
                return List.of(paso);
            }
        }

        return Collections.emptyList();
    }

    @Override
    public void moverA(Posicion nuevaPosicion) {
        if (estado == EstadoRobot.INTACTO) {
            this.posicion = nuevaPosicion;
        }
    }

    @Override
    public String toString() {
        return "RobotRapido{" +
                "posicion=" + posicion +
                ", estado=" + estado +
                '}';
    }
    @Override
    public TipoRobot getTipoRobot(){
        return TipoRobot.RAPIDO;
    }
}
